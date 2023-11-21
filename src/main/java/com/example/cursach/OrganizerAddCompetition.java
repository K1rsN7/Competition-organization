package com.example.cursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class OrganizerAddCompetition {
    int idOrganizer; // Идентификатор организатора
    String name = ""; // Имя пользователя
    DataBase dataBase = null; // База данных
    String pathImagePlug = "plug_banner.png"; // Название файла с изображением заглушкой
    @FXML
    private ImageView banner; // Баннер соревнования
    @FXML
    private TextArea description; // Описание
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private DatePicker holding_date; // Дата проведения
    @FXML
    private TextField path_banner; // Имя файла с баннером
    @FXML
    private TextField title; // Название соревнования

    void setIdOrganizer(int id) {
            /*
            Функция позволяет получить идентификатор организатора из другого класса
             */
        this.idOrganizer = id;

    }

    void setName(String name_f) {
            /*
            Функция позволяет получить ФИО организатора из другого класса
             */
        this.name = name_f;
    }

    @FXML
    void initialize() {
        dataBase = new DataBase(); // Инициализируем базу данных
    }

    @FXML
    void addCompetition() throws SQLException, ClassNotFoundException, MalformedURLException {
            /*
            Функция позволяет добавить новое соревнование в базу данных
             */
        // Проверка на заполненность обязательных полей
        if (title.getText().length() < 3 || holding_date.getValue() == null) {
            dataBase.windowMessenger("Пресутсвуют незаполненные поля!");
        } else {
            dataBase.addNewCompetition(this.idOrganizer, title.getText(), holding_date.getValue().toString(), description.getText(), path_banner.getText());
            loadBanner(path_banner.getText());
            holding_date.setValue(null);
            dataBase.windowMessenger("Соревнование успешно добавлено");
            title.setText("");
            description.setText("");
            path_banner.setText("");
        }
    }

    @FXML
    void exit() throws IOException, SQLException, ClassNotFoundException {
            /*
            Функция позволяет загрузить кабинет организатора
             */
        Stage currentStage = (Stage) exit.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizer-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Личный кабинет организатора: " + this.name);
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        OrganizerCabin controller = fxmlLoader.getController();
        controller.setIdOrganizer(this.idOrganizer);
        controller.loadCompetitions();
        currentStage.close();
    }

    void loadBanner(String path_banner) throws MalformedURLException {
            /*
            Функция позволяет загрузить баннер изображения
             */
        File file = new File("src/main/resources/" + path_banner);
        try {
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            banner.setImage(image);
        }
        // В случае ошибки вставляем картинку заглушку
        catch (MalformedURLException ignored) {
            file = new File("src/main/resources/" + pathImagePlug);
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            banner.setImage(image);
        }
    }


}
