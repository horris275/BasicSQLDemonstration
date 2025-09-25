package com.github.horris275.basicsqldemonstration.sql;

/**
 * Represents a row in the database with a unique identifier,
 * title, description, and URL.
 *
 * <p>A {@code uniqueId} of {@code -1} indicates that the row
 * has not yet been assigned a persistent identifier by the database.
 * Once created, the database will update the value to reflect the identifier.</p>
 *
 * @author horris275
 * @version 22.09.2025
 */
public class DatabaseRow
{
    private int uniqueId;
    private final String title;
    private final String description;
    private final String url;

    /**
     * Constructs a new {@code DatabaseRow} with the unique identifier,
     * title, description, and URL.
     *
     * @param uniqueId    an integer representing the unique identifier
     * @param title       the title of the database row
     * @param description the description of the database row
     * @param url         the url of the database row
     */
    public DatabaseRow(int uniqueId, String title, String description, String url)
    {
        this.uniqueId = uniqueId;
        this.title = title;
        this.description = description;
        this.url = url;
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
        if (this.uniqueId == -1)
        {
            this.uniqueId = newId;
        }
    }

    /**
     * Returns the title of this database row.
     *
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Returns the description of this database row.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the URL of this database row.
     *
     * @return the URL
     */
    public String getUrl()
    {
        return url;
    }
}