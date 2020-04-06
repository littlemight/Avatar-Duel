package com.avatarduel.controller;

import com.avatarduel.event.EventChannel;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.EmptyCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
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
        System.out.println(hover_card_controller);
        channel.addSubscriber(card_controller, hover_card_controller);
        StackPane card_box = card_controller.getContent();
        card_box.prefWidthProperty().bind(temp_card_book.prefWidthProperty().divide(5));
        card_box.prefHeightProperty().bind(card_box.prefWidthProperty().multiply(1.4));
        temp_card_book.getChildren().add(card_box);
    }
}
