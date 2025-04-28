package org.example.db_javafx;

import java.sql.*;

public class DB {
    //    устанавливаем соединение
    private final String HOST = "localhost";
    private final String PORT = "8889";
    private final String DB_NAME = "db_java";
    private final String LOGIN = "root";
    private final String PASS = "root";

    private Connection dbConn = null;

    private Connection getDbConn() throws ClassNotFoundException, SQLException {
        String get_Str = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConn = DriverManager.getConnection(get_Str, LOGIN, PASS);
        return dbConn;
    }
    //    проверка на подключение
    public void isConnected() throws SQLException, ClassNotFoundException {
        dbConn = getDbConn();
        System.out.println(dbConn.isValid(1000));
    }
//    создаем метод для внесения в базу данных при регистрации
//    будем принимать String login, String email, String pass
    public void regUser (String login, String email, String pass) {
//        пишем sql команду
//        в values будут подставлены значения
        String sql = "INSERT INTO `users` (login, email, pass) VALUES (?, ?, ?)";
//        создаем объект и исключ ошибки
        try {
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
//            устанавливаем  в вопросы параметры
            prSt.setString(1, login);
            prSt.setString(2, email);
            prSt.setString(3, pass);
//            добавляем пользователя
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
//    проврека на одинковый логин
    public boolean isUserExist(String login) {
        String sql = "SELECT `id` FROM `users` WHERE `login` = ?";
        try {
//            создаем объект
        PreparedStatement prSt = getDbConn().prepareStatement(sql);
        prSt.setString(1, login);
//        делаем выборку
            ResultSet rs = prSt.executeQuery();
//            возрвщаем запись, если она сущ
            return rs.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //    вернем False
        return false;
    }
    //    авторизация
    public boolean authUser(String login, String pass) {
//        пишем запрос для поиска пользоват, у кот логин и пароль будут совпад с опредленн значен
        String sql = "SELECT `id` FROM `users` WHERE `login` = ? AND `pass` = ?";
        try {
//            создаем шаблон запроса, куда подставляем данные от пользователя
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
            prSt.setString(1, login);
            prSt.setString(2, pass);
//        делаем выборку
            ResultSet rs = prSt.executeQuery();
//            возрвщаем запись, если она сущ
            return rs.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //    вернем False
        return false;
    }
//    получение всех статей
    public ResultSet getArticles() throws SQLException, ClassNotFoundException {
        String sql = "SELECT `id`,`title`, `intro` FROM `articles`";
        Statement stmt = getDbConn().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return rs;
    }
    //    ищем одну статью по ее id
    public ResultSet getIDArticles(int id) throws SQLException, ClassNotFoundException {
//        ищем одну статью по id
        String sql = "SELECT `title`, `text` FROM `articles` WHERE `id` = ?";
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
            prSt.setInt(1, id);
//            выполнем запрос и его же возвращаем из функции
            return prSt.executeQuery();
    }

// описываем сам метод добавления статьи
    public void addArticle(String title, String intro, String full_text) {
        //        пишем sql команду, в values будут подставлены значения
        String sql = "INSERT INTO `articles` (`title`, `intro`, `text`) VALUES (?, ?, ?)";
//        создаем объект и исключ ошибки
        try {
            PreparedStatement prSt = getDbConn().prepareStatement(sql);
//            устанавливаем  в вопросы параметры
            prSt.setString(1, title);
            prSt.setString(2, intro);
            prSt.setString(3, full_text);
//            добавляем пользователя
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
