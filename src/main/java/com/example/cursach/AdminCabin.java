package com.example.cursach;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class AdminCabin {
    DataBase dataBase = null; // База данных
    @FXML
    private TextArea biography_input; // Поле ввода биографии спортсмена
    @FXML
    private TextField birth_country_input; // Поле ввода страны в которой родился спортсмен
    @FXML
    private DatePicker birth_date_input; // Поле ввода даты рождения спортсмена
    @FXML
    private TextField fio_input; // Поле ввода ФИО пользователя
    @FXML
    private TextField image_input; // Поле ввода название файла с изображением спортсмена
    @FXML
    private TextField login_input; // Поле ввода с логином пользователя
    @FXML
    private TextField password_input; // Поле ввода с паролем пользователя
    @FXML
    private Button add_user; // Кнопка для добавления пользователя
    @FXML
    private ComboBox<String> roles_menu; // Поле ввода с выбором доступных ролей
    @FXML
    private Button exit; // Кнопка для выхода

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        dataBase = new DataBase();  // Инициализируем базу данных
        roles_menu.setItems(FXCollections.observableArrayList(dataBase.getNameRoles()));
    }

    public void choiceOfRole() {
        /*
        Функция позволяет сделать доступными поля ввода в зависимости от роли нового пользователя
         */
        if (Objects.equals(roles_menu.getValue(), "Спортсмен")) {
            birth_date_input.setDisable(false);
            birth_country_input.setDisable(false);
            image_input.setDisable(false);
            biography_input.setDisable(false);
        } else {
            birth_date_input.setDisable(true);
            birth_country_input.setDisable(true);
            image_input.setDisable(true);
            biography_input.setDisable(true);
        }
        add_user.setText("Добавить: " + roles_menu.getValue());
    }

    public void addUser() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет проверить заполненность полей и сделать запрос на добавление нового пользователя
         */
        // Проверка на обязательные поля
        if (login_input.getText().length() == 0 || password_input.getText().length() == 0 ||
                fio_input.getText().length() == 0) {
            windowMessengerError("Присутствуют незаполненные обязательные поля!");
        } else {
            // Проверка на уникальность логина
            if (dataBase.isDuplicateLogin(login_input.getText())) {
                windowMessengerError("Такой логин уже используется другим пользователем!");
            } else {
                // Проверка на добавление спортсмена
                if (Objects.equals(roles_menu.getValue(), "Спортсмен")) {
                    // Проверка на заполнение обязательных полей для спортсмена
                    if (birth_country_input.getText().length() == 0 || birth_date_input.getValue() == null) {
                        windowMessengerError("Пресутсвуют незаполненные обязательные поля для спортсмена!");
                    } else {
                        // Проверка возраста спортсмена, он должен быть выше 7 лет
                        if (Period.between(birth_date_input.getValue(), LocalDate.now()).getYears() < 7) {
                            windowMessengerError("Спортсмен/ка слишком мал/а");
                        } else {
                            // Добавляем нового спортсмена
                            dataBase.addNewAthletes(roles_menu.getValue(), login_input.getText(), password_input.getText(),
                                    fio_input.getText(), birth_date_input.getValue().toString(), birth_country_input.getText(),
                                    image_input.getText(), biography_input.getText());
                            clearCells();
                        }
                    }
                } else {
                    // Добавляем нового пользователя
                    dataBase.addNewUser(roles_menu.getValue(), login_input.getText(), password_input.getText(),
                            fio_input.getText());
                    clearCells();
                }
            }
        }
    }

    public void clearCells() {
        /*
        Функция позволяет очистить все поля ввода
         */
        biography_input.setText("");
        birth_country_input.setText("");
        birth_date_input.setValue(null);
        fio_input.setText("");
        image_input.setText("");
        login_input.setText("");
        password_input.setText("");
        add_user.setText("");
        roles_menu.setValue("");
        add_user.setText("Добавить");
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
    void loadGradingCriteria() throws IOException {
        /*
        Функция позволяет загрузить окно для работы с критериями оценивания
         */
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin-edit-gradingcriteria.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Изменение критериев оценивания");
        stage.setScene(scene);
        stage.getIcons().add(new Image("iconka.png"));
        stage.show();
        stage.setResizable(false);
        exit();
    }

    @FXML
    void editUser() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет совершить изменения у пользователей
         */
        // Проверка на обязательные поля
        if (login_input.getText().length() == 0 || password_input.getText().length() == 0 ||
                fio_input.getText().length() == 0) {
            windowMessengerError("Присутствуют незаполненные обязательные поля!");
        } else {
            int idRole = dataBase.getIdRole(login_input.getText(), password_input.getText());
            // Проверка на изменения у администратора
            if (idRole != 5) {
                if (Objects.equals(roles_menu.getValue(), "Спортсмен")) {
                    // Если у спортсмена дата и родина не заполнены, меняем только ФИО
                    if (birth_country_input.getText().length() == 0 || birth_date_input.getValue() == null) {
                        dataBase.editUser(idRole, login_input.getText(), password_input.getText(), fio_input.getText());
                    } else {
                        // Проверяем на возраст
                        if (Period.between(birth_date_input.getValue(), LocalDate.now()).getYears() < 7) {
                            windowMessengerError("Спортсмен/ка слишком мал/а!");
                            dataBase.editUser(idRole, login_input.getText(), password_input.getText(), fio_input.getText());
                        } else {
                            // Выполняем изменения у спортсмена
                            dataBase.editUser(login_input.getText(), password_input.getText(), fio_input.getText(),
                                    birth_country_input.getText(), birth_date_input.getValue().toString());
                            clearCells();
                        }
                    }
                } else {
                    // Выполняем изменения
                    dataBase.editUser(idRole, login_input.getText(), password_input.getText(), fio_input.getText());
                    clearCells();
                }
            } else {
                windowMessengerError("У администратора нельзя менять данные!");
            }
        }

    }

    @FXML
    void deleteUser() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить людей из системы, которые не взаимодействовали с соревнованиями
         */
        // Проверка на обязательные поля
        if (login_input.getText().length() != 0 && password_input.getText().length() != 0) {
            dataBase.deleteUser(login_input.getText(), password_input.getText());
        } else {
            windowMessengerError("Не заполнены поля логин и пароль!");
        }
        clearCells();
    }

    void windowMessengerError(String messenger) {
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
