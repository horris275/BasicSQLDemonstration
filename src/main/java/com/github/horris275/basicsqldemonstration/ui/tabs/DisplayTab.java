package com.github.horris275.basicsqldemonstration.ui.tabs;

import com.github.horris275.basicsqldemonstration.sql.interfaces.DatabaseService;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;
import com.github.horris275.basicsqldemonstration.ui.controllers.DisplayTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * A custom {@link Tab} representing the "Display" tab in the user interface.
 *
 * <p>This tab enables users to view database rows using a {@link DisplayTabController}.
 * The tab is non-closable and automatically loads the FXML layout.
 * If the FXML layout fails to load, a {@link RuntimeException} is thrown.</p>
 *
 * @author horris275
 * @version 22.09.2025
 */
public class DisplayTab extends Tab
{
    private final DisplayTabController displayTabController;

    /**
     * Constructs a new {@code DisplayTab} with the given database service.
     *
     * @param databaseService the service used by the tab's controller to perform display operations
     */
    public DisplayTab(DynamicDatabaseService databaseService)
    {
        setText("Display");
        setClosable(false);

        this.displayTabController = new DisplayTabController(databaseService);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DisplayData.fxml"));
            loader.setControllerFactory(param -> displayTabController);
            setContent(loader.load());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load " + getClass().getName() + ".fxml", e);
        }

        refreshTable();
    }

    /**
     * Refreshes the contents of the table to reflect the current state of the database.
     */
    public void refreshTable()
    {
        displayTabController.updateTable();
    }
}