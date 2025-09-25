package com.github.horris275.basicsqldemonstration.utils;

/**
 * A utility class that provides number-related helper methods.
 *
 * @author horris275
 * @version 22.09.2025
 */
public final class NumberUtils
{
    /**
     * Converts the specified string representation of a number to an integer.
     *
     * @param strData the string representation of the integer
     * @return        the converted integer, or {@code -1} if the string cannot be parsed
     */
    public static int toInteger(String strData)
    {
        try
        {
            return Integer.parseInt(strData.trim());
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private NumberUtils() {}
}