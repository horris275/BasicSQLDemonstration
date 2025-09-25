package com.github.horris275.basicsqldemonstration.ui.tabs;

import com.github.horris275.basicsqldemonstration.sql.DatabaseService;
import com.github.horris275.basicsqldemonstration.ui.controllers.DeleteTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * A custom {@link Tab} representing the "Delete" tab in the user interface.
 *
 * <p>This tab allows users to delete database rows using a {@link DeleteTabController}.
 * The tab is non-closable and automatically loads the FXML layout.
 * If the FXML layout fails to load, a {@link RuntimeException} is thrown.</p>
 *
 * @author horris275
 * @version 22.09.2025
 */
public class DeleteTab extends Tab
{
    /**
     * Constructs a new {@code DeleteTab} with the given database service.
     *
     * @param databaseService the service used by the tab's controller to perform deletion operations
     */
    public DeleteTab(DatabaseService databaseService)
    {
        setText("Delete");
        setClosable(false);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DeleteData.fxml"));
            loader.setControllerFactory(param -> new DeleteTabController(databaseService));
            setContent(loader.load());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load " + getClass().getName() + ".fxml", e);
        }
    }
}