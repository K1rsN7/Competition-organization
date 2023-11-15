package com.example.cursach;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class JuryCabin {
    DB db = null; // База данных
    @FXML
    private Button exit; // Кнопка выхода
    @FXML
    private ComboBox<String> idAthletes; // Список атлетов на данном соревнование
    @FXML
    private ComboBox<String> idCompetition; // Список соревнований
    @FXML
    // Список доступных члену жури критериев оценки для спортсмена на определенном соревнование
    private ComboBox<String> idCriteria;
    @FXML
    private ComboBox<String> idHoldingDate; // Список доступных дат данного соревнования
    @FXML
    private ImageView imageAthlete; // Фотография выбранного спортсмена
    @FXML
    private Spinner<Integer> points; // Количество баллов выставленных пользователем
    private int idJury; // Идентификатор члена журю

    public void setIdJury(int id) {
        /*
        Функция позволяет передать в переменную на моменте авторизации идентификатор члена жури
         */
        this.idJury = id;
        points.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 5));
    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException, MalformedURLException {
        db = new DB();
        loadImage(); // загружаем фото атлета
        idCompetition.setItems(FXCollections.observableArrayList(db.getNameCompetition())); // Заполняем соревнования
    }

    @FXML
    void dateUpload() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет очистить данные во всех полях выбора (кроме списка соревнований)
        и заполнить даты проведения выбранного соревнования
         */
        loadImage(); // загружаем фото атлета
        idAthletes.setItems(FXCollections.observableArrayList()); // очищаем список спортсменов
        idCriteria.setItems(FXCollections.observableArrayList()); // очищаем список критериев
        idHoldingDate.setItems(FXCollections.observableArrayList()); // очищаем список дат проведения
        idHoldingDate.setItems(FXCollections.observableArrayList(
                db.getHoldingDate(idCompetition.getValue()))); // загружаем данные о датах проведения
    }

    @FXML
    void athleteUpload() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет добавить список атлетов, которые участвуют в выбранном соревнование в конкретный день,
        очищает поля ввода списка атлетов и критериев оценивания
         */
        loadImage(); // загружаем фото атлета
        idCriteria.setItems(FXCollections.observableArrayList()); // очищаем список критериев
        idAthletes.setItems(FXCollections.observableArrayList()); // очищаем список спортсменов
        idAthletes.setItems(FXCollections.observableArrayList(db.getFioAthletes(
                idCompetition.getValue(), idHoldingDate.getValue()))); // загружаем данные о спортсменах
    }

    @FXML
    void criteriaUpload() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет заполнить список доступных критериев для выставления
        членом жури данному атлету
         */
        loadImage(); // загружаем фото атлета
        idCriteria.setItems(FXCollections.observableArrayList()); // очищаем список критериев
        idCriteria.setItems(FXCollections.observableArrayList(db.getAvailableCriteria(idCompetition.getValue(),
                idHoldingDate.getValue(), idAthletes.getValue(), this.idJury))); // загружаем данные о критериях оценивания
    }

    @FXML
    void addedPoints() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет внести в базу данных критерии оценивания члена жури
         */
        if (idCompetition.getValue().length() == 0 || idHoldingDate.getValue().length() == 0 ||
                idAthletes.getValue().length() == 0 || idCriteria.getValue().length() == 0) {
            windowMessenger("Не заполнены все параметры ввода до конца");
        } else {
            db.addPoints(idCompetition.getValue(), idHoldingDate.getValue(), idAthletes.getValue(), idCriteria.getValue(),
                    this.idJury, points.getValue()); // добавляем в таблицу базы данных информацию о баллах члена жури
            criteriaUpload(); // обновляем информацию о доступных критериях оценивания
            points.getValueFactory().setValue(5); // Выставляем значение по умолчанию после добавления
        }
    }

    void loadImage() throws SQLException, ClassNotFoundException, MalformedURLException {
        /*
        Функция позволяет добавить изображение атлета на форму
         */
        // Добавление фотографии спортсмена на форму
        String name_file = db.getPhotoAthlete(idAthletes.getValue());
        if (name_file.length() > 3) {
            File file = new File("src/main/resources/" + name_file);
            try {
                String urlImage = file.toURI().toURL().toString();
                Image image = new Image(urlImage);
                imageAthlete.setImage(image);
            }
            // В случае ошибки вставляем картинку заглушку
            catch (MalformedURLException ignored) {
                file = new File("src/main/resources/plug.png");
                String urlImage = file.toURI().toURL().toString();
                Image image = new Image(urlImage);
                imageAthlete.setImage(image);
            }
        } else {
            File file = new File("src/main/resources/plug.png");
            String urlImage = file.toURI().toURL().toString();
            Image image = new Image(urlImage);
            imageAthlete.setImage(image);
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
