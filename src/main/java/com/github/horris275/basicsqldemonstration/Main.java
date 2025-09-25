package com.github.horris275.basicsqldemonstration;

import com.github.horris275.basicsqldemonstration.sql.DatabaseService;
import com.github.horris275.basicsqldemonstration.sql.SQLManager;
import com.github.horris275.basicsqldemonstration.ui.tabs.DeleteTab;
import com.github.horris275.basicsqldemonstration.ui.tabs.DisplayTab;
import com.github.horris275.basicsqldemonstration.ui.tabs.InsertTab;
import com.github.horris275.basicsqldemonstration.ui.tabs.ModifyTab;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.awt.*;

/**
 * The main entry class for the JavaFX application.
 *
 * <p>The class is responsible for initialising, creating, and launching the user interface.
 * The application provides basic CRUD operations on a SQL database, where each element
 * of CRUD is represented within each tab.</p>
 *
 * <p>The default screen size is set to 75% of the resolution of the user's monitor.</p>
 *
 * <p>Database connection parameters are hardcoded for demonstration purposes.
 * Additional considerations and validation would be required to make it production ready.
 * Finally, the table would be moved to a dynamic system to retrieve column names.</p>
 *
 * @author horris275
 * @version 17.09.2025
 */
public class Main extends Application
{
    /**
     * Initialises and shows the primary stage of the JavaFX application.
     *
     * <p>The method is responsible for setting up the database service, creating the tab
     * pane containing all CRUD tabs, setting the window size, and finally displaying the scene</p>
     *
     * @param stage the primary stage provided by the JavaFX runtime
     */
    @Override
    public void start(Stage stage)
    {
        final DatabaseService databaseService = createDatabaseService();
        final TabPane tabPane = createTabPane(databaseService);

        final Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        final int width = (int) (resolution.width * 0.75);
        final int height = (int) (resolution.height * 0.75);

        final Scene scene = new Scene(tabPane, width, height);

        stage.setTitle("Basic SQL Demonstration with JavaFX UI");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates and configures the main {@link TabPane} containing all CRUD tabs.
     *
     * <p>As current, it provides Display, Insert, Modify, and Delete for CRUD operations.</p>
     *
     * @param databaseService the {@link DatabaseService} used by each tab controller
     * @return                a configured {@link TabPane} containing all tabs
     */
    private TabPane createTabPane(DatabaseService databaseService)
    {
        TabPane tabPane = new TabPane();

        DeleteTab deleteTab = new DeleteTab(databaseService);
        DisplayTab displayTab = new DisplayTab(databaseService);
        InsertTab insertTab = new InsertTab(databaseService);
        ModifyTab modifyTab = new ModifyTab(databaseService);

        ObservableList<Tab> tabs = tabPane.getTabs();

        tabs.add(displayTab);
        tabs.add(insertTab);
        tabs.add(modifyTab);
        tabs.add(deleteTab);

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == displayTab)
            {
                displayTab.refreshTable();
            }
        });

        return tabPane;
    }

    /**
     * Creates the {@link DatabaseService} instance for interacting with the database.
     *
     * <p>Currently, the values are hardcoded and connects to a MariaDB database.
     * This is for demonstration purposes.</p>
     *
     * @return a {@link DatabaseService} implementation connected to the database
     */
    private DatabaseService createDatabaseService()
    {
        final String host = "localhost";
        final String port = "3306";
        final String database = "test";
        final String table = "database_example";
        final String user = "root";
        final String password = "pie123";

        return new SQLManager(host, port, database, table, user, password);
    }

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args the arguments passed to the program (unused)
     */
    public static void main(String[] args)
    {
        launch();
    }
}