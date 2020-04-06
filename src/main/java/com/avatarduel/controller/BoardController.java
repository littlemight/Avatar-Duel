package com.avatarduel.controller;

import com.avatarduel.event.EventChannel;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.EmptyCard;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable  {
    @FXML
    FlowPane temp_card_book;

    @FXML
    Pane hover_card_pane;

    StackPane hover_card_box;

    private CardController hover_card_controller;

    private EventChannel channel;

    GridPane card_field;
    
    @FXML
    Pane card_pane;
    
    @FXML
    Label neff_deck, size_deck;
    Pane deck;
    
    private int col=0;
    private int row=0;
    
    public BoardController(EventChannel channel) {
        this.channel = channel;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader hover_card_loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
            hover_card_loader.setControllerFactory(c -> new CardController(this.channel));
            hover_card_box = hover_card_loader.load();

            hover_card_controller = hover_card_loader.getController();

            hover_card_box.prefHeightProperty().bind(hover_card_pane.prefHeightProperty());
            hover_card_box.prefWidthProperty().bind(hover_card_pane.prefWidthProperty());
            hover_card_pane.getChildren().add(hover_card_box);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void addCard(CardController card_controller) {
        channel.addSubscriber(card_controller, hover_card_controller);
        StackPane card_box = card_controller.getContent();
        card_box.prefWidthProperty().bind(temp_card_book.prefWidthProperty().divide(5));
        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));
        temp_card_book.getChildren().add(card_box);
    }

    public void addCardField(CardController card_controller) {
        StackPane card_box = card_controller.getContent();

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
