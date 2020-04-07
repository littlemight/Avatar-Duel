package com.avatarduel.controller;

import com.avatarduel.event.Event;
import com.avatarduel.event.EventChannel;
import com.avatarduel.event.NewCardDrawnEvent;
import com.avatarduel.event.Subscriber;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.EmptyCard;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable, Subscriber {
    @FXML
    Pane hover_card_pane;

    @FXML
    VBox hover_card_view;

    @FXML
    VBox hover_card_detailed_box;

    @FXML
    Label detailed_box_desc;

    @FXML
    Pane player1_pane;

    @FXML
    Pane player2_pane;

    AnchorPane player1_field, player2_field;


    /**
     * The model
     * It should be a class Game which has the deck, 2 players, and other game rules for each phase
     */
    Player player1, player2;
    PlayerFieldController player1_controller, player2_controller;

    StackPane hover_card_box;
    private CardController hover_card_controller;
    private EventChannel channel;
//
//    @FXML
//    Pane card_pane;
//
//    @FXML
//    Label neff_deck, size_deck;
//
////    Pane deck;
//
//    private int col=0;
//    private int row=0;
//
    public BoardController(EventChannel channel) {
        this.channel = channel;
        this.player1 = new Player(); // should be a singleton object
        this.player2 = new Player();
    }

    /**
     * Should be called after setting Player1 and Player2
     * @param location
     * @param resources
     */
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

            FXMLLoader player1_loader = new FXMLLoader(getClass().getResource("../view/Player1Field.fxml"));
            player1_loader.setControllerFactory(c -> new PlayerFieldController(this.channel));
            player1_field = player1_loader.load();
            this.player1_controller = player1_loader.getController();
            this.player1_pane.getChildren().add(player1_field);

            FXMLLoader player2_loader = new FXMLLoader(getClass().getResource("../view/Player2Field.fxml"));
            player2_loader.setControllerFactory(c -> new PlayerFieldController(this.channel));
            player2_field = player2_loader.load();
            this.player2_controller = player2_loader.getController();
            this.player2_pane.getChildren().add(player2_field);

            this.channel.addSubscriber(player1_controller, this);
            this.channel.addSubscriber(player2_controller, this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
//
    public void setPlayer1(Player player) {
        try {
            this.player1 = player;
            player1_controller.setPlayer(this.player1);
        } catch (Exception e) {
            System.out.println("In Player Controller: " + e);
        }
    }

    public void setPlayer2(Player player) {
        this.player2 = player;
        player2_controller.setPlayer(this.player2);
    }

    /**
     * For testing purposes.
     */
    public void drawBoth() {
        for (int i = 0; i < 7; i++) {
            player1_controller.draw();
            player2_controller.draw();
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof NewCardDrawnEvent) {
            this.channel.addSubscriber((CardController)event.getInfo(), this.hover_card_controller);
        }
    }
//
//    public void addCard(CardController card_controller) {
//        channel.addSubscriber(card_controller, hover_card_controller);
////        StackPane card_box = card_controller.getContent();
////        card_box.prefWidthProperty().bind(temp_card_book.prefWidthProperty().divide(5));
////        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));
////        temp_card_book.getChildren().add(card_box);
//    }
//
//    public void addCardField(CardController card_controller) {
//        StackPane card_box = card_controller.getContent();
//
////        card_box.prefHeightProperty().bind(card_field.prefWidthProperty().divide(8));
////        card_field.add(card_box,col,row);
////        col++;
////        if (col==8){col=0;row++;}
//    }
//
//    public void updateDeck(IntegerProperty neff, int size){
//        // deck.getChildren().add(card_box);
//        neff_deck.textProperty().bind(Bindings.convert(neff));
//        size_deck.textProperty().setValue(Integer.toString(size));
//    }
}
