package com.example.cursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Authorization {
    String name; // ФИО пользователя
    @FXML
    private Button inlet; // Кнопка для попытки авторизации
    @FXML
    private Button idGuest; // Кнопка для входа как гостя
    @FXML
    private CheckBox isShowPassword; // Флажок для показа пароля
    @FXML
    private TextField login; // Поле ввода логина
    @FXML
    private PasswordField password; // Поле ввода пароля при отключённом флажке
    @FXML
    private TextField publicPassword; // Поле ввода пароля при включённом флажке

    @FXML
    void changeVisibility() {
        /*
        Функция для отображения введённого пароля пользователем
         */

        // Если флажок включён, то отображаем вводимый пользователем текст
        if (isShowPassword.isSelected()) {
            publicPassword.setText(password.getText());
            publicPassword.setVisible(true);
            password.setVisible(false);
            return;
        }

        password.setText(publicPassword.getText());
        password.setVisible(true);
        publicPassword.setVisible(false);
    }

    @FXML
    void initialize() {
        DB db = new DB(); // Инициализируем базу данных
        publicPassword.setVisible(false); // Скрываем пароль

        // Добавляем событие на кнопку для попытки авторизации
        inlet.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                // Синхронизируем текст
                if (isShowPassword.isSelected()) {
                    password.setText(publicPassword.getText());
                }

                // Проверяем на наличие пустоты
                if (!login.getText().trim().equals("") & !password.getText().trim().equals("")) {
                    int a = db.getIdRole(login.getText(), password.getText()); // Получаем идентификатор роди

                    // Если это пользователь, осуществляем вход от пользователя
                    if (a == 1) {
                        name = db.getName(login.getText(), password.getText(), 1);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет пользователя: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        System.out.println("LOG: Зашёл пользователь " + name);
                    }

                    // Если это спортсмен, осуществляем вход от спортсмена
                    else if (a == 2) {
                        name = db.getName(login.getText(), password.getText(), 2);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sportsman-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет спортсмена: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        SportsmanCabin controller = fxmlLoader.getController();
                        controller.setIdSportsman(db.getIdSportsman(name));
                        controller.loadCompetitions();
                        System.out.println("LOG: Зашёл спортсмен " + name);
                    }

                    // Если это организатор, осуществляем вход от организатора
                    else if (a == 3) {
                        name = db.getName(login.getText(), password.getText(), 3);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizer-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет организатора: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        OrganizerCabin controller = fxmlLoader.getController();
                        controller.setIdOrganizer(db.getIdOrganizer(name));
                        controller.loadCompetitions();
                        System.out.println("LOG: Зашёл организатор " + name);
                    }

                    // Если это член жури, осуществляем вход от члена жури
                    else if (a == 4) {
                        name = db.getName(login.getText(), password.getText(), 4);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("jury-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет члена жури: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        JuryCabin controller = fxmlLoader.getController();
                        controller.setIdJury(db.getIdJury(name));
                        System.out.println("LOG: Зашёл член жури " + name);
                    }

                    // Если это администратор, осуществляем вход от администратора
                    else if (a == 5) {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет администратора");
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        System.out.println("LOG: Зашёл администратор");
                    }
                    // Иначе выводим сообщение об ошибке
                    else {
                        windowMessenger("Такого пользователя не существует");
                    }

                    // Очищаем поля ввода
                    login.setText("");
                    password.setText("");
                    publicPassword.setText("");

                } else {
                    windowMessenger("Пользователь не заполнил поля");
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Добавляем событие на кнопку входа под гостем
        idGuest.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-cabin.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage.setTitle("Гость");
                stage.setScene(scene);
                stage.getIcons().add(new Image("iconka.png"));
                stage.show();
                stage.setResizable(false);
                System.out.println("LOG: Зашёл гость");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    void windowMessenger(String messenger) {
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
