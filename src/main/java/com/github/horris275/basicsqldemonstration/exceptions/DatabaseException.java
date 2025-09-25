package com.github.horris275.basicsqldemonstration.exceptions;

/**
 * A custom exception used for database-related errors.
 *
 * @author horris275
 * @version 25.09.2025
 */
public class DatabaseException extends RuntimeException
{
    /**
     * Constructs a new {@code DatabaseException} with a specified detail message.
     *
     * @param errorMessage the detail message that describes the cause of the exception
     */
    public DatabaseException(String errorMessage)
    {
        super(errorMessage);
    }

    /**
     * Constructs a new {@code DatabaseException} with a
     * specified detail message underlying cause.
     *
     * @param errorMessage the detail message that describes the cause of the exception
     * @param cause        the underlying cause of this exception
     */
    public DatabaseException(String errorMessage, Throwable cause)
    {
        super(errorMessage, cause);
    }
}