package com.example.cursach;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    // TODO: все ошибки отображать в окне
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DB_NAME = "cursach";
    private final String LOGIN = "root";
    private final String PASS = "root";
    private Connection dbConn = null;

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        /*
        Функция позволяет совершить подключение к базе данных
        Output:
            Функция возвращает подключение
         */
        String connStr = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + "?characterEncoding=UTF8";
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(connStr, LOGIN, PASS);
        return dbConn;
    }

    public int getIdRole(String login, String password) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить идентификатор роли вошедшего пользователя
        Input:
            login - логин пользователя
            password - пароль пользователя
         Output:
            Функция возвращает идентификатор роли
         */
        int idRole = 0; // идентификатор роли

        // Получаем идентификатор роли
        String sql = "SELECT role_idRole FROM users where login=? and password=?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            idRole = res.getInt("role_idRole");
        }
        return idRole;
    }

    public String getName(String login, String password, int idRole) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить ФИО вошедшего пользователя
        Input:
            login - логин пользователя
            password - пароль пользователя
            idRole - идентификатор роли пользователя
        Output:
            Функция возвращает ФИО пользователя
         */
        int idUsers = getIdUsers(login, password); // получаем идентификатор пользователя
        String sql;
        String name = ""; // ФИО пользователя

        // В зависимости от идентификатора роли получаем ФИО пользователя из таблицы
        if (idRole == 1) {
            sql = "SELECT FIO FROM visitor WHERE Users_idUsers = " + idUsers;
        } else if (idRole == 2) {
            sql = "SELECT FIO FROM athletes WHERE Users_idUsers = " + idUsers;
        } else if (idRole == 3) {
            sql = "SELECT FIO FROM organizer WHERE Users_idUsers = " + idUsers;
        } else {
            sql = "SELECT FIO FROM jury WHERE Users_idUsers = " + idUsers;
        }
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            name = resultSet.getString("FIO");
        }
        return name;
    }

    public ArrayList<String> getNameRoles() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список всех возможных ролей
        Output:
            Функция возвращает список всех ролей в формате ArrayList
         */
        String sql = "SELECT name_role FROM role GROUP BY name_role";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> arrayList = new ArrayList<>();
        while (resultSet.next()) {
            arrayList.add(resultSet.getString("name_role"));
        }
        return arrayList;
    }

    public boolean isDuplicateLogin(String login) throws SQLException, ClassNotFoundException {
        /*
        Функция делает запрос и проверяет на наличие такого логина в базе данных
        Input:
            login - логин пользователя
        Output:
            Функция возвращает бинарный ответ на наличие дубликатов
         */
        int count = 0;
        String sql = "SELECT count(*) as count FROM users WHERE login = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) count = resultSet.getInt("count");
        return count > 0;
    }

    public void insertUsers(String login, String password, int idRole) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить нового пользователя
        Input:
            login - логин пользователя
            password - пароль пользователя
            idRole - идентификатор роли
         */
        String sql = "INSERT INTO users (login, password, role_idRole) VALUES (?, ?, ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.setInt(3, idRole);
        statement.executeUpdate();
    }

    public void addNewUser(String role, String login, String password, String fio) throws SQLException, ClassNotFoundException {
        /*
        Функция автоматизирует добавление пользователя в таблицу users и jury/organizer
        в случае добавления члена жури/организатора
        Input:
            role - название роли пользователя
            login - логин пользователя
            password - пароль пользователя
            fio - ФИО пользователя
         */
        int idRole = getIdRole(role); // получаем идентификатор роли
        CallableStatement statement = getDbConnection().prepareCall("call addNewUser(?, ?, ?, ?)");
        statement.setInt(1, idRole);
        statement.setString(2, login);
        statement.setString(3, password);
        statement.setString(4, fio);
        statement.executeUpdate();
    }

    public int getIdRole(String nameRole) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет по названию роле получить идентификатор роли
        Input:
            role - название роли
        Output:
            idRole - идентификатор роли
         */
        int idRole = 0;
        String sql = "SELECT idRole FROM role WHERE name_role = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, nameRole);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idRole = resultSet.getInt("idRole");
        return idRole;
    }

    public void addNewAthletes(String role, String login, String password, String fio,
                               String birth_date, String birth_country, String image, String biography) throws SQLException, ClassNotFoundException {
        /*
        Функция автоматизирует добавление пользователя в таблицу users и athletes
        Input:
            role - название роли пользователя
            login - логин пользователя
            password - пароль пользователя
            fio - ФИО спортсмена
            birth_date - дата рождения спортсмена
            birth_country - родина
            image - название файла с фотографией спортсмена
            biography - биография спортсмена
         */
        int idRole = getIdRole(role); // получаем идентификатор роли
        int idUser; // идентификатор пользователя

        // Проверка на наличие такого атлета в бд
        if (isDuplicateAthlete(fio, birth_date, birth_country)) {
            System.out.println("ERROR: Запрос на добавления спортсмена отклонён, найден спортсмен с такими данными");
        } else {
            // Добавляем в таблицу с пользователями
            insertUsers(login, password, idRole);
            System.out.print("LOG: Добавлен пользователь с login = '" + login + "' ");

            // Получаем идентификатор пользователя
            idUser = getIdUsers(login, password);

            // Добавляем нового спортсмена
            String sql = "INSERT INTO athletes (FIO, birth_date, birth_country, image, biography, Users_idUsers) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, fio);
            statement.setString(2, birth_date);
            statement.setString(3, birth_country);
            statement.setString(4, image.length() != 0 ? image : null);
            statement.setString(5, biography.length() != 0 ? biography : null);
            statement.setInt(6, idUser);
            statement.executeUpdate();
            windowMessenger("Добавление нового спортсмена прошла успешно");
            System.out.print("; Добавлен спортсмен с ФИО = '" + fio + "'\n");
        }
    }

    boolean isDuplicateAthlete(String fio, String birth_date, String birth_country) throws SQLException, ClassNotFoundException {
        /*
        Функция делает запрос и проверяет на наличие такого спортсмена в базе данных
        Input:
            fio - фамилия нового спортсмена
            birth_date - дата рождения нового спортсмена
            birth_country - страна в которой родился новый спортсмен
        Output:
            Функция возвращает бинарный ответ на наличие дубликатов
         */
        int count = 0;
        String sql = "SELECT count(*) as count FROM athletes WHERE FIO = ? AND birth_date = ? AND birth_country = ? ";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, fio);
        statement.setString(2, birth_date);
        statement.setString(3, birth_country);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) count = resultSet.getInt("count");
        return count > 0;
    }

    public int getIdJury(String fio) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет по ФИО получить идентификатор члена жури
        Input:
            fio - ФИО члена жури
        Output:
            Функция возвращает идентификатор члена жури
         */
        int idJury = 0;
        String sql = "SELECT idJury FROM jury WHERE FIO=?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, fio);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idJury = resultSet.getInt("idJury");
        return idJury;
    }

    public ArrayList<String> getNameCompetition() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список все соревнований из базы данных
        Output:
            competitionsList - список названий соревнования
         */
        String sql = "SELECT DISTINCT title FROM competition";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> competitionsList = new ArrayList<>();
        while (resultSet.next()) {
            competitionsList.add(resultSet.getString("title"));
        }
        return competitionsList;
    }

    public ArrayList<String> getNameCompetition(int idAthletes) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список все соревнований которых участвовал спортсмен
        Input:
            idAthletes - идентификатор атлета
        Output:
            competitionsList - список названий соревнований и их дат проведения
         */
        String sql = "SELECT title, holding_date FROM participation_history AS ph JOIN competition AS c ON " +
                "ph.Competition_idCompetition=c.idCompetition WHERE ph.Athletes_idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idAthletes);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> competitionsList = new ArrayList<>();
        while (resultSet.next()) {
            competitionsList.add(resultSet.getString("title") + "|" + resultSet.getString("holding_date"));
        }
        return competitionsList;
    }

    public ArrayList<String> getHoldingDate(String competition) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список дат проведения по определённому соревнованию
        Input:
            competition - название соревнования
        Output:
            holdingDatesList - список дат проведения
         */
        String sql = "SELECT holding_date FROM competition WHERE title = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, competition);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> holdingDatesList = new ArrayList<>();
        while (resultSet.next()) {
            holdingDatesList.add(resultSet.getString("holding_date"));
        }
        return holdingDatesList;
    }

    public ArrayList<String> getFioAthletes(String competition, String holding_date) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список атлетов на определённое соревнование
        Input:
            competition - название соревнования
            holding_date - дата проведения соревнования
        Output:
            fioAthletesList - список фамилий атлетов
         */
        int idCompetition = getIdCompetition(competition, holding_date); // Идентификатор соревнования

        // Получаем фамилии всех атлетов по соревнования
        String sql = "SELECT fio FROM participation_history AS ph JOIN athletes AS a ON a.idAthletes = " +
                "ph.Athletes_idAthletes WHERE Competition_idCompetition = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<String> fioAthletesList = new ArrayList<>();
        while (resultSet.next()) {
            fioAthletesList.add(resultSet.getString("fio"));
        }
        return fioAthletesList;
    }

    public ArrayList<String> getAvailableCriteria(String competition, String holding_date, String fio, int idJury)
            throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список критериев оценивания для определенного спортсмена на определенном соревнование
        по которым не стоят баллы
        Input:
             competition - название соревнования
             holding_date - дата проведения соревнования
             fio - ФИО спортсмена
             idJury - идентификатор члена жури
         Output:
            Функция возвращает список критериев, по которым определенный член жури не выставил баллы
         */
        int idCompetition = getIdCompetition(competition, holding_date); // Получаем идентификатор соревнования
        int idAthlete = 0; // идентификатор спортсмена


        // Получаем идентификатор атлета в определённом соревнование
        String sql = "SELECT Athletes_idAthletes as idAthletes FROM participation_history AS ph JOIN athletes AS a ON " +
                "a.idAthletes = ph.Athletes_idAthletes WHERE ph.Competition_idCompetition = ? AND a.FIO = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setString(2, fio);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idAthlete = resultSet.getInt("idAthletes");

        // Получаем критерии по котором не выставлены баллы
        sql = "SELECT nameCriteria FROM gradingcriteria AS gc LEFT JOIN (SELECT je.Jury_idGradingCriteria FROM " +
                "jury_evaluations AS je WHERE je.Jury_idCompetition = ? AND je.Jury_idAthlete = ? AND je.Jury_idJury = ?) " +
                "AS idGC ON gc.idGradingCriteria = idGC.Jury_idGradingCriteria  WHERE Jury_idGradingCriteria IS NULL";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setInt(2, idAthlete);
        statement.setInt(3, idJury);
        resultSet = statement.executeQuery();
        ArrayList<String> gradingCriteriaList = new ArrayList<>();
        while (resultSet.next()) {
            gradingCriteriaList.add(resultSet.getString("nameCriteria"));
        }
        return gradingCriteriaList;
    }

    public String getPhotoAthlete(String fio) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название фотографии по ФИО спортсмена
        Input:
            fio - ФИО спортсмена
        Output:
            Функция возвращает имя файла с фотографией атлета
         */
        String name_file = "";
        String sql = "SELECT image FROM athletes WHERE FIO = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, fio);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) name_file = resultSet.getString("image");
        return name_file;
    }

    public void addPoints(String competition, String holding_date, String fio, String nameCriteria,
                          int idJury, int points) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить в базу данных оценку жюри
        Input:
            competition - название соревнования
            holding_date - дата проведения
            fio - ФИО спортсмена
            nameCriteria - название критерия
            idJury - идентификатор члена жури
            points - количество выставленных баллов
         */
        int idCompetition = getIdCompetition(competition, holding_date); // Получаем идентификатор соревнования
        int idAthlete = 0;
        int idCriteria = getIdCriteria(nameCriteria); // Получаем идентификатор критерия оценивания

        // Получаем идентификатор атлета по соревнованию
        String sql = "SELECT Athletes_idAthletes as idAthletes FROM participation_history AS ph JOIN athletes AS a ON " +
                "a.idAthletes = ph.Athletes_idAthletes WHERE ph.Competition_idCompetition = ? AND a.FIO = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setString(2, fio);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idAthlete = resultSet.getInt("idAthletes");

        // Добавляем в таблицу с оценками оценку
        sql = "INSERT INTO jury_evaluations (Jury_idCompetition, Jury_idAthlete, Jury_idJury, Jury_idGradingCriteria, " +
                "points) VALUES (?, ?, ?, ?, ?)";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setInt(2, idAthlete);
        statement.setInt(3, idJury);
        statement.setInt(4, idCriteria);
        statement.setInt(5, points);
        statement.executeUpdate();
        windowMessenger("Оценка членом жури прошла успешно");
        System.out.println("LOG: Добавлена оценка члена жури с id=" + idJury + " спортсмену с id=" + idAthlete + " на " +
                "соревнование с id=" + idCompetition + " по критерию с id=" + idCriteria);
    }

    public int getIdSportsman(String fio) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить идентификатор спортсмена
        Input:
            fio - ФИО спортсмена
        Output:
            idSportsman - идентификатор спортсмена
         */
        int idSportsman = 0;
        String sql = "SELECT idAthletes FROM athletes WHERE FIO=?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, fio);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idSportsman = resultSet.getInt("idAthletes");
        return idSportsman;
    }

    public int getIdCompetition(String nameCompetition, String holdingDate) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет по названию соревнования и его дате проведения получить его идентификатор
        Input:
            nameCompetition - название соревнования
            holdingDate - дата проведения соревнования
        Output:
            idCompetition - идентификатор соревнования
         */
        int idCompetition = 0;
        String sql = "SELECT idCompetition FROM competition WHERE title = ? AND holding_date = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, nameCompetition);
        statement.setString(2, holdingDate);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idCompetition = resultSet.getInt("idCompetition");
        return idCompetition;
    }


    public String getPhotoCompetition(int idCompetition) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название файла с баннером соревнования
        Input:
            idCompetition - идентификатор соревнования
        Output:
            name_file - название файла с банером
         */
        String name_file = "";
        String sql = "SELECT baner FROM competition WHERE idCompetition = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) name_file = resultSet.getString("baner");
        return name_file;
    }

    public Text getDescription(int idCompetition) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить описание соревнования
        Input:
            idCompetition - идентификатор соревнования
        Output:
            description - описание соревнования
         */
        Text description = new Text();
        String sql = "SELECT description FROM competition WHERE idCompetition = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) description.setText(resultSet.getString("description"));
        return description;
    }

    public ArrayList<String> getJuryEvaluations(int idCompetition, int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список средних оценок по критериям
        Input:
            idCompetition - идентификатор соревнования
            idSportsman - идентификатор спортсмена
        Output:
            juryEvaluations - список критериев со средними оценками
         */
        ArrayList<String> juryEvaluations = new ArrayList<>();

        // Получаем место на соревнование если оно выставлено
        String sql = "SELECT ranking_place FROM participation_history WHERE Competition_idCompetition = ? AND Athletes_idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setInt(2, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next())
            if (resultSet.getString("ranking_place") != null)
                juryEvaluations.add("Место: " + resultSet.getString("ranking_place"));

        // Получаем список критериев и среднюю оценку
        sql = "SELECT nameCriteria, ROUND(AVG(points), 2) as meanPoints FROM jury_evaluations je JOIN " +
                "gradingcriteria gc ON je.Jury_idGradingCriteria = gc.idGradingCriteria WHERE je.Jury_idCompetition = ? " +
                "AND je.Jury_idAthlete = ? GROUP BY nameCriteria ";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setInt(2, idSportsman);
        resultSet = statement.executeQuery();
        while (resultSet.next()) juryEvaluations.add(resultSet.getString("nameCriteria") + ": " +
                resultSet.getString("meanPoints"));
        return juryEvaluations;
    }

    public String getNameAthlete(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить ФИО спортсмена
        Input:
            idSportsMan - идентификатор спортсмена
        Output:
            fio - ФИО спортсмена
         */
        String fio = "";
        String sql = "SELECT FIO FROM athletes WHERE idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) fio = resultSet.getString("FIO");
        return fio;
    }

    public String getBirthDate(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить дату рождения спортсмена
        Input:
            idSportsman - идентификатор спортсмена
        Output:
            birth_date - дата рождения спортсмена
         */
        String birth_date = "";
        String sql = "SELECT birth_date FROM athletes WHERE idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) birth_date = resultSet.getString("birth_date");
        return birth_date;
    }

    public String getBirthCountry(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить родину спортсмена
        Input:
            idSportsman - идентификатор спортсмена
        Output:
            birth_country - родина спортсмена
         */
        String birth_country = "";
        String sql = "SELECT birth_country FROM athletes WHERE idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) birth_country = resultSet.getString("birth_country");
        return birth_country;
    }

    public String getImage(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить названия файла с фотографией спортсмена
        Input:
            idSportsman - идентификатор спортсмена
        Output:
            image - название файла с фотографией
         */
        String image = "";
        String sql = "SELECT image FROM athletes WHERE idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) image = resultSet.getString("image");
        return image;
    }

    public String getBiography(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить биографию спортсмена
        Input:
            idSportsman - идентификатор спортсмена
        Output:
             biography - биография спортсмена
         */
        String biography = "";
        String sql = "SELECT biography FROM athletes WHERE idAthletes = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) biography = resultSet.getString("biography");
        return biography;
    }

    public void saveChangesAthlete(int idSportsman, String path_image, String biography) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сохранить изменения в информации спортсмена
        Input:
            idSportsman - идентификатор спортсмена
            path_image - название файла с фотографией спортсмена
            biography - биография спортсмена
         */
        String sql = "UPDATE athletes SET image = ?, biography = ? WHERE (idAthletes = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, path_image);
        statement.setString(2, biography);
        statement.setInt(3, idSportsman);
        statement.executeUpdate();
    }

    public int getIdOrganizer(String FIO) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить идентификатор спортсмена по ФИО
        Input:
            FIO - ФИО организатора
        Output:
            idOrganizer - идентификатор организатора
         */
        int idOrganizer = 0;
        String sql = "SELECT idOrganizer FROM organizer WHERE FIO = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, FIO);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idOrganizer = resultSet.getInt("idOrganizer");
        return idOrganizer;
    }

    public ArrayList<String> getCompetition(int idOrganizer) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список соревнований определённого организатора
        Input:
            idOrganizer - идентификатор организатора
        Output:
            list - список названий и дат проведения соревнований
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT title, holding_date FROM competition WHERE Organizer_idOrganizer = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idOrganizer);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("title") + "|" + resultSet.getString("holding_date"));
        return list;
    }

    public ArrayList<String> getCompetition() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список всех соревнования
        Output:
            list - список названий и дат проведения мероприятий
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT title, holding_date FROM competition ";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("title") + "|" + resultSet.getString("holding_date"));
        return list;
    }

    public String getPathBanner(String title, String holding_date) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить название файла с баннером соревнования
        Input:
            title - название соревнования
            holding_date - дата проведения соревнования
        Output:
            file_Banner - название файла с баннером
         */
        String file_banner = "";
        String sql = "SELECT baner FROM competition WHERE title=? AND holding_date = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, holding_date);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) file_banner = resultSet.getString("baner");
        return file_banner;
    }

    public String getDescriptionCompetition(String title, String holding_date) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить описание соревнования
        Input:
            title - название соревнования
            holding_date - дата проведения соревнования
        Output:
            description - описание соревнования
         */
        String description = "";
        String sql = "SELECT description FROM competition WHERE title=? AND holding_date = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, holding_date);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) description = resultSet.getString("description");
        return description;
    }

    public ArrayList<String> getAthleteList(String title, String holdingDate) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список спортсменом на определенном соревнование
        Input:
            title - название соревнования
            holdingDate - дата проведения соревнования
        Output:
            list - список спортсменом
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT FIO FROM participation_history AS PH JOIN athletes AS a ON a.idAthletes = ph.Athletes_idAthletes " +
                "WHERE ph.Competition_idCompetition = (SELECT idCompetition FROM competition WHERE title = " +
                "? AND holding_date = ? )";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, holdingDate);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("FIO"));
        return list;
    }

    public void saveInfoCompetition(String title, String holdingDate, String description, String path_banner)
            throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сохранить изменения в соревновании
        Input:
            title - название соревнования
            holdingDate - дата проведения соревнования
            description - описание соревнования
            path_banner - название файла содержащего баннер соревнования
         */
        int idCompetition = getIdCompetition(title, holdingDate); // Получаем идентификатор соревнования
        String sql = "UPDATE competition SET description = ?, baner = ? WHERE (idCompetition = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, description);
        statement.setString(2, path_banner);
        statement.setInt(3, idCompetition);
        statement.executeUpdate();
    }

    public ArrayList<String> generateRating(String title, String holdingDate) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет сгенерировать итоговую таблицу лидеров
        Input:
            title - название соревнования
            holdingDate - дата проведения соревнования
        Output:
            list - турнирная таблица в итоге
         */
        ArrayList<String> list = new ArrayList<>();
        int idCompetition = getIdCompetition(title, holdingDate); // Получаем идентификатор соревнования

        // Изменяет статус проведения мероприятия
        String sql = "UPDATE competition SET isRating = 1 WHERE (title = ? AND holding_date = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, holdingDate);
        statement.executeUpdate();

        // Получаем турнирную таблицу в итоге
        sql = "SELECT Jury_idAthlete, FIO, SUM(points) AS points FROM jury_evaluations AS je JOIN (SELECT a.idAthletes, a.FIO, " +
                "ph.Competition_idCompetition FROM participation_history AS ph JOIN athletes AS a ON a.idAthletes = " +
                "ph.Athletes_idAthletes WHERE ph.Competition_idCompetition = ?) AS promez ON promez.idAthletes = " +
                "je.Jury_idAthlete AND promez.Competition_idCompetition = je.Jury_idCompetition GROUP BY Jury_idAthlete,FIO ORDER BY points DESC";
        statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        ResultSet resultSet = statement.executeQuery();
        int i = 1;
        while (resultSet.next()) {
            list.add(i + " место: " + resultSet.getString("FIO") + "; Кол-во баллов: " +
                    resultSet.getString("points"));

            // Добавляем места спортсменам
            sql = "UPDATE participation_history SET ranking_place = ? WHERE (Competition_idCompetition = ?) and (Athletes_idAthletes = ?)";
            statement = getDbConnection().prepareStatement(sql);
            statement.setInt(1, i);
            statement.setInt(2, idCompetition);
            statement.setInt(3, resultSet.getInt("Jury_idAthlete"));
            statement.executeUpdate();
            i++;
        }
        return list;
    }

    public boolean checkGenerateRating(String title, String holdingDate) throws SQLException, ClassNotFoundException {
       /*
        Функция позволяет получить статус сформирована ли таблица лидеров
        Input:
            title - название соревнования
            holdingDate - дата проведения мероприятия
        Output:
            check - сформирована таблица или нет
        */
        boolean check = false;
        String sql = "SELECT isRating FROM competition WHERE title = ? AND holding_date = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, holdingDate);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) check = resultSet.getInt("isRating") == 1;
        return check;
    }

    public void addNewCompetition(int idOrganizer, String title, String holding_date, String description,
                                  String path_banner) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить новое соревнование
        Input:
            idOrganizer - идентификатор организатора
            title - название соревнования
            holding_date - дата проведения соревнования
            description - описание соревнования
            path_banner - имя файла содержащего баннер
         */
        String sql = "INSERT INTO competition (title, baner, description, holding_date, Organizer_idOrganizer) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, title);
        statement.setString(2, path_banner.length() > 3 ? path_banner : null);
        statement.setString(3, description.length() > 3 ? description : null);
        statement.setString(4, holding_date);
        statement.setInt(5, idOrganizer);
        statement.executeUpdate();
    }

    public void dropCompetition(String title, String holdingDate) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет отменить соревнование
        Input:
            title - название соревнования
            holdingDate - дата проведения соревнования
         */
        int idCompetition = getIdCompetition(title, holdingDate); // Получаем идентификатор соревнования
        String sql = "SELECT count(*) AS count FROM jury_evaluations WHERE  Jury_idCompetition = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            // Проверя м на наличие выставленных оценок
            if (resultSet.getInt("count") == 0) {
                sql = "DELETE FROM competition WHERE (idCompetition = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setInt(1, idCompetition);
                statement.executeUpdate();
                sql = "DELETE FROM participation_history WHERE (Competition_idCompetition = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setInt(1, idCompetition);
                statement.executeUpdate();
                windowMessenger("Отмена соревнования прошла успешно");
            } else {
                windowMessengerError("Участникам уже выставлены баллы");
            }
        }
    }

    public ArrayList<String> getCompetitionApply(int idSportsman) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список доступны соревнований для спортсмена
        Input:
            idSportsman - идентификатор спортсмена
        Output:
            list - список названий и дат проведения соревнований
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT competition.title, competition.holding_date FROM competition left join (SELECT * FROM " +
                "competition AS c left join participation_history AS ph ON ph.Competition_idCompetition = c.idCompetition " +
                "WHERE ph.Athletes_idAthletes = ?) AS itog ON competition.idCompetition = itog.idCompetition WHERE itog.idCompetition is null";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idSportsman);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("title") + "|" + resultSet.getString("holding_date"));
        return list;
    }

    public void addAthletesCompetition(int idCompetition, int idSportsmen) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет зарегистрировать спортсмена на соревнование
        Input:
            idCompetition - идентификатор соревнования
            idSportsman - идентификатор спортсмена
         */
        String sql = "INSERT INTO participation_history (Competition_idCompetition, Athletes_idAthletes) VALUES (?, ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCompetition);
        statement.setInt(2, idSportsmen);
        statement.executeUpdate();
        windowMessenger("Спортсмен успешно зарегистрирован на соревнование");
        System.out.println("LOG: зарегистрирован спортсмен с id = " + idSportsmen + " на соревнование с id = " + idCompetition);
    }

    public ArrayList<String> getCriteria() throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить список критериев оценивания
        Output:
            list - список критериев
         */
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT nameCriteria FROM gradingcriteria";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) list.add(resultSet.getString("nameCriteria"));
        return list;
    }

    public boolean isNotDuplicateGuardingCriteria(String nameCriteria) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет проверить на наличие критерия оценивания
        Input:
            nameCriteria - название критерия
        Output:
            isNotDuplicate - позволяет вернуть отсутствует ли такой критерий оценивания
         */
        boolean isNotDuplicate = false;
        String sql = "SELECT count(*) AS count FROM gradingcriteria WHERE nameCriteria = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, nameCriteria);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) isNotDuplicate = resultSet.getInt("count") < 1;
        return isNotDuplicate;
    }

    public void addNewGradingCriteria(String nameCriteria) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет добавить новый критерий оценивания
        Input:
            nameCriteria - название критерия оценивания
         */
        boolean isNotDuplicate = isNotDuplicateGuardingCriteria(nameCriteria); // Проверка на наличие критерия оценивания
        if (isNotDuplicate) {
            String sql = "INSERT INTO gradingcriteria (nameCriteria) VALUES (?)";
            PreparedStatement statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, nameCriteria);
            statement.executeUpdate();
            windowMessenger("Добавления критерия оценивания прошло успешно");
        } else {
            windowMessengerError("Такой критерий оценивания уже существует");
        }

    }

    public void editGradingCriteria(String originalName, String newName) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет отредактировать название критерия оценивания
        Input:
            originalName - оригинальное название критерия оценивания
            newName - новое название критерия оценивания
         */
        boolean isDuplicate = isNotDuplicateGuardingCriteria(newName); // Проверка на наличие критерия оценивания
        if (isDuplicate) {
            String sql = "UPDATE gradingcriteria SET nameCriteria = ? WHERE (nameCriteria = ?)";
            PreparedStatement statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, newName);
            statement.setString(2, originalName);
            statement.executeUpdate();
            windowMessenger("Изменение названия критерия оценивания прошло успешно");
        } else {
            windowMessengerError("Такой критерий оценивания уже существует");
        }

    }

    public int getIdCriteria(String nameCriteria) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет по названию критерия оценивания получить его идентификатор
        Input:
            nameCriteria - названия критерия оценивания
        Output:
            idCriteria - идентификатор критерия оценивания
         */
        int idCriteria = 0;
        String sql = "SELECT idGradingCriteria FROM gradingcriteria WHERE nameCriteria = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, nameCriteria);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idCriteria = resultSet.getInt("idGradingCriteria");
        return idCriteria;
    }

    public void dropGradingCriteria(String nameCriteria) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить критерий оценивания
        Input:
            nameCriteria - названия критерия оценивания
         */
        int idCriteria = getIdCriteria(nameCriteria); // Получаем идентификатор критерия оценивания
        String sql = "SELECT count(*) AS count FROM jury_evaluations WHERE Jury_idGradingCriteria = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idCriteria);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getInt("count") == 0) {
                sql = "DELETE FROM gradingcriteria WHERE (nameCriteria = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setString(1, nameCriteria);
                statement.executeUpdate();
                windowMessenger("Критерий оценивания успешно удалён");
            } else windowMessengerError("Члены жури уже вставляли по данному критерию баллы");
        }

    }

    public int getIdUsers(String login, String password) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет получить идентификатор пользователя по его логину и паролю
        Input:
            login - логин пользователя
            password - пароль пользователя
        Output:
            idUser - идентификатор пользователя
         */
        int idUser = 0;
        String sql = "SELECT idUsers FROM users WHERE login = ? AND password = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) idUser = resultSet.getInt("idUsers");
        return idUser;
    }

    private void deleteUser(int idUser) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить пользователя по его и его идентификатору
        InputL
            idUser - идентификатор пользователя
         */
        String sql = "DELETE FROM users WHERE (idUsers = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idUser);
        statement.executeUpdate();
    }

    private void deleteVisitor(int idUser) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить посетителя по его и его идентификатору
        InputL
            idUser - идентификатор пользователя
         */
        String sql = "DELETE FROM visitor WHERE (Users_idUsers = ?)";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, idUser);
        statement.executeUpdate();
        deleteUser(idUser); // Удаляем из таблицы пользователей
        windowMessenger("Удаление посетителя прошло успешно");
        System.out.println("LOG: удаление посетителя с idUsers = " + idUser);
    }

    private void deleteAthlete(int idUser) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить спортсмена по его и его идентификатору
        InputL
            idUser - идентификатор пользователя
         */
        // Количество участий спортсмена в соревнованиях
        String sql = "SELECT count(*) AS count FROM athletes AS a JOIN participation_history AS ph ON ph.Athletes_idAthletes = a.idAthletes WHERE a.Users_idUsers = " + idUser;
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getInt("count") == 0) {
                sql = "DELETE FROM athletes WHERE (Users_idUsers = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setInt(1, idUser);
                statement.executeUpdate();
                deleteUser(idUser); // Удаляем из таблицы пользователей
                windowMessenger("Удаление спортсмена прошло успешно");
                System.out.println("LOG: удаление спортсмена с idUsers = " + idUser);
            } else {
                windowMessengerError("Данный спортсмен участвует/участвовал в соревнованиях");
            }
        }
    }

    private void deleteOrganizer(int idUser) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить организатора по его и его идентификатору
        InputL
            idUser - идентификатор пользователя
         */
        // Количество проведённых мероприятий
        String sql = "SELECT count(*) AS count FROM organizer AS o JOIN competition AS c ON c.Organizer_idOrganizer = o.idOrganizer WHERE o.Users_idUsers = " + idUser;
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getInt("count") == 0) {
                sql = "DELETE FROM organizer WHERE (Users_idUsers = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setInt(1, idUser);
                statement.executeUpdate();
                deleteUser(idUser); // Удаляем из таблицы пользователей
                windowMessenger("Удаление организатора прошло успешно");
                System.out.println("LOG: удаление организатора с idUsers = " + idUser);
            } else {
                windowMessengerError("Организатор проводит/проводил соревнования");
            }
        }
    }

    private void deleteJury(int idUser) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить члена жюри по его и его идентификатору
        InputL
            idUser - идентификатор пользователя
         */
        // Количество оценённых спортсменов
        String sql = "SELECT count(*) AS count FROM jury AS j JOIN jury_evaluations AS je ON j.idJury = je.Jury_idJury WHERE j.Users_idUsers = " + idUser;
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            if (resultSet.getInt("count") == 0) {
                sql = "DELETE FROM jury WHERE (Users_idUsers = ?)";
                statement = getDbConnection().prepareStatement(sql);
                statement.setInt(1, idUser);
                statement.executeUpdate();
                deleteUser(idUser); // Удаляем из таблицы пользователей
                windowMessenger("Удаление челна жури прошло успешно");
                System.out.println("LOG: удаление члена жури с idUsers = " + idUser);
            } else {
                windowMessengerError("Член жури уже оценивал спортсменов");
            }
        }
    }


    public void deleteUser(String login, String password) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет удалить пользователя
        Input:
            idRole - идентификатор роли
            login - логин пользователя
            password - пароль пользователя
        */
        int idUser = getIdUsers(login, password); // Получаем идентификатор пользователя
        int idRole = getIdRole(login, password); // Получаем идентификатор роли
        if (idUser != 0) {
            if (idRole == 1) {
                deleteVisitor(idUser);
            } else if (idRole == 2) {
                deleteAthlete(idUser);
            } else if (idRole == 3) {
                deleteOrganizer(idUser);
            } else if (idRole == 4) {
                deleteJury(idUser);
            } else if (idRole == 5) {
                windowMessengerError("Администратора нельзя удалять");
            }
        } else {
            windowMessengerError("Такого пользователя не существует");
        }

    }

    public void editUser(int idRole, String login, String password, String fio) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет изменить ФИО пользователя
        Input:
            idRole - идентификатор роли
            login - логин пользователя
            password - пароль пользователя
            fio - ФИО пользователя
         */
        String sql;
        int idUser = getIdUsers(login, password);
        if (idUser != 0 && idUser != 5) {
            if (idRole == 1) {
                sql = "UPDATE visitor SET FIO = ? WHERE (Users_idUsers = ?)";
            } else if (idRole == 2) {
                sql = "UPDATE athletes SET FIO = ? WHERE (Users_idUsers = ?)";
            } else if (idRole == 3) {
                sql = "UPDATE organizer SET FIO = ? WHERE (Users_idUsers = ?)";
            } else {
                sql = "UPDATE jury SET FIO = ? WHERE (Users_idUsers = ?)";
            }
            PreparedStatement statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, fio);
            statement.setInt(2, idUser);
            statement.executeUpdate();
            windowMessenger("Изменения успешно применены");
            System.out.println("LOG: изменения пользователя с id = " + idUser);
        } else {
            windowMessengerError("Нет такого пользователя");
        }
    }

    public void editUser(String login, String password, String fio, String birth_country,
                         String birth_date) throws SQLException, ClassNotFoundException {
        /*
        Функция позволяет внести изменения в информацию спортсмена
        Input:
            login - логин спортсмена
            password - пароль спортсмена
            fio - ФИО спортсмена
            birth_country - родина спортсмена
            birth_date - дата рождения спортсмена
        Output:

         */
        int idUser = getIdUsers(login, password); // Получаем идентификатор пользователя
        if (idUser != 0) {
            String sql = "UPDATE athletes SET FIO = ?, birth_date = ?, birth_country = ? WHERE (Users_idUsers = ?)";
            PreparedStatement statement = getDbConnection().prepareStatement(sql);
            statement.setString(1, fio);
            statement.setString(2, birth_date);
            statement.setString(3, birth_country);
            statement.setInt(4, idUser);
            statement.executeUpdate();
            windowMessenger("Изменения успешно применены");
            System.out.println("LOG: изменения спортсмена с id = " + idUser);
        } else {
            windowMessengerError("Такого пользователя нет");
        }
    }

    void windowMessenger(String messenger) {
        /*
        Функция позволяет вывести окно с сообщением ошибки
         */
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех!!!");
        alert.setHeaderText(String.format(messenger));
        alert.showAndWait();
    }

    void windowMessengerError(String messenger) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!!!");
        alert.setHeaderText(String.format(messenger));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("error.png"));
        alert.showAndWait();
    }

}
