package com.github.horris275.basicsqldemonstration.sql;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;

import java.sql.*;
import java.util.*;

/**
 * A concrete implementation of {@link DynamicDatabaseService} using a SQL Database (MariaDB).
 * The class provides CRUD operations for a specific table in a SQL database.
 * The current schema outlines the columns as id, title, description, and URL.
 * Additionally, it adds a method to retrieve column names dynamically.
 *
 * @author horris275
 * @version 01.10.2025a
 */
public class SQLManager implements DynamicDatabaseService
{
    private final String table;
    private final String user;
    private final String password;
    private final String databasePath;

    /**
     * Constructs a new {@code SQLManager} with the desired database connection properties.
     *
     * @param address  the database host address (e.g. "localhost")
     * @param port     the port the database is listening on (e.g. "3306")
     * @param database the name of the database
     * @param table    the table name to operate on
     * @param user     the database username
     * @param password the database password
     */
    public SQLManager(String address, String port, String database, String table, String user, String password)
    {
        this.table = table;
        this.user = user;
        this.password = password;
        this.databasePath = "jdbc:mariadb://" + address + ":" + port + "/" + database;
    }

    /**
     * Retrieves all rows within the database.
     *
     * @return                   a list containing all database rows; never {@code null}
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public List<DatabaseRow> fetchAll() throws DatabaseException
    {
        List<DatabaseRow> databaseRows = new ArrayList<>();
        String query = "SELECT * FROM " + table;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query))
        {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next())
            {
                DatabaseRow databaseRow = new DatabaseRow();

                for (int count = 1; count <= columnCount; count++)
                {
                    String columnName = metaData.getColumnName(count);
                    Object value = resultSet.getObject(columnName);
                    databaseRow.setColumn(columnName, value);
                }

                databaseRows.add(databaseRow);
            }
        }
        catch(SQLException e)
        {
            throw new DatabaseException("An error has occurred while attempting to retrieve all rows", e);
        }

        return databaseRows;
    }

    /**
     * Retrieves a single row by its unique identifier.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @return                   an {@code Optional} containing the row if found, or empty if not
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public Optional<DatabaseRow> fetch(int id) throws DatabaseException
    {
        String query = "SELECT * FROM " + table + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery())
            {
                if (!resultSet.next())
                {
                    return Optional.empty();
                }

                DatabaseRow databaseRow = new DatabaseRow();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int count = 1; count <= columnCount; count++)
                {
                    String columnName = metaData.getColumnName(count);

                    if (columnName.equalsIgnoreCase("ID"))
                    {
                        continue;
                    }

                    databaseRow.setColumn(columnName, resultSet.getObject(columnName));
                }

                databaseRow.setUniqueId(id);

                return Optional.of(databaseRow);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while retrieving row with id=" + id, e);
        }
    }

    /**
     * Checks whether a row with the given identifier exists in the database.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @return                   {@code true} if the row exists, otherwise {@code false}
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public boolean check(int id) throws DatabaseException
    {
        String query = "SELECT 1 FROM " + table + " WHERE id = ? LIMIT 1";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery())
            {
                return resultSet.next();
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while checking for a row with id=" + id, e);
        }
    }

    /**
     * Inserts a new row into the database.
     *
     * @param databaseRow        the {@link DatabaseRow} to insert
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public void insert(DatabaseRow databaseRow) throws DatabaseException
    {
        Map<String, Object> columns = databaseRow.getColumnValues();
        String query = createPreparedQuery("INSERT INTO " + table + " (%columns) VALUES (%placeholders)", columns);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            int count = 1;

            for (String column : columns.keySet())
            {
                statement.setObject(count++, databaseRow.getColumn(column));
            }

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys())
            {
                if (generatedKeys.next() && columns.containsKey("id"))
                {
                    databaseRow.setUniqueId(generatedKeys.getInt("id"));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while inserting the selected data", e);
        }
    }

    /**
     * Updates an existing row in the database with new values.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @param databaseRow        the {@link DatabaseRow} containing the updates
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public void modify(int id, DatabaseRow databaseRow) throws DatabaseException
    {
        Map<String, Object> columns = databaseRow.getColumnValues();
        String query = createPreparedModifyQuery("UPDATE " + table + " SET %statement WHERE id = ?", columns);

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            int count = 1;

            for (String column : columns.keySet())
            {
                statement.setObject(count++, databaseRow.getColumn(column));
            }

            if (query.contains("WHERE"))
            {
                statement.setInt(4, id);
            }

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while updating row with id=" + id, e);
        }
    }

    /**
     * Deletes a row from the database.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @throws DatabaseException if a database access error occurs
     */
    @Override
    public void delete(int id) throws DatabaseException
    {
        String query = "DELETE FROM " + table + " WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while deleting row with id=" + id, e);
        }
    }

    /**
     * Retrieves the column names dynamically from the database table.
     *
     * @return                   a list of strings that represent the column names
     * @throws DatabaseException if a database access error occurs
     */
    public List<String> retrieveColumnNames()
    {
        String query = "SELECT * FROM " + table + " LIMIT 1";
        List<String> columnNames = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query))
        {
            ResultSetMetaData metaData = resultSet.getMetaData();

            for (int count = 1; count <= metaData.getColumnCount(); count++)
            {
                columnNames.add(metaData.getColumnName(count));
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while attempting to retrieve the column names", e);
        }

        return columnNames;
    }

    /**
     * Creates and returns a new connection to the database.
     *
     * @return              a {@link Connection} object representing a connection to the database
     * @throws SQLException if a database access errors occurs or the connection cannot be established
     */
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(databasePath, user, password);
    }

    /**
     * Creates a prepared statement with specified columns and parameterised placeholders.
     * The base query must contain %columns and %placeholders, of which are replaced
     * with the actual column names and the correct number of placeholders.
     *
     * @param baseQuery the SQL query containing %columns and %placeholders
     * @param columns   a map of column names to values
     * @return          the SQL query with actual column names and placeholders inserted
     */
    private String createPreparedQuery(String baseQuery, Map<String, Object> columns)
    {
        String columnNames = toQueryColumns(columns.keySet());
        String placeholders = toQueryPlaceholders(columns.size());

        return baseQuery.replace("%columns", columnNames)
                .replace("%placeholders", placeholders);
    }

    private String createPreparedModifyQuery(String baseQuery, Map<String, Object> columns)
    {
        String modifyStatement = toModifyStatement(columns.keySet());

        if (!columns.containsKey("id"))
        {
            modifyStatement = modifyStatement.replace(" WHERE id = ?", "");
        }

        return baseQuery.replace("%statement", modifyStatement);
    }

    /**
     * Converts a set of column names into a comma-separated string.
     *
     * @param columnNames the set of column names to join
     * @return            a comma-separated string of column names
     */
    private String toQueryColumns(Set<String> columnNames)
    {
        return String.join(", ", columnNames);
    }

    /**
     * Generates a comma-separated string of parameter placeholders.
     *
     * @param columnCount the number of placeholders to generate
     * @return            a string representing the placeholders
     */
    private String toQueryPlaceholders(int columnCount)
    {
        StringBuilder builder = new StringBuilder();

        for (int count = 1; count <= columnCount; count ++)
        {
            builder.append("?");

            if (count < columnCount)
            {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    /**
     *
     *
     * @param columnNames
     * @return
     */
    private String toModifyStatement(Set<String> columnNames)
    {
        StringBuilder builder = new StringBuilder();
        int count = 1;
        int columnCount = columnNames.size();

        for (String columnName : columnNames)
        {
            if (columnName.equals("id"))
            {
                count++;
                continue;
            }

            builder.append(columnName).append(" = ?");

            if (count++ < columnCount)
            {
                builder.append(", ");
            }
        }

        return builder.toString();
    }
}