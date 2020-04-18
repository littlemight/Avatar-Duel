package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.ConfirmBox;
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

/**
 * Controller class for {@link SummonedSkill} cards attribute information
 */
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
    private int owner;

    BoardChannel channel;

    /**
     * Constructor for SummonedSkillController
     * @param channel channel for publishing/subscribing events
     * @see PlayerFieldController
     */
    public SummonedSkillController(BoardChannel channel) {
        this.channel = channel;
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the controller and board.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     * @see Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        summoned_skill_box.prefHeightProperty().addListener(e -> {
            description_text.setFont(new Font(((double)65 / 500) * summoned_skill_box.getPrefHeight()));
        });
        base_card_pane.prefHeightProperty().bind(summoned_skill_box.prefHeightProperty());
        base_card_pane.prefWidthProperty().bind(summoned_skill_box.prefWidthProperty());
        description_box.prefHeightProperty().bind(summoned_skill_box.prefHeightProperty().multiply((double)(176) / 500));
    }

    /**
     * Sets the summoned skill card in control.
     * Loads information from the card and sets corresponding resources of the card.
     * Publish {@link NewSummonedCardEvent} and {@link NewSkillCardPlaced}.
     * Sets {@code onMouseClicked} events to remove card.
     * @param summoned_skill card to be controlled.
     */
    public void setSummonedCharacter(SummonedSkill summoned_skill) {
        summoned_skill.setChannel(channel);
        channel.addSubscriber(summoned_skill, this);

        this.summoned_skill = summoned_skill;

        Skill base_card = (Skill) summoned_skill.getBaseCard();
        String description = "";
        if (base_card instanceof Aura) {
            description += "+ATK: " +  ((Aura) base_card).getDeltaAtk() + "\n";
            description += "+DEF: " + ((Aura) base_card).getDeltaDef() + "\n";
        } else if (base_card instanceof PowerUp) {
            description += "PWR UP";
        } else {
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

            this.summoned_skill_box.setOnMouseClicked(e -> {
                switch (this.channel.getPhase()) {
                    case MAIN:
                        if (e.getClickCount() == 2) {
                            if (ConfirmBox.display(e.getScreenX(), e.getScreenY(),
                            "Discard " + this.summoned_skill.getBaseCard().getName(),
                        "Attached to [" + this.summoned_skill.getAppliedTo().getBaseCard().getName() + "]")
                            ) {
                                this.summoned_skill.removeCard();
                            }
                        }
                        break;
                    default:
                        break;
                }
            });
        } catch (Exception e) {
            System.out.println("IN SUMMONEDCARD: " + e);
        }
    }

    /**
     * Sets the owner of the card.
     * @param owner owner of the card (player id)
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Event handler for {@link HoverSummonedCardEvent} to the card in field/zone.
     * @param mouseEvent mouse events captured
     */
    public void onMouseEnter(MouseEvent mouseEvent) {
        base_card_controller.publish(new HoverCardEvent(this.summoned_skill.getBaseCard()));
        publish(new HoverSummonedCardEvent(this.summoned_skill));
    }

    /**
     * Event handler for {@link HoverSummonedCardEvent} to the card in field/zone.
     * @param mouseEvent mouse events captured
     */
    public void onMouseExit(MouseEvent mouseEvent) {
        base_card_controller.publish(new HoverCardEvent(EmptyCard.getInstance()));
        publish(new HoverSummonedCardEvent(null));
    }

    /**
     * Destroys summoned card's graphic.
     */
    public void destroy() {
        ((Pane)summoned_skill_box.getParent()).getChildren().remove(summoned_skill_box);
    }
    
    /**
     * Sets character position in field/zone
     * @param position column position of the card
     */
    public void setPosition(int position){
        this.position = position;
    }

    /**
     * Implemented from {@link Publisher} to publish to {@link BoardChannel}.
     * @param event event sent to {@link BoardChannel}
     */
    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }

    /**
     * Implemented from {@link Subscriber} to listen from {@link BoardChannel}.
     * @param event event sent from {@link BoardChannel}
     * @see DestroySummonedCardEvent
     */
    @Override
    public void onEvent(Event event) {
        if (event instanceof DestroySummonedCardEvent){
            this.destroy();
            this.channel.removeComponent(this);
        }
    }
}
