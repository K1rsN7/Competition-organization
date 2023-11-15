package com.example.cursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class UserSportsmanCabin {
    String pathImagePlug = "plug.png"; // Название файла с изображением заглушкой
    int idSportsman; // Идентификатор спортсмена
    DB db = null; // База данных
    @FXML
    private Label FIO; // ФИО спортсмена
    @FXML
    private TextFlow biography; // Биография спортсмена
    @FXML
    private Label birth_country; // Страна рождения спортсмена
    @FXML
    private Label birth_date; // Дата рождения спортсмена
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private ImageView image_athlete; // Фотография со спортсменом
    @FXML
    private StackPane stack;

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
        Функция позволяет загрузить на форму все данные
         */
        FIO.setText("ФИО: " + db.getNameAthlete(idSportsman));
        birth_date.setText("Дата рождения: " + db.getBirthDate(idSportsman));
        birth_country.setText("Родина: " + db.getBirthCountry(idSportsman));
        String path_image = db.getImage(idSportsman);

        // Загружаем фотографию
        File file = new File("src/main/resources/" + path_image);
        try {
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            image_athlete.setImage(image);
        }
        // В случае ошибки вставляем картинку заглушку
        catch (MalformedURLException ignored) {
            file = new File("src/main/resources/" + pathImagePlug);
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            image_athlete.setImage(image);
        }

        biography.getChildren().add(new Text(db.getBiography(idSportsman)));
    }

    @FXML
    void exitScene() throws IOException, SQLException, ClassNotFoundException {
        /*
        Функция позволяет вернуться в кабинет спортсмена
         */
        Stage currentStage = (Stage) exit.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-cabin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle(currentStage.getTitle());
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        UserCabin controller = fxmlLoader.getController();
        controller.loadCompetitions();
        currentStage.close();
    }
}
