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
    DataBase dataBase = null; // База данных
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
    @FXML
    private ComboBox<String> listJury; // Список членов жюри на соревнование
    @FXML
    private ChoiceBox<String> listJuryApply;// Список доступных членов жюри

    void setIdOrganizer(int id) {
        /*
        Функция позволяет получить идентификатор организатора из другого класса
         */
        this.idOrganizer = id;
    }

    @FXML
    void initialize() {
        dataBase = new DataBase(); // Инициализируем базу данных
    }

    void loadCompetitions() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить список соревнований
         */
        listCompetitions.setItems(FXCollections.observableArrayList(dataBase.getCompetition(this.idOrganizer)));
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
        path_banner.setText(dataBase.getPathBanner(title, holding_date));
        description.setText(dataBase.getDescriptionCompetition(title, holding_date));
        loadBanner(path_banner.getText());
        listJury.setItems(FXCollections.observableArrayList(dataBase.getListJury(title, holding_date)));
        listJuryApply.setItems(FXCollections.observableArrayList(dataBase.getListJuryApply(title, holding_date)));
        // Проверяем сформирован ли итоговая турнирная таблица
        boolean check = dataBase.checkGenerateRating(title, holding_date);
        if (check) {
            standings.setItems(FXCollections.observableArrayList(dataBase.generateRating(title, holding_date)));
        } else {
            standings.setItems(FXCollections.observableArrayList(dataBase.getAthleteList(title, holding_date)));
        }
    }

    @FXML
    void saveInfo() throws MalformedURLException, SQLException, ClassNotFoundException {
        /*
        Функция позволяет сохранить изменения о соревнование
         */
        if (listCompetitions.getValue() != null) {
            String nameCompetitions = listCompetitions.getValue();
            String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
            String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
            dataBase.saveInfoCompetition(title, holding_date, description.getText(), path_banner.getText());
            loadInfo();
        } else {
            dataBase.windowMessengerError("Выберите сначала соревнование!");
        }

    }

    @FXML
    void generateRating() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сформировать итоговую турнирную таблицу
         */
        if (!standings.getItems().isEmpty()) {
            String nameCompetitions = listCompetitions.getValue();
            String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
            String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
            boolean flag = dataBase.checkJuryPoints(title, holding_date);
            if (flag) {
                standings.setItems(FXCollections.observableArrayList(dataBase.generateRating(title, holding_date)));
                dataBase.windowMessenger("Рейтинг успешно сформирован");
            } else {
                dataBase.windowMessengerError("Не все члены жюри выставили баллы!");
            }
        } else {
            dataBase.windowMessengerError("Выберите сначала соревнование!");
        }
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
        stage.setTitle("Добавление нового соревнования");
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        OrganizerAddCompetition controller = fxmlLoader.getController();
        controller.setIdOrganizer(this.idOrganizer);
        controller.setName(currentStage.getTitle().substring(currentStage.getTitle().indexOf(":") + 2));
        currentStage.close();
    }

    @FXML
    void dropCompetition() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет отменить соревнование
         */
        if (standings.getItems().size() > 0) {
            String nameCompetitions = listCompetitions.getValue();
            String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
            String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
            dataBase.dropCompetition(title, holding_date);
            loadInfo();
        } else {
            dataBase.windowMessengerError("Выберите сначала соревнование!");
        }

    }

    @FXML
    void applyJury() {
        /*
        Функция позволяет зарегистрировать члена жюри на соревнование
         */
        try {
            if (listJuryApply.getValue() != null) {
                String nameCompetitions = listCompetitions.getValue();
                String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
                String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);
                dataBase.addJuryFromCompetition(title, holding_date, listJuryApply.getValue());
                listJury.setItems(FXCollections.observableArrayList());
                listJuryApply.setItems(FXCollections.observableArrayList());
                listJury.setItems(FXCollections.observableArrayList(dataBase.getListJury(title, holding_date)));
                listJuryApply.setItems(FXCollections.observableArrayList(dataBase.getListJuryApply(title, holding_date)));
            }
        } catch (SQLException | ClassNotFoundException e) {

        }

    }


}
