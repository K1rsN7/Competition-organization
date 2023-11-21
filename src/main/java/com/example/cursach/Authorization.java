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
        DataBase dataBase = new DataBase(); // Инициализируем базу данных
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
                    int idRole = dataBase.getIdRole(login.getText(), password.getText()); // Получаем идентификатор роди

                    // Если это пользователь, осуществляем вход от пользователя
                    if (idRole==0){
                        dataBase.windowMessengerError("Введён неверный логин или пароль");
                    }
                    // Если это посетитель, осуществляем вход от посетителя
                    else if (idRole == 1) {
                        name = dataBase.getName(login.getText(), password.getText(), 1);
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
                    else if (idRole == 2) {
                        name = dataBase.getName(login.getText(), password.getText(), 2);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sportsman-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Кабинет соревнований спортсмена: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        SportsmanCabin controller = fxmlLoader.getController();
                        controller.setIdSportsman(dataBase.getIdSportsman(name));
                        controller.loadCompetitions();
                        System.out.println("LOG: Зашёл спортсмен " + name);
                    }

                    // Если это организатор, осуществляем вход от организатора
                    else if (idRole == 3) {
                        name = dataBase.getName(login.getText(), password.getText(), 3);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizer-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет организатора: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        OrganizerCabin controller = fxmlLoader.getController();
                        controller.setIdOrganizer(dataBase.getIdOrganizer(name));
                        controller.loadCompetitions();
                        System.out.println("LOG: Зашёл организатор " + name);
                    }

                    // Если это член жури, осуществляем вход от члена жури
                    else if (idRole == 4) {
                        name = dataBase.getName(login.getText(), password.getText(), 4);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("jury-cabin.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                        stage.setTitle("Личный кабинет члена жюри: " + name);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("iconka.png"));
                        stage.show();
                        stage.setResizable(false);
                        JuryCabin controller = fxmlLoader.getController();
                        controller.setIdJury(dataBase.getIdJury(name));
                        controller.loadCompetition();
                        System.out.println("LOG: Зашёл член жюри " + name);
                    }

                    // Если это администратор, осуществляем вход от администратора
                    else if (idRole == 5) {
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
                        dataBase.windowMessenger("Такого пользователя не существует");
                    }

                    // Очищаем поля ввода
                    login.setText("");
                    password.setText("");
                    publicPassword.setText("");

                } else {
                   dataBase.windowMessengerError("Не заполнены все поля");
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
                stage.setTitle("Личный кабинет пользователя");
                stage.setScene(scene);
                stage.getIcons().add(new Image("iconka.png"));
                stage.show();
                stage.setResizable(false);
                System.out.println("LOG: Зашёл неавторизованный пользователь");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
