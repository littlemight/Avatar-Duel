package com.avatarduel.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable  {
    @FXML
    FlowPane temp_card_book;

    @FXML
    Pane card_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void addCard(StackPane card_box) {
        card_box.prefWidthProperty().bind(temp_card_book.prefWidthProperty().divide(5));
        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));

        temp_card_book.getChildren().add(card_box);
    }
}
