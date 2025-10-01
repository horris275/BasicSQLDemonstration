package com.github.horris275.basicsqldemonstration.ui.controllers;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DatabaseService;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;
import com.github.horris275.basicsqldemonstration.utils.NumberUtils;
import com.github.horris275.basicsqldemonstration.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * The controller class for the "Delete" tab operations in the user interface.
 *
 * <p>This controller handles the deletion of database rows based on
 * a user-provided unique identifier. It makes use of {@link DynamicDatabaseService}
 * to provide this functionality.</p>
 *
 * @author horris275
 * @version 22.09.2025
 */
public class DeleteTabController
{
    private final DynamicDatabaseService databaseService;
    @FXML private TextField idField;

    /**
     * Constructs a new {@code DeleteTabController} with the given database service.
     *
     * @param databaseService the service used to access and modify the database
     */
    public DeleteTabController(DynamicDatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    /**
     * Handles the submit action for the delete operation.
     *
     * @param event the action triggered by the user
     */
    @FXML
    protected void handleSubmit(ActionEvent event)
    {
        int id = NumberUtils.toInteger(idField.getText());

        if (!validateInput(id))
        {
            UIUtils.alert("Please enter a valid integer that represents a data row!", Alert.AlertType.WARNING);
            return;
        }

        try
        {
            databaseService.delete(id);
            UIUtils.alert("Entry " + id + " has been successfully deleted from the database!", Alert.AlertType.INFORMATION);
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
}