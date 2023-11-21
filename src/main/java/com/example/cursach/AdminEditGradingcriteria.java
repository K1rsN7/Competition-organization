package com.example.cursach;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class AdminEditGradingcriteria {
    DataBase dataBase = null; // Переменна с базой данных
    @FXML
    private ComboBox<String> list; // Список критериев оценивания
    @FXML
    private TextField name; // Название критерия

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        dataBase = new DataBase(); // Инициализируем базу данных
        update(); // Обновляем список критериев оценивания
    }

    @FXML
    void update() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет обновить данные на форме
         */
        list.setItems(FXCollections.observableArrayList(dataBase.getCriteria()));
        name.setText(list.getValue());
        list.setValue(null);
    }

    @FXML
    void add() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить новый критерий оценивания
         */
        if (name.getText() != null) {
            dataBase.addNewGradingCriteria(name.getText());
        } else {
            windowMessengerError("Заполните поле названия!");
        }
        update();
    }

    @FXML
    void edit() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет изменить название критерия оценивания
         */
        if (list.getValue() != null && name.getText() != null) {
            dataBase.editGradingCriteria(list.getValue(), name.getText());
        } else {
            windowMessengerError("Выберите критерий оценивания и заполните поле названия!");
        }
        update();
    }

    @FXML
    void drop() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить критерий оценивания
         */
        if (list.getValue() != null) {
            dataBase.dropGradingCriteria(list.getValue());
        } else {
            windowMessengerError("Выберите критерий оценивания!");
        }
        update();
    }

    @FXML
    void exit() throws IOException {
        /*
        Функция позволяет вернуться в кабинет администратора
         */
        Stage currentStage = (Stage) list.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Личный кабинет администратора");
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        currentStage.close();
    }

    void windowMessengerError(String messenger) {
        /*
        Функция позволяет вывести окно с сообщением ошибки
         */
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!!!");
        alert.setHeaderText(String.format(messenger));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("error.png"));
        alert.showAndWait();
    }
}
