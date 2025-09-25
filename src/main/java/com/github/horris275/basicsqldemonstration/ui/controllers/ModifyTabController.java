package com.github.horris275.basicsqldemonstration.ui.controllers;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.DatabaseRow;
import com.github.horris275.basicsqldemonstration.sql.DatabaseService;
import com.github.horris275.basicsqldemonstration.utils.NumberUtils;
import com.github.horris275.basicsqldemonstration.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * The controller class for the "Modify" tab operations in the user interface.
 *
 * <p>This controller handles the modification of database rows when interacting
 * with the modify tab. It makes use of {@link DatabaseService} to provide functionality.</p>
 *
 * @author horris275
 * @version 23.09.2025
 */
public class ModifyTabController
{
    private final DatabaseService databaseService;
    @FXML private TextField idField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private TextField urlField;

    /**
     * Constructs a new {@code ModifyTabController} with the given database service.
     *
     * @param databaseService the service used to modify the database rows
     */
    public ModifyTabController(DatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    /**
     * Handles the search action for the modify operation.
     * It validates the unique identifier inputted by the user and populates
     * the other fields with the current data within the database.
     *
     * @param event the action triggered by the user
     */
    @FXML
    protected void handleSearch(ActionEvent event)
    {
        int identifier = NumberUtils.toInteger(idField.getText());

        if (!validateInput(identifier))
        {
            UIUtils.alert("Please enter a valid integer that represents a data row!", Alert.AlertType.WARNING);
            resetFields();
            return;
        }

        try
        {
            databaseService.fetch(identifier).ifPresentOrElse(row -> {
                idField.setEditable(false);
                titleField.setText(row.getTitle());
                descriptionField.setText(row.getDescription());
                urlField.setText(row.getUrl());
            }, () -> UIUtils.alert("Unable to obtain the desired database row", Alert.AlertType.ERROR));
        }
        catch (DatabaseException e)
        {
            UIUtils.alert("Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the submit action for the modify operation.
     *
     * @param event the action triggered by the user
     */
    @FXML
    protected void handleSubmit(ActionEvent event)
    {
        int identifier = NumberUtils.toInteger(idField.getText());
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String url = urlField.getText().trim();

        if (identifier <= 0 || title.isEmpty() || description.isEmpty() || url.isEmpty())
        {
            UIUtils.alert("Please fill in all fields before submitting!", Alert.AlertType.WARNING);
            return;
        }

        try
        {
            databaseService.modify(identifier, new DatabaseRow(identifier, title, description, url));
            idField.setEditable(true);
            resetFields();
            UIUtils.alert("The data row has successfully been updated!", Alert.AlertType.INFORMATION);
        }
        catch (DatabaseException e)
        {
            UIUtils.alert("Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Validates the user input by checking if a row with a given identifier exists.
     *
     * @param id the unique identifier of the row to validate
     * @return   {@code true} if the identifier exists in the database, otherwise {@code false}
     */
    private boolean validateInput(int id)
    {
        try
        {
            return databaseService.check(id);
        }
        catch (DatabaseException e)
        {
            return false;
        }
    }

    /**
     * Clears the input fields in the modify tab form.
     */
    private void resetFields()
    {
        idField.clear();
        titleField.clear();
        descriptionField.clear();
        urlField.clear();
    }
}