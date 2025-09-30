package com.github.horris275.basicsqldemonstration.sql.interfaces;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.DatabaseRow;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents an object that can provide common
 * database-related functionality (CRUD).
 *
 * <p>Each method may throw a {@link DatabaseException} if a database
 * error occurs during the operation.</p>
 *
 * @author horris275
 * @version 25.09.2025
 */
public interface DatabaseService
{
    /**
     * Retrieves all rows within the database.
     *
     * @return                   a list containing all database rows; never {@code null}
     * @throws DatabaseException if a database access error occurs
     */
    List<DatabaseRow> fetchAll() throws DatabaseException;

    /**
     * Retrieves a single row by its unique identifier.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @return                   an {@code Optional} containing the row if found, or empty if not
     * @throws DatabaseException if a database access error occurs
     */
    Optional<DatabaseRow> fetch(int id) throws DatabaseException;

    /**
     * Checks whether a row with the given identifier exists in the database.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @return                   {@code true} if the row exists, otherwise {@code false}
     * @throws DatabaseException if a database access error occurs
     */
    boolean check(int id) throws DatabaseException;

    /**
     * Inserts a new row into the database.
     *
     * @param databaseRow        the {@link DatabaseRow} to insert
     * @throws DatabaseException if a database access error occurs
     */
    void insert(DatabaseRow databaseRow) throws DatabaseException;

    /**
     * Updates an existing row in the database with new values.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @param databaseRow        the {@link DatabaseRow} containing the updates
     * @throws DatabaseException if a database access error occurs
     */
    void modify(int id, DatabaseRow databaseRow) throws DatabaseException;

    /**
     * Deletes a row from the database.
     *
     * @param id                 an integer representing the unique identifier of the row
     * @throws DatabaseException if a database access error occurs
     */
    void delete(int id) throws DatabaseException;
}