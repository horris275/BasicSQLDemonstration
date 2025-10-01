module com.github.horris275.basicsqldemonstration {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens com.github.horris275.basicsqldemonstration to javafx.fxml;
    opens com.github.horris275.basicsqldemonstration.sql to javafx.base;
    opens com.github.horris275.basicsqldemonstration.ui.controllers to javafx.fxml;

    exports com.github.horris275.basicsqldemonstration;
    opens com.github.horris275.basicsqldemonstration.sql.interfaces to javafx.base;
}