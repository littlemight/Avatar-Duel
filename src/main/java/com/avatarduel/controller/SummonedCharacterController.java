package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.card.EmptyCard;
import com.avatarduel.model.card.SummonedCharacter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class SummonedCharacterController implements Initializable, Publisher {
    @FXML
    StackPane summoned_character_box;

    @FXML
    VBox base_card_pane;

    @FXML
    Label net_atk, net_def;

    @FXML
    Label net_atk_text, net_def_text;

    @FXML
    HBox net_atk_box, net_def_box;

    CardController base_card_controller;
    SummonedCharacter summoned_character;

    EventChannel channel;

    public SummonedCharacterController(EventChannel channel) {
        this.channel = channel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        summoned_character_box.prefHeightProperty().addListener(e -> {
            net_atk.setFont(new Font(((double)60 / 500) * summoned_character_box.getPrefHeight()));
            net_def.setFont(new Font(((double)60 / 500) * summoned_character_box.getPrefHeight()));
            net_atk_text.setFont(new Font(((double)60 / 500) * summoned_character_box.getPrefHeight()));
            net_def_text.setFont(new Font(((double)60 / 500) * summoned_character_box.getPrefHeight()));
        });
        base_card_pane.prefHeightProperty().bind(summoned_character_box.prefHeightProperty());
        base_card_pane.prefWidthProperty().bind(summoned_character_box.prefWidthProperty());
        net_atk_box.prefHeightProperty().bind(summoned_character_box.prefHeightProperty().multiply((double)(88) / 500));
        net_def_box.prefHeightProperty().bind(summoned_character_box.prefHeightProperty().multiply((double)(88) / 500));

        // Should only be enabled on Main Phase
        summoned_character_box.setOnMouseClicked(e -> rotateCharacter());
    }

    /**
     * Rotates the character card
     */
    private void rotateCharacter() {
        this.summoned_character.rotate();
        this.base_card_controller.rotate();
    }

    public void setSummonedCharacter(SummonedCharacter summoned_character) {
        this.summoned_character = summoned_character;

        this.net_atk.setText(Integer.toString(summoned_character.getNetAtk()));
        this.net_def.setText(Integer.toString(summoned_character.getNetDef()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(this.channel));
        try {
            loader.load();
            base_card_controller = loader.getController();
            base_card_controller.setCard(summoned_character.getBaseCard());
            StackPane base_card_box = base_card_controller.getContent();
            base_card_box.prefHeightProperty().bind(summoned_character_box.prefHeightProperty());
            base_card_pane.getChildren().add(base_card_box);

            this.base_card_controller.setMouseTransparent(true);

            this.channel.addSubscriber(this, (Subscriber) this.channel.getMain());
            publish(new NewSummonedCardEvent(this.base_card_controller));
        } catch (Exception e) {
            System.out.println("IN SUMMONEDCARD: " + e);
        }
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        this.channel.sendEvent(base_card_controller, new HoverCardEvent(this.summoned_character.getBaseCard()));
        publish(new HoverSummonedCardEvent(this.summoned_character));
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        this.channel.sendEvent(base_card_controller, new HoverCardEvent(EmptyCard.getInstance()));
        publish(new HoverSummonedCardEvent(null));
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
