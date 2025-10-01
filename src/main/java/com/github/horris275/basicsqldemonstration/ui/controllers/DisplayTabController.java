package com.github.horris275.basicsqldemonstration.ui.controllers;

import com.github.horris275.basicsqldemonstration.exceptions.DatabaseException;
import com.github.horris275.basicsqldemonstration.sql.DatabaseRow;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;
import com.github.horris275.basicsqldemonstration.utils.UIUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

/**
 * The controller class for the "Display" tab operations in the user interface.
 *
 * <p>This controller handles the updating of database rows when interacting
 * with the display tab. It makes use of {@link DynamicDatabaseService} to provide functionality.</p>
 *
 * <p>It also makes use of the {@code #initialize} method to create the table columns dynamically</p>
 *
 * @author horris275
 * @version 01.10.2025
 */
public class DisplayTabController
{
    private final DynamicDatabaseService databaseService;
    @FXML private TableView<DatabaseRow> table;

    /**
     * Constructs a new {@code DisplayTabController} with the given database service.
     *
     * @param databaseService the service used to retrieve the database rows
     */
    public DisplayTabController(DynamicDatabaseService databaseService)
    {
        this.databaseService = databaseService;
    }

    /**
     * Creates a {@link TableColumn} for each column name retrieved from the database.
     */
    public void initialize()
    {
        List<String> columnNames = databaseService.retrieveColumnNames();

        for (String columnName : columnNames)
        {
            TableColumn<DatabaseRow, String> tableColumn = new TableColumn<>(columnName);

            tableColumn.setCellValueFactory(cellData -> {
                DatabaseRow row = cellData.getValue();
                Object value = row.getColumn(columnName);
                return new SimpleStringProperty(value != null ? value.toString() : "");
            });

            table.getColumns().add(tableColumn);
        }
    }

    /**
     * Updates the {@link TableView} with the current contents of the database.
     * This method fetches all rows from the database using {@link DynamicDatabaseService#fetchAll()}
     * and populates the table with an observable list of {@link DatabaseRow} objects.
     */
    public void updateTable()
    {
        try
        {
            List<DatabaseRow> databaseRows = databaseService.fetchAll();
            ObservableList<DatabaseRow> observableDatabaseRows = FXCollections.observableArrayList(databaseRows);
            table.setItems(observableDatabaseRows);
        }
        catch (DatabaseException e)
        {
            UIUtils.alert("Database error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}