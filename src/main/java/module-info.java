module TodoListApp {
    requires java.logging;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.validation;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;

    opens edu.ntnu.idatt1002.k2g10.todolistapp.models;
    opens edu.ntnu.idatt1002.k2g10.todolistapp.controllers;
    opens edu.ntnu.idatt1002.k2g10.todolistapp.utils.crypto;

    exports edu.ntnu.idatt1002.k2g10.todolistapp;
}