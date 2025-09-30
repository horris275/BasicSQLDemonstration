package com.github.horris275.basicsqldemonstration.sql.interfaces;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;

import java.util.List;

/**
 * This interface represents an object that provides dynamic database services.
 * It extends the {@link DatabaseService} interface and provides an additional
 * method that enables the retrieval of column names for dynamic systems.
 *
 * @author horris275
 * @version 30.09.2025
 */
public interface DynamicDatabaseService extends DatabaseService
{
    /**
     * Retrieves the column names dynamically from the database table.
     *
     * @return                   a list of strings that represent the column names
     * @throws DatabaseException if a database access error occurs
     */
    List<String> retrieveColumnNames() throws DatabaseException;
}