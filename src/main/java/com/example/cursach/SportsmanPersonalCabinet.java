package com.example.cursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class SportsmanPersonalCabinet {
    int idSportsman; // Идентификатор спортсмена
    DB db = null; // База данных
    @FXML
    private Label FIO; // ФИО спортсмена
    @FXML
    private TextArea biography; // Автобиография спортсмена
    @FXML
    private Label birth_country; // Родина
    @FXML
    private Label birth_date; // Дата рождения
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private ImageView image_athlete; // Фотография спортсмена
    @FXML
    private TextField path_image; // Имя файла с фотографией

    public void setIdSportsman(int id) {
        /*
        Функция позволяет получить идентификатор спортсмена из другого класса
         */
        this.idSportsman = id;
    }

    @FXML
    void initialize() {
        db = new DB(); // Инициализируем базу данных
    }

    void loadInfo() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить всю информацию на форму
         */
        FIO.setText("ФИО: " + db.getNameAthlete(idSportsman));
        birth_date.setText("Дата рождения: " + db.getBirthDate(idSportsman));
        birth_country.setText("Родина: " + db.getBirthCountry(idSportsman));
        path_image.setText(db.getImage(idSportsman));
        biography.setText(db.getBiography(idSportsman));
        File file = new File("src/main/resources/" + path_image.getText());
        try {
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            image_athlete.setImage(image);
        }
        // В случае ошибки вставляем картинку заглушку
        catch (MalformedURLException ignored) {
            file = new File("src/main/resources/plug_banner.png");
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            image_athlete.setImage(image);
        }
    }

    @FXML
    void exitScene() throws IOException, SQLException, ClassNotFoundException {
        /*
        Функция позволяет вернуться в кабинет спортсмена
         */
        Stage currentStage = (Stage) exit.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sportsman-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(currentStage.getTitle());
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        SportsmanCabin controller = fxmlLoader.getController();
        controller.setIdSportsman(this.idSportsman);
        controller.loadCompetitions();
        currentStage.close();
    }

    @FXML
    void saveChanges() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сохранить изменения в фотографии и автобиографии
         */
        db.saveChangesAthlete(idSportsman, path_image.getText(), biography.getText());
    }
}
