package com.example.cursach;

import javafx.collections.FXCollections;
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

public class OrganizerCabin {
    @FXML
    public TextArea description; // Описание соревнования
    int idOrganizer; // Идентификатор организатора
    String pathImagePlug = "plug_banner.png"; // Название файла с изображением заглушкой
    DB db = null; // База данных
    @FXML
    private Button addCompetition; // Кнопка добавления нового соревнования
    @FXML
    private ImageView banner; // Баннер
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private ComboBox<String> listCompetitions; // Список соревнований
    @FXML
    private TextField path_banner; // Название файла с баннером
    @FXML
    private ListView<String> standings; // Турнирная таблица

    void setIdOrganizer(int id) {
        /*
        Функция позволяет получить идентификатор организатора из другого класса
         */
        this.idOrganizer = id;
    }

    @FXML
    void initialize() {
        db = new DB(); // Инициализируем базу данных
    }

    void loadCompetitions() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить список соревнований
         */
        listCompetitions.setItems(FXCollections.observableArrayList(db.getCompetition(this.idOrganizer)));
        loadBanner("");
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

    @FXML
    void loadInfo() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить информацию о конкретном соревновании
         */
        String nameCompetitions = listCompetitions.getValue();
        String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
        path_banner.setText(db.getPathBanner(title, holding_date));
        description.setText(db.getDescriptionCompetition(title, holding_date));
        loadBanner(path_banner.getText());

        // Проверяем сформирован ли итоговая турнирная таблица
        boolean check = db.checkGenerateRating(title, holding_date);
        if (check) {
            standings.setItems(FXCollections.observableArrayList(db.generateRating(title, holding_date)));
        } else {
            standings.setItems(FXCollections.observableArrayList(db.getAthleteList(title, holding_date)));
        }
    }

    @FXML
    void saveInfo() throws MalformedURLException, SQLException, ClassNotFoundException {
        /*
        Функция позволяет сохранить изменения о соревнование
         */
        String nameCompetitions = listCompetitions.getValue();
        String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
        db.saveInfoCompetition(title, holding_date, description.getText(), path_banner.getText());
        loadInfo();
    }

    @FXML
    void generateRating() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сформировать итоговую турнирную таблицу
         */
        String nameCompetitions = listCompetitions.getValue();
        String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
        standings.setItems(FXCollections.observableArrayList(db.generateRating(title, holding_date)));
    }

    @FXML
    void exit() {
        /*
        Функция позволяет вернуться на окно авторизации
         */
        Stage currentStage = (Stage) exit.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void addCompetition() throws IOException {
        /*
        Функция позволяет загрузить новую форму добавления нового соревнования
         */
        Stage currentStage = (Stage) addCompetition.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("organizer-add-competition.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(currentStage.getTitle());
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        OrganizerAddCompetition controller = fxmlLoader.getController();
        controller.setIdOrganizer(this.idOrganizer);
        currentStage.close();
    }

    @FXML
    void dropCompetition() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет отменить соревнование
         */
        String nameCompetitions = listCompetitions.getValue();
        String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
        db.dropCompetition(title, holding_date);
        loadInfo();
    }
}
