module TodoListApp {
    requires java.logging;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;

    opens edu.ntnu.idatt1002.k2g10.todolistapp.models to javafx.base;
    opens edu.ntnu.idatt1002.k2g10.todolistapp.controllers to javafx.fxml;

    exports edu.ntnu.idatt1002.k2g10.todolistapp;
}