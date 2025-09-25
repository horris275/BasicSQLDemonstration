package com.github.horris275.basicsqldemonstration.utils;

import javafx.scene.control.Alert;

import java.util.Map;

/**
 * A utility class that provides user interface related functionality.
 *
 * <p>
 * Currently it provides an alert method that can be used to offer feedback to users.
 * </p>
 *
 * @author horris275
 * @version 23.09.2025
 */
public final class UIUtils
{
    private static final Map<Alert.AlertType, String> TITLES = Map.of(
            Alert.AlertType.INFORMATION, "Information Alert:",
            Alert.AlertType.WARNING, "Warning Alert:",
            Alert.AlertType.CONFIRMATION, "Confirmation Alert:",
            Alert.AlertType.ERROR, "Error Alert:"
    );

    /**
     * Displays an alert dialog with the specified header text and alert type.
     *
     * @param header the header text to display in the alert dialog
     * @param type   the type of alert to show (e.g., {@link Alert.AlertType#INFORMATION}
     */
    public static void alert(String header, Alert.AlertType type)
    {
        Alert alert = new Alert(type);
        alert.setTitle(TITLES.getOrDefault(type, "Alert"));
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private UIUtils() {}
}