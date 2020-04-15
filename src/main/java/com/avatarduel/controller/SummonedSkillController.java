package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.card.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class SummonedSkillController implements Initializable, Publisher, Subscriber {
    @FXML
    StackPane summoned_skill_box;

    @FXML
    VBox base_card_pane;

    @FXML
    Label description_text;

    @FXML
    HBox description_box;

    CardController base_card_controller;
    SummonedSkill summoned_skill;
    private int position;

    BoardChannel channel;
    CardChannel card_channel;

    public SummonedSkillController(BoardChannel channel) {
        this.channel = channel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        summoned_skill_box.prefHeightProperty().addListener(e -> {
            description_text.setFont(new Font(((double)65 / 500) * summoned_skill_box.getPrefHeight()));
        });
        base_card_pane.prefHeightProperty().bind(summoned_skill_box.prefHeightProperty());
        base_card_pane.prefWidthProperty().bind(summoned_skill_box.prefWidthProperty());
        description_box.prefHeightProperty().bind(summoned_skill_box.prefHeightProperty().multiply((double)(176) / 500));
    }

    public void setSummonedCharacter(SummonedSkill summoned_skill) {
        card_channel = new CardChannel();
        summoned_skill.setChannel(card_channel);
        card_channel.addSubscriber(summoned_skill, this);

        this.summoned_skill = summoned_skill;

        Skill base_card = (Skill) summoned_skill.getBaseCard();
        String description = "";
        if (base_card instanceof Aura) {
            description += "+ATK: " +  ((Aura) base_card).getDeltaAtk() + "\n";
            description += "+DEF: " + ((Aura) base_card).getDeltaDef() + "\n";
        } else if (base_card instanceof PowerUp) {
            description += "PWR UP";
        } else { // its a destroy card
            description += "DESTROY";
        }
        this.description_text.setText(description);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(this.channel));
        try {
            loader.load();
            base_card_controller = loader.getController();
            base_card_controller.setCard(base_card);
            StackPane base_card_box = base_card_controller.getContent();
            base_card_box.prefHeightProperty().bind(summoned_skill_box.prefHeightProperty());
            base_card_pane.getChildren().add(base_card_box);

            this.base_card_controller.setMouseTransparent(true);

            this.channel.addSubscriber(this, (Subscriber) this.channel.getMain());
            publish(new NewSummonedCardEvent(this.base_card_controller));
            publish(new NewSkillCardPlaced(this.summoned_skill));
        } catch (Exception e) {
            System.out.println("IN SUMMONEDCARD: " + e);
        }
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        base_card_controller.publish(new HoverCardEvent(this.summoned_skill.getBaseCard()));
        publish(new HoverSummonedCardEvent(this.summoned_skill));
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        base_card_controller.publish(new HoverCardEvent(EmptyCard.getInstance()));
        publish(new HoverSummonedCardEvent(null));
    }

    public void destroy() {
        ((Pane)summoned_skill_box.getParent()).getChildren().remove(summoned_skill_box);
    }
    
    public void setPosition(int position){
        this.position = position;
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }

    @Override
    public void onEvent(Event event) {
        this.destroy();
    }
}
