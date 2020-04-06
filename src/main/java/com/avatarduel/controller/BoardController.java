package com.avatarduel.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable  {
    @FXML
    FlowPane temp_card_book;

    @FXML
    GridPane card_field;

    @FXML
    Pane card_pane;

    @FXML
    Label neff_deck, size_deck;
    Pane deck;

    private int col=0;
    private int row=0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void addCard(StackPane card_box) {
        card_box.prefWidthProperty().bind(temp_card_book.prefWidthProperty().divide(5));
        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));

        temp_card_book.getChildren().add(card_box);
    }

    public void addCardField(StackPane card_box) {
        card_box.prefWidthProperty().bind(card_field.prefWidthProperty().divide(8));
        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));
        card_field.add(card_box,col,row);
        col++;
        if (col==8){col=0;row++;}
    }

    public void updateDeck(IntegerProperty neff, int size){
        // deck.getChildren().add(card_box);
        neff_deck.textProperty().bind(Bindings.convert(neff));
        size_deck.textProperty().setValue(Integer.toString(size));
    }
}
