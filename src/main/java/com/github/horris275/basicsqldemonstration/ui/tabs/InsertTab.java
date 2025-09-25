package com.github.horris275.basicsqldemonstration.ui.tabs;

import com.github.horris275.basicsqldemonstration.sql.DatabaseService;
import com.github.horris275.basicsqldemonstration.ui.controllers.InsertTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * A custom {@link Tab} representing the "Insert" tab in the user interface.
 *
 * <p>This tab allows users to insert new database rows using a {@link InsertTabController}.
 * The tab is non-closable and automatically loads the FXML layout.
 * If the FXML layout fails to load, a {@link RuntimeException} is thrown.</p>
 *
 * @author horris275
 * @version 22.09.2025
 */
public class InsertTab extends Tab
{
    /**
     * Constructs a new {@code InsertTab} with the given database service.
     *
     * @param databaseService the service used by the tab's controller to perform insertion operations
     */
    public InsertTab(DatabaseService databaseService)
    {
        setText("Insert");
        setClosable(false);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InsertData.fxml"));
            loader.setControllerFactory(param -> new InsertTabController(databaseService));
            setContent(loader.load());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load " + getClass().getName() + ".fxml", e);
        }
    }
}