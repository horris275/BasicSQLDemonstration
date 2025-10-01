package com.github.horris275.basicsqldemonstration.ui.tabs;

import com.github.horris275.basicsqldemonstration.sql.interfaces.DatabaseService;
import com.github.horris275.basicsqldemonstration.sql.interfaces.DynamicDatabaseService;
import com.github.horris275.basicsqldemonstration.ui.controllers.ModifyTabController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

/**
 * A custom {@link Tab} representing the "Modify" tab in the user interface.
 *
 * <p>This tab allows users to modify existing database rows using a {@link ModifyTabController}.
 * The tab is non-closable and automatically loads the FXML layout.
 * If the FXML layout fails to load, a {@link RuntimeException} is thrown.</p>
 *
 * @author horris275
 * @version 23.09.2025
 */
public class ModifyTab extends Tab
{

    /**
     * Constructs a new {@code ModifyTab} with the given database service.
     *
     * @param databaseService the service used by the tab's controller to perform modification operations
     */
    public ModifyTab(DynamicDatabaseService databaseService)
    {
        setText("Modify");
        setClosable(false);

        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifyData.fxml"));
            loader.setControllerFactory(param -> new ModifyTabController(databaseService));
            setContent(loader.load());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load " + getClass().getName() + ".fxml", e);
        }
    }
}