module org.example.db_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens org.example.db_javafx to javafx.fxml;
    exports org.example.db_javafx;
    exports org.example.db_javafx.controllers;
    opens org.example.db_javafx.controllers to javafx.fxml;
    exports org.example.db_javafx.models;
    opens org.example.db_javafx.models to javafx.fxml;
}