package org.example.db_javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.db_javafx.models.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        //    для проверки соединения создаем объект
        DB db = new DB();
//    обращаемся к методу
        try {
            db.isConnected();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
//        так при выходе пользоват, мы удаляем сам файл, чтоюбы не было ошибки нужно добавить еще одно условие
        File file = new File("user.settings");
//        если файл сущ, то тогда десириализ
        if(file.exists()) {
//        десириализация
        FileInputStream fis = new FileInputStream("user.settings");
        ObjectInputStream ois = new ObjectInputStream(fis);
        User user = (User) ois.readObject();
        ois.close();
        //        проводим проверку, если user есть в БД, то откроем одну страницу articles-panel.fxml
//            в противном случае другую, как и при пустом логине
            if (db.isUserExist(user.getLogin()))
//       просто прибегаем к методу уже
                setScene("articles-panel.fxml", stage);
            else
                setScene("main.fxml", stage);
        }   else
            setScene("main.fxml", stage);
    }

    public static void main(String[] args) {
        launch();
    }

//    много раз обращ к показу новой сцены, поэтому сделаем отдельный метод
//    будем передава название Scene(fxml файл), а также Stage
    public static void setScene(String sceneName, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Программа для работы с БД");
        stage.setScene(scene);
        stage.show();
    }
}