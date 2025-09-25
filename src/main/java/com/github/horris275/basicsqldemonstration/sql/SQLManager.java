package com.github.horris275.basicsqldemonstration.sql;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A concrete implementation of {@link DatabaseService} using a SQL Database (MariaDB).
 * The class provides CRUD operations for a specific table in a SQL database.
 * The current schema outlines the columns as id, title, description, and URL.
 *
 * @author horris275
 * @version 25.09.2025
 */
public class SQLManager implements DatabaseService
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
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String url = resultSet.getString("url");

                databaseRows.add(new DatabaseRow(id, title, description, url));
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
                if (resultSet.next())
                {
                    int identifier = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String url = resultSet.getString("url");

                    return Optional.of(new DatabaseRow(identifier, title, description, url));
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while retrieving row with id=" + id, e);
        }

        return Optional.empty();
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
        String query = "INSERT INTO " + table + " (title, description, url) VALUES (?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, databaseRow.getTitle());
            statement.setString(2, databaseRow.getDescription());
            statement.setString(3, databaseRow.getUrl());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    int newId = generatedKeys.getInt(1);
                    databaseRow.setUniqueId(newId);
                }
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("An error has occurred while inserting data with title=" + databaseRow.getTitle(), e);
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
        String query = "UPDATE " + table + " SET title = ?, description = ?, url = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, databaseRow.getTitle());
            statement.setString(2, databaseRow.getDescription());
            statement.setString(3, databaseRow.getUrl());
            statement.setInt(4, id);

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
     * Creates and returns a new connection to the database.
     *
     * @return              a {@link Connection} object representing a connection to the database
     * @throws SQLException if a database access errors occurs or the connection cannot be established
     */
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(databasePath, user, password);
    }
}