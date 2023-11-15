package com.example.cursach;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AdminAddGradingcriteria {
    DB db = null; // Переменна с базой данных
    @FXML
    private ComboBox<String> list; // Список критериев оценивания
    @FXML
    private TextField name; // Название критерия

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        db = new DB(); // Инициализируем базу данных
        update(); // Обновляем список критериев оценивания
    }

    @FXML
    void update() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет обновить данные на форме
         */
        list.setItems(FXCollections.observableArrayList(db.getCriteria()));
        name.setText(list.getValue());
        list.setValue(null);
    }

    @FXML
    void add() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить новый критерий оценивания
         */
        db.addNewGradingCriteria(name.getText());
        update();
    }

    @FXML
    void edit() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет изменить название критерия оценивания
         */
        db.editGradingCriteria(list.getValue(), name.getText());
        update();
    }

    @FXML
    void drop() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить критерий оценивания
         */
        db.dropGradingCriteria(list.getValue());
        update();
    }

    @FXML
    void exit() throws IOException {
        /*
        Функция позволяет вернуться в кабинет администратора
         */
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Личный кабинет администратора");
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        Stage currentStage = (Stage) name.getScene().getWindow();
        currentStage.close();
    }
}
