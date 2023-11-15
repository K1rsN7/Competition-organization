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

public class UserCabin {
    String pathImagePlug = "plug_banner.png"; // Название файла с изображением заглушкой
    DB db = null; // База данных
    @FXML
    private ImageView banner; // Баннер чемпионата
    @FXML
    private TextArea description; // Описание чемпионата
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private ComboBox<String> listCompetitions; // Список соревнований
    @FXML
    private ListView<String> standings; // Турнирная таблица участников

    @FXML
    void initialize() throws MalformedURLException, SQLException, ClassNotFoundException {
        db = new DB(); // Инициализируем базу данных
        loadCompetitions(); // Загружаем список соревнований

        standings.setCellFactory(stringListView -> {
            /*
            Событие позволяет загрузить окно со всей информацией о спортсмене при нажатии на него в списке ПКМ
             */
            // Создадим контекстное меню
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem addItem = new MenuItem("Посмотреть информацию о спортсмене");

            // Событие при нажатии на пункт контекстного меню
            addItem.setOnAction(event -> {
                String item = cell.getItem(); // Запись из таблицы
                String fio;

                // Парсим ФИО спортсмена
                if (item.contains("место")) {
                    fio = item.substring(item.indexOf("место: ") + "место: ".length(), item.indexOf(";"));
                } else {
                    fio = item;
                }

                // Загружаем окно со спортсменом и загрузим туда данные
                Stage currentStage = (Stage) standings.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-sportsman-cabin.fxml"));
                Stage stage = new Stage();
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 600, 400);
                    stage.setTitle(currentStage.getTitle());
                    stage.setScene(scene);
                    stage.getIcons().add(new Image("iconka.png"));
                    stage.show();
                    stage.setResizable(false);
                    UserSportsmanCabin controller = fxmlLoader.getController();
                    controller.setIdSportsman(db.getIdSportsman(fio));
                    controller.loadInfo();
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
                currentStage.close();
            });

            // Добавим контекстное меню на турнирную таблицу
            contextMenu.getItems().addAll(addItem);
            cell.textProperty().bind(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell;
        });

    }

    void loadCompetitions() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить список соревнований
         */
        listCompetitions.setItems(FXCollections.observableArrayList(db.getCompetition()));
        File file = new File("src/main/resources/" + pathImagePlug);
        String urlImage = file.toURI().toURL().toString();
        Image image = new Image(urlImage);
        banner.setImage(image);
        loadBanner(null);
    }

    void loadBanner(String path_banner) throws MalformedURLException {
        /*
        Функция позволяет загрузить баннер изображения
         */
        if (path_banner == null) {
            File file = new File("src/main/resources/" + pathImagePlug);
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            banner.setImage(image);
        } else {
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

    @FXML
    void loadInfo() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет загрузить всю информацию по текущему соревнованию
         */
        // Получаем название соревнования и его дату проведения
        String nameCompetitions = listCompetitions.getValue();
        String title = nameCompetitions.substring(0, nameCompetitions.indexOf("|"));
        String holding_date = nameCompetitions.substring(nameCompetitions.indexOf("|") + 1);

        // Загружаем на форму описание соревнования и баннер соревнования
        description.setText(db.getDescriptionCompetition(title, holding_date));
        String path_banner = db.getPathBanner(title, holding_date);
        loadBanner(path_banner);
        // Проверяем сформирован ли итоговая турнирная таблица
        boolean check = db.checkGenerateRating(title, holding_date);
        if (check) {
            standings.setItems(FXCollections.observableArrayList(db.generateRating(title, holding_date)));
        } else {
            standings.setItems(FXCollections.observableArrayList(db.getAthleteList(title, holding_date)));
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

}
