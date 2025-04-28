package org.example.db_javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.db_javafx.DB;
import org.example.db_javafx.HelloApplication;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddArticleController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea full_text_field;

    @FXML
    private TextArea intro_field;

    @FXML
    private TextField title_field;

    @FXML
    void addArticle(ActionEvent event) {
//   получаем данные от пользователя
//   getCharacters получает все символы и при помощи (toString) приводим к формату, getText для TextArea
        String title = title_field.getCharacters().toString();
        String intro = intro_field.getText().toString();
        String full_text = full_text_field.getText().toString();

//     при нажатии на кнопку будет установлена белая обводка,
//     если прошли все проверки на ошибки, а иначе красная
        title_field.setStyle("-fx-border-color: #fafafa");
        intro_field.setStyle("-fx-border-color: #fafafa");
        full_text_field.setStyle("-fx-border-color: #fafafa");
//            добавляем проверки
//            если меньше или 1 символ, то обводка будет красного цвета
        if(title.length() <=1)
            title_field.setStyle("-fx-border-color: #e06249");
        else if(intro.length() <=10)
            intro_field.setStyle("-fx-border-color: #e06249");
        else if(full_text.length() <=10)
            full_text_field.setStyle("-fx-border-color: #e06249");
//        метод для добавления статьи в БД
        else {
            DB db = new DB();
            db.addArticle(title, intro, full_text);
//            переадресация обратно на страницу
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                HelloApplication.setScene("articles-panel.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void initialize() {

    }
}
