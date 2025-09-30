package com.github.horris275.basicsqldemonstration.sql;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a row in a database table.
 *
 * <p>This class is a work in progress and is not yet functioning as intended</p>
 *
 * @author horris275
 * @version 30.09.2025
 */
public class DatabaseRow
{
    private final Map<String, Object> columns;

    /**
     * Constructs a new {@code DatabaseRow} without any parameters.
     * Initialises the {@code columns} field as an empty {@link LinkedHashMap}
     */
    public DatabaseRow()
    {
        this(new LinkedHashMap<>());
    }

    /**
     * Constructs a new {@code DatabaseRow} without any parameters.
     * Assigns the {@code columns} Map as a new {@link LinkedHashMap}
     *
     * @param columns the initial column names and values to populate this row
     */
    public DatabaseRow(Map<String, Object> columns)
    {
        this.columns = new LinkedHashMap<>(columns);
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

/*
class OldDatabaseRow
{
    public OldDatabaseRow(int uniqueId, String title, String description, String url)
    {
        this.uniqueId = uniqueId;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public int getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(int newId)
    {
        if (this.uniqueId == -1)
        {
            this.uniqueId = newId;
        }
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUrl()
    {
        return url;
    }
}*/