package org.example.db_javafx.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.db_javafx.DB;
import org.example.db_javafx.HelloApplication;
import org.example.db_javafx.models.User;

public class RegController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button auth_btn;

    @FXML
    private TextField auth_login;

    @FXML
    private PasswordField auth_pass;

    @FXML
    private Button reg_btn;

    @FXML
    private TextField reg_email;

    @FXML
    private TextField reg_login;

    @FXML
    private PasswordField reg_pass;

    @FXML
    private CheckBox reg_rights;

    //        создаем новый объект на основе класса DB
    private DB db = new DB();

    @FXML
    void initialize() {
//            при нажатии на кнопку -> запускаем обработчик событий -> регистрацию
        reg_btn.setOnAction(event -> {
            registrationUser();
        });
//            авторизация пользователя
//        добавляем event чтобы было новое окошко после авторизации
        auth_btn.setOnAction(event -> {
            try {
                authUser(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void authUser(ActionEvent event) throws IOException {
//            получаем данные от пользователя
//            getCharacters получает все символы и при помощи (toString) приводим к формату
        String login = auth_login.getCharacters().toString();
        String pass = auth_pass.getCharacters().toString();

//     при нажатии на кнопку войти, будет установлена белая обводка,
//     если прошли все проверки на ошибки, а иначе красная
        auth_login.setStyle("-fx-border-color: #fafafa");
        reg_pass.setStyle("-fx-border-color: #fafafa");
//            добавляем проверки
//            если логин меньше или 1 символ, то обводка будет красного увета
        if(login.length() <= 1)
            auth_login.setStyle("-fx-border-color: #e06249");
        else if(pass.length() <= 3)
            auth_pass.setStyle("-fx-border-color: #e06249");
//        проверка на сущ пользователя
//        если этот метод вернет false, то такого пользователя нет
        else if(!db.authUser(login, md5String(pass)))
            auth_btn.setText("Пользователя нет");
        else {
//     используем новый метод   authUser     передаем не пароль, а кеш
            db.authUser(login, md5String(pass));
//                очищаем поля, если пользователь есть
            auth_login.setText("");
            auth_pass.setText("");
            auth_btn.setText("Готово!");

//            для сохранения логина использ сериализац
//            создаем или открываем файл для записи user.settings
            FileOutputStream fos = new FileOutputStream("user.settings");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            поместили объект для записи (логин)
            oos.writeObject(new User(login));
            oos.close();

// чтобы после авторизации новое окно было
//            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("articles-panel.fxml"));
//            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
////            нужно срздать объект stage и указываем на какой scene (окошку мы находимся)
////            поэтому мы должны обратиться к такому параметру как event
////            привожим все к классу Node (род класс, благодаря кот опишем объект)
////            после чего переходим к метода getScene, чтобы получ текущ сцену и тек окно getWindow()
////            потом приводим к классу Stage
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
////           поменяли сцену
//            stage.setScene(scene);
////            отобразили новое окно
//            stage.show();
//            сокращаем код и просто прибегаем к методу уже
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            обращ к HelloApplication
            HelloApplication.setScene("articles-panel.fxml", stage);
        }
    }
    private void registrationUser() {
//            получаем данные от пользователя
//            getCharacters получает все символы и при помощи (toString) приводим к формату
            String login = reg_login.getCharacters().toString();
            String email = reg_email.getCharacters().toString();
            String pass = reg_pass.getCharacters().toString();

//     при нажатии на кнопку зарегистрироваться, будет установлена белая обводка,
//     если прошли все проверки на ошибки, а иначе красная
            reg_login.setStyle("-fx-border-color: #fafafa");
            reg_email.setStyle("-fx-border-color: #fafafa");
            reg_pass.setStyle("-fx-border-color: #fafafa");
//            добавляем проверки
//            если логин меньше или 1 символ, то обводка будет красного цвета
            if(login.length() <=1)
                reg_login.setStyle("-fx-border-color: #e06249");
//            добавляем проверки email
            else if(email.length() <=5 || !email.contains("@") || !email.contains("."))
                reg_email.setStyle("-fx-border-color: #e06249");
            else if(pass.length() <=3)
                reg_pass.setStyle("-fx-border-color: #e06249");
//            проверяем поле с галочкой isSelected
            else if(!reg_rights.isSelected())
//                выводим на саму кнопку
                reg_btn.setText("Поставьте галочку");
//            проверяем на логин
            else if(db.isUserExist(login))
                reg_btn.setText("Введите другой логин");
            else {
//                сам код для регистрации
//                передаем не пароль, а кеш
                db.regUser(login, email, md5String(pass));
//                очищаем поля
                reg_login.setText("");
                reg_email.setText("");
                reg_pass.setText("");
                reg_btn.setText("Готово!");
            }
        }
//        переводим пароль в хэш-код
    public static String md5String (String pass) {
//            MessageDigest позволяет из обычной строки сформиров хэш-код
            MessageDigest messageDigest = null;
//            объект для хранения преобраз строки в байт-код
            byte[] digest = new byte[0];
            try {
//                указываем метод
                messageDigest = MessageDigest.getInstance("MD5");
//                очищаем от лишгих символов
                messageDigest.reset();
                //       преобраз в байт-код
                messageDigest.update(pass.getBytes());
//                полученные байты запихнули в массив
                digest = messageDigest.digest();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
//            преобразуем к одному большому числу
            BigInteger bigInt = new BigInteger(1, digest);
//            преобразуем в строку
            String m5dHex = bigInt.toString(16);
//            цикл обычно состоит из 32 симвовлов
            while (m5dHex.length() < 32) {
                m5dHex = "0" + m5dHex;
            }
//            возвращ полученную строку, что явдяется хэш-код нашего пароля
            return m5dHex;
    }
}



