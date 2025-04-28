package org.example.db_javafx.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.db_javafx.DB;
import org.example.db_javafx.HelloApplication;

public class ShowArticleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back_btn;

    @FXML
    private Label show_text;

    @FXML
    private Label show_title;

    DB db = new DB();

    @FXML
    void initialize() throws SQLException, IOException, ClassNotFoundException {

//        получвем статью по ID, который записан в статьческой переменной
        ResultSet res = db.getIDArticles(ArticlesPanelController.fullArticledId);

//         перебираем полученные данные
        while (res.next()) {
            show_text.setText(res.getString("text"));
            show_title.setText(res.getString("title"));
        }

        //        прописываеи, что при нажатии на кнопку назад
        back_btn.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                HelloApplication.setScene("articles-panel.fxml", stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
