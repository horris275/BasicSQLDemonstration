package com.github.horris275.basicsqldemonstration.sql;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a row in a database table.
 *
 * <p>It contains a {@link LinkedHashMap} which represents the column
 * name and the associated data. The unique identifier (if required) is controlled
 * by a separate field {@code uniqueId} and must not be used within the LinkedHashMap.</p>
 *
 * @author horris275
 * @version 01.10.2025
 */
public class DatabaseRow
{
    private final Map<String, Object> columns;
    private int uniqueId;

    /**
     * Constructs a new {@code DatabaseRow} without any parameters.
     * Initialises the {@code columns} field as an empty {@link LinkedHashMap}
     */
    public DatabaseRow()
    {
        this(-1, new LinkedHashMap<>());
    }

    /**
     * Constructs a new {@code DatabaseRow} with a predefined column data.
     * Assigns the {@code columns} Map as a new {@link LinkedHashMap}
     *
     * @param uniqueId an integer representing the unique identifier
     * @param columns  the initial column names and values to populate this row
     */
    public DatabaseRow(int uniqueId, Map<String, Object> columns)
    {
        this.columns = new LinkedHashMap<>(columns);
        this.uniqueId = uniqueId;
    }

    /**
     * Returns the unique identifier of this database row.
     *
     * @return the unique identifier
     */
    public int getUniqueId()
    {
        return uniqueId;
    }

    /**
     * Sets a new unique identifier for this database row.
     * This method only completes successfully if the identifier
     * is -1, as used by the database insertion method to
     * update the identifier upon creation.
     *
     * @param newId the new unique identifier
     */
    public void setUniqueId(int newId)
    {
        if (uniqueId == -1)
        {
            uniqueId = newId;
        }
    }

    /**
     * Returns the value of the given column.
     *
     * @param columnName the name of the column to return
     * @return           the value of the column, or null if not present
     */
    public Object getColumn(String columnName)
    {
        return columns.get(columnName);
    }

    /**
     * Sets a new column name and associated value in this database row.
     *
     * @param columnName the name of the column to insert
     * @param value      the value associated with the column
     */
    public void setColumn(String columnName, Object value)
    {
        columns.put(columnName, value);
    }

    /**
     * Returns an unmodifiable list of the column names.
     *
     * @return the unmodifiable list of column names
     */
    public List<String> getColumnNames()
    {
        return List.copyOf(columns.keySet());
    }

    /**
     * Returns an unmodifiable {@link Map} of column names and values.
     *
     * @return the unmodifiable {@link Map} of column names and values
     */
    public Map<String, Object> getColumnValues()
    {
        return Collections.unmodifiableMap(columns);
    }
}