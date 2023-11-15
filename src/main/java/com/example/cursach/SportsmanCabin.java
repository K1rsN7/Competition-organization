package com.example.cursach;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class SportsmanCabin {
    @FXML
    public ComboBox<String> competitionList; // Список соревнований в которых участвовал спортсмен
    @FXML
    public ComboBox<String> competitionListApply; // Список доступных соревнований
    String pathImagePlug = "plug_banner.png"; // Название файла с изображением заглушкой
    DB db = null; // База данных
    int idSportsman; // Идентификатор спортсмена
    @FXML
    private ImageView banner; // Баннер соревнования
    @FXML
    private TextFlow competitionDescription; // Описание соревнования
    @FXML
    private ListView<String> juryEvaluations; // Оценки жюри по критериям
    @FXML
    private Button personalCabinet; // Кнопка загрузки личного кабинета пользователя
    @FXML
    private Button exit; // Кнопка выхода


    public void setIdSportsman(int id) {
        /*
        Функция позволяет получить из другого класса идентификатор спортсмена
         */
        this.idSportsman = id;
    }

    @FXML
    void initialize() throws MalformedURLException, SQLException, ClassNotFoundException {
        db = new DB(); // Инициализируем базу данных
        loadImage(0); // Загружаем баннер на форму
    }

    @FXML
    public void loadCompetitions() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет загрузить названия соревнований в которых учавствовал спортсмен
         */
        competitionList.setItems(FXCollections.observableArrayList(db.getNameCompetition(this.idSportsman)));
        competitionListApply.setItems(FXCollections.observableArrayList(db.getCompetitionApply(this.idSportsman)));
    }

    @FXML
    void eventDataUpload() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить информацию по конкретному соревнованию и оценках жури
         */
        int idCompetition;
        String nameCompetitions = competitionList.getValue();
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
        nameCompetitions = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        idCompetition = db.getIdCompetition(nameCompetitions, holding_date);
        loadImage(idCompetition);
        competitionDescription.getChildren().add(db.getDescription(idCompetition));
        juryEvaluations.setItems(FXCollections.observableArrayList(db.getJuryEvaluations(idCompetition, idSportsman)));
    }

    void loadImage(int idCompetition) throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить баннер соревнования
         */
        // Проверка на существование идентификатора соревнования
        if (idCompetition > 0) {
            // Добавление фотографии спортсмена на форму
            File file = new File("src/main/resources/" + db.getPhotoCompetition(idCompetition));
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
        } else {
            File file = new File("src/main/resources/" + pathImagePlug);
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            banner.setImage(image);
        }
    }

    @FXML
    void loadPersonalCabinet() throws IOException, SQLException, ClassNotFoundException {
        /*
        Функция позволяет загрузить личный кабинет спортсмена
         */
        Stage currentStage = (Stage) personalCabinet.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sportsman-personal-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(currentStage.getTitle());
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        SportsmanPersonalCabinet controller = fxmlLoader.getController();
        controller.setIdSportsman(this.idSportsman);
        controller.loadInfo();
        currentStage.close();
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
    void applyCompetition() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет зарегистрировать спортсмена на новое мероприятие
         */
        if (competitionListApply.getValue() != null) {
            int idCompetition;
            String nameCompetitions = competitionListApply.getValue();
            String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
            nameCompetitions = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
            idCompetition = db.getIdCompetition(nameCompetitions, holding_date);
            db.addAthletesCompetition(idCompetition, idSportsman);
            loadCompetitions();
        }
    }
}