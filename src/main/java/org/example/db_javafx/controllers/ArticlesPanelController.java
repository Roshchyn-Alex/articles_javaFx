package org.example.db_javafx.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.db_javafx.DB;
import org.example.db_javafx.HelloApplication;

public class ArticlesPanelController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button exit_btn, add_article_btn;

    @FXML
    private VBox panelVBox;

//    в эту переменную при нажатии на статью будем вставлять id статьи, кот хотим открыть
    public static int fullArticledId;

    @FXML
    void initialize() throws SQLException, IOException, ClassNotFoundException {
//        делаем через цикл, чтобы сразу 10 кнопок было
//        for (int i = 0; i < 10; i++) {
//        установка новых объектов через код
//            Button button = new Button("Кнопка");
////        установка id
//            button.setId("button");
////        кнопку размещаем  путем добавления HBox (объекты располаг друг возле друга)
//            HBox hBox = new HBox();
//            hBox.getChildren().add(button);
////       кнопку по центру
//            hBox.setAlignment(Pos.BASELINE_CENTER);
        DB db = new DB();
        ResultSet result = db.getArticles();
        while (result.next()) {
//            чтобы это не писать, подгружаем файлы с дизайном, обращ к осноному классу HelloApplication
//            желтая лампочка, чтобы requireNonNull не пустое значение будет
            Node node = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("article.fxml")));
//          прописываем  подстановку ID для каждой статьи

//      подгружаем статьи из БД, обращ к node и методу lookup (позвол найти объект по id)
//      все полученное преобраз к классу Label
            Label title = (Label) node.lookup("#title");
//            обращ к найденному объекту и подставляем данные
            title.setText(result.getString("title"));

            Label intro = (Label) node.lookup("#intro");
            intro.setText(result.getString("intro"));

//          в качестве ID для все статьи мы устанавливаем id самой записи
//          для этого ID берем из БД из объекта result
//          в метод gerArticles выбираем не только title и intro, но и ID
            node.setId(result.getString("id"));

            final Node nodeSet = node;

//            добавляем обработчик событий setOnMouseClicked - нажатие, Entered - наведение
            node.setOnMouseEntered(event -> {
//                устанавливаем новые стили
                nodeSet.setStyle("-fx-background-color: #707173");
            });
//            убираем мышь
            node.setOnMouseExited(event -> {
                nodeSet.setStyle("-fx-background-color: #343434");
            });
//            кликаем мышкой
            node.setOnMouseClicked(event -> {
//                при нажатии на саму статью подставляем значение id
//                уазываем ID той стати, что хотим открыть
//                чтобы указать это, ьерем ID из того блока, по которому нажали
//                этот id мы уже ранее указывали для блока - node.setId(res.getString("id"))
                fullArticledId = Integer.parseInt(nodeSet.getId());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    HelloApplication.setScene("show_article.fxml", stage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

//        передаем  Node на страницу с дизайном, так как род класс
            panelVBox.getChildren().add(node);
//        отсупы
            panelVBox.setSpacing(10);
        }

//        прописываеи, что при нажатии на кнопку Выйти
        exit_btn.setOnAction(event -> {
            try {
                exitUser(event);
            } catch (IOException e) {
               e.printStackTrace();
            }
        });
//        прописываеи, что при нажатии на кнопку добавить статью
        add_article_btn.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                HelloApplication.setScene("add_article.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
//    сам метод описываем
    private void exitUser(ActionEvent event) throws IOException {
//        чтобы выйти нам нужно удалить сам файл, кот созд при сериализации
        File file = new File("user.settings");
        file.delete();

//        при нажатии Выйти переадрес на main.fxml
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        HelloApplication.setScene("main.fxml", stage);
    }

}
