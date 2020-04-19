package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.card.EmptyCard;
import com.avatarduel.model.card.SummonedCharacter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for {@link SummonedCharacter} cards attribute information
 */
public class SummonedCharacterController implements Initializable, Publisher, Subscriber {
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
    private boolean is_selected;
    private int position;
    private int owner;
    BoardChannel channel;

    /**
     * Constructor for SummonedCharacterController
     * @param channel channel for publishing/subscribing events
     * @see PlayerFieldController
     */
    public SummonedCharacterController(BoardChannel channel) {
        this.channel = channel;
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the controller and board.
     * Sets {@code onMouseClicked} and {@code onMouseEntered} events
     * for {@link SkillCharacterPickedEvent} event.
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

        summoned_character_box.setOnMouseClicked(e -> {
            switch (this.channel.getPhase()) {
                case SKILLPICK:
                    publish(new SkillCharacterPickedEvent(this));
                    summoned_character_box.setCursor(Cursor.DEFAULT);
                    break;
                case MAIN:
                    if (this.channel.getPlayerID() == this.owner) {
                        rotateCharacter();
                    }
                    break;
                default:
                    break;
            }
        });
        summoned_character_box.setOnMouseEntered(e -> {
            this.onMouseEnter(e);
            switch (this.channel.getPhase()) {
                case SKILLPICK:
                    summoned_character_box.setCursor(Cursor.HAND);
                    break;
                default:
                    break;
            }
        });
        is_selected=false;
    }

    /**
     * Getter for {@code summoned_character}
     * @return controller's {@code summoned_character} 
     */
    public SummonedCharacter getSummonedCharacter() {
        return this.summoned_character;
    }

    /**
     * Rotates the summoned character card's graphic
     */
    private void rotateCharacter() {
        this.summoned_character.rotate();
        this.base_card_controller.rotate();
    }

    /**
     * Sets the summoned character card in control.
     * Loads information from the card and sets corresponding
     * resources of the card.
     * @param summoned_character card to be controlled.
     */
    public void setSummonedCharacter(SummonedCharacter summoned_character) {
        channel.addSubscriber(summoned_character, this);
        this.summoned_character = summoned_character;

        this.net_atk.textProperty().bind(summoned_character.getNetAtkProperty().asString());
        this.net_def.textProperty().bind(summoned_character.getNetDefProperty().asString());

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

    /**
     * Sets the owner of the card.
     * @param owner owner of the card (player id)
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Event handler for {@link HoverSummonedCardEvent} to the card in hand.
     * @param mouseEvent mouse events captured
     */
    public void onMouseEnter(MouseEvent mouseEvent) {
        base_card_controller.publish(new HoverCardEvent(this.summoned_character.getBaseCard()));
        publish(new HoverSummonedCardEvent(this.summoned_character));
    }

    /**
     * Event handler for {@link HoverSummonedCardEvent} to the card in hand.
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
        ((Pane)summoned_character_box.getParent()).getChildren().remove(summoned_character_box);
    }

    /**
     * Toggle card selected effect
     */
    public void toggleSelected(){
        if (is_selected){
            this.base_card_pane.setEffect(null);
            is_selected=false;
        }else{
            this.base_card_pane.setEffect(new DropShadow(50f, Color.DODGERBLUE));
            is_selected=true;
        }
    }

    /**
     * Sets hinting effect to the summoned card
     * @param is_hinting true to activate effect
     */
    public void setHinting(boolean is_hinting){
        if (is_hinting){
            this.base_card_pane.setEffect(new DropShadow(50f, Color.CRIMSON));
        }else{
            this.base_card_pane.setEffect(null);
        }
    }

    /**
     * Sets character position in field/zone
     * @param position column position of the card
     */
    public void setPosition(int position){
        this.position = position;
    }

    /**
     * Getter for {@code position} in field/zone
     * @return controller's {@code position}
     */
    public int getPosition(){
        return this.position;
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
