package com.github.horris275.basicsqldemonstration.ui.controllers;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.DatabaseRow;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;
import com.github.horris275.basicsqldemonstration.utils.UIUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * The controller class for the "Insert" tab operations in the user interface.
 *
 * <p>This controller handles the insertion of database rows when interacting
 * with the insert tab. It makes use of {@link DynamicDatabaseService} to provide functionality.</p>
 *
 * @author horris275
 * @version 17.09.2025
 */
public class InsertTabController
{
    private final DynamicDatabaseService databaseService;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField urlField;

    /**
     * Constructs a new {@code InsertTabController} with the given database service.
     *
     * @param databaseService the service used to insert the database rows
     */
    public InsertTabController(DynamicDatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    /**
     * Handles the submit action for the insert operation.
     *
     * @param event the action triggered by the user
     */
    @FXML
    private void handleSubmit(ActionEvent event)
    {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String url = urlField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || url.isEmpty())
        {
            UIUtils.alert("Please fill in all fields before submitting!", Alert.AlertType.WARNING);
            return;
        }

        try
        {
            DatabaseRow databaseRow = new DatabaseRow();

            databaseRow.setColumn("id", -1);
            databaseRow.setColumn("title", title);
            databaseRow.setColumn("description", description);
            databaseRow.setColumn("url", url);

            databaseService.insert(databaseRow);
            resetFields();
            UIUtils.alert("Data has successfully been inserted into the database!", Alert.AlertType.INFORMATION);
        }
        catch (DatabaseException e)
        {
            UIUtils.alert("Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Clears the input fields in the insert tab form.
     */
    private void resetFields()
    {
        titleField.clear();
        descriptionField.clear();
        urlField.clear();
    }
}