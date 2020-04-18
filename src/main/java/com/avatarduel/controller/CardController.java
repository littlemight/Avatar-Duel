package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.ConfirmBox;
import com.avatarduel.model.Element;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;

import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for Card class
 */
public class CardController implements Initializable, Subscriber, Publisher {
    @FXML
    Label card_name, card_type, card_description;

    @FXML
    ImageView card_element;

    @FXML
    Label type_left_bracket, type_right_bracket;

    @FXML
    ImageView card_image;

    @FXML
    StackPane card_box;

    @FXML
    VBox card_front;

    @FXML
    Pane card_back;

    @FXML
    VBox card_bottom;

    @FXML
    Pane card_attribute_pane;

    HBox card_attribute_box;

    private BoardChannel channel;
    private Card card;

    /**
     * Constructor for CardController
     * @param channel channel for publishing/subscribing events
     * @see PlayerFieldController
     * @see SummonedCharacterController
     * @see SummonedSkillController
     * @see BoardController
     */
    public CardController(BoardChannel channel) {
        this.channel = channel;
        this.card = EmptyCard.getInstance();
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the card in control.
     * Sets onMouseClicked event on {@code card_front} for discard event.
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
        // Maintains aspect ratio
        this.card_box.prefWidthProperty().bind(this.card_box.prefHeightProperty().multiply((double) 5 / 7));
        this.card_front.spacingProperty().bind(this.card_box.prefHeightProperty().multiply((double) 5 / 700));
        this.card_front.
                paddingProperty().
                bind(
                        Bindings.createObjectBinding(() -> new Insets(
                                        card_front.prefHeightProperty().multiply((double) 10 / 700).getValue()), card_front.prefHeightProperty()
                        )
        );

        this.card_description.
                paddingProperty().
                bind(
                        Bindings.createObjectBinding(() -> new Insets(
                                card_front.prefHeightProperty().multiply((double) 5 / 700).getValue()), card_front.prefHeightProperty()
                        )
        );

        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);
        card_box.widthProperty().addListener(e -> {
            card_name.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            card_type.setFont(new Font(((double)24 / 500) * card_box.getWidth()));
            type_left_bracket.setFont(new Font(((double)24 / 500) * card_box.getWidth()));
            type_right_bracket.setFont(new Font(((double)24 / 500) * card_box.getWidth()));
            card_description.setFont(new Font(((double)22 / 500) * card_box.getWidth()));
        });

        card_name.prefHeightProperty().bind(card_box.heightProperty().multiply((double) 60 / 700));
        card_image.fitWidthProperty().bind(card_box.widthProperty().multiply((double) 400 / 500));
        card_image.fitHeightProperty().bind(card_image.fitWidthProperty());
        card_element.fitWidthProperty().bind(card_box.widthProperty().multiply((double) 60 / 500));
        card_element.fitHeightProperty().bind(card_element.fitWidthProperty());
        card_bottom.prefHeightProperty().bind(card_box.heightProperty().multiply((double) 170 / 700));
        card_bottom.prefWidthProperty().bind(card_bottom.prefHeightProperty().multiply((double) 460 / 170));
        card_description.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.7));
        card_description.prefWidthProperty().bind(card_bottom.prefWidthProperty());
        card_attribute_pane.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.3));
        card_attribute_pane.prefWidthProperty().bind(card_bottom.prefWidthProperty());

        card_front.
                prefHeightProperty().
                bind(card_box.heightProperty());
        card_front.
                prefWidthProperty().
                bind(card_box.widthProperty());

        card_back.
                prefHeightProperty().
                bind(card_box.heightProperty());
        card_back.
                prefWidthProperty().
                bind(card_box.widthProperty());


        Circle back_logo = (Circle) card_back.getChildren().get(0);
        back_logo.radiusProperty().bind(card_box.widthProperty().multiply((double) 150 / 500));
        back_logo.layoutXProperty().bind(card_box.widthProperty().multiply(0.5));
        back_logo.layoutYProperty().bind(card_box.heightProperty().multiply(0.5));

        setClosed();

        this.card_front.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                switch (this.channel.getPhase()) {
                    case DISCARD:
                        if (ConfirmBox.display(e.getScreenX(), e.getScreenY(),
                                "Discard " + this.card.getName(),
                                "Discarding [" + this.card.getName() + "]")
                        ) {
                            this.destroy();
                            publish(new DestroyCardEvent(this.card));
                        }
                        break;
                    default:
                        break;

                }
            }
        });
    }

    /**
     * Sets {@code card_box} to become transparent to mouse events.
     * Useful when coupled with SummonedCharacter.fxml/SummonedSkill.fxml
     * @param value true or false
     */
    public void setMouseTransparent(boolean value) {
        this.card_box.setMouseTransparent(value);
    }

    /**
     * Checks if the card is opened.
     * @return true if visible
     */
    public boolean isOpened() {
        return card_front.isVisible();
    }

    /**
     * Checks if the card is closed.
     * @return true if closed
     */
    public boolean isClosed() {
        return card_back.isVisible();
    }

    /**
     * Sets the card to be closed.
     */
    public void setClosed() {
        card_front.setVisible(false);
        card_back.setVisible(true);
    }

    /**
     * Sets the card to be opened.
     */
    public void setOpened() {
        card_front.setVisible(true);
        card_back.setVisible(false);
    }

    /**
     * Flips the card (front and back).
     */
    public void flipCard() {
        if (card_front.isVisible()) {
            setClosed();
        } else {
            setOpened();
        }
    }

    /**
     * Sets the card in control.
     * Loads information from the card and sets corresponding
     * resources of the card.
     * @param card card to be controlled.
     */
    public void setCard(Card card) {
        if (card instanceof EmptyCard) {
            setClosed();
            return;
        }

        setOpened();
        this.card = card;
        this.card_name.setText(card.getName());
        this.card_description.setText(card.getDescription());

        if (card.getElement() == Element.AIR) {
            card_front.setStyle("-fx-background-color: #FCE5B0; -fx-border-color: #91ccef;");
            card_name.setStyle("-fx-background-color: #B46826; -fx-text-fill: #fdfbc8;");
            card_type.setStyle("-fx-text-fill: #967862");
            card_description.setStyle("-fx-background-color: #EBF5EE");
        }
        else if(card.getElement() == Element.WATER){
            card_front.setStyle("-fx-background-color: #80B6E3; -fx-border-color: #E6C38C;");
            card_name.setStyle("-fx-background-color: #1A4C9C; -fx-text-fill: #fdfbc8;");
            card_type.setStyle("-fx-text-fill: #173158");
            card_description.setStyle("-fx-background-color: #C4E0F9;");
        }
        else if(card.getElement() == Element.FIRE){
            card_front.setStyle("-fx-background-color: #BA5D22; -fx-border-color: #F18517;");
            card_name.setStyle("-fx-background-color: #841E00; -fx-text-fill: #efeab9;");
            card_type.setStyle("-fx-text-fill: #ffc12f");
            card_description.setStyle("-fx-background-color: #DDA448;");
        }
        else if(card.getElement() == Element.EARTH){
            card_front.setStyle("-fx-background-color: #4C4A29; -fx-border-color: EE9B44;");
            card_name.setStyle("-fx-background-color: #2F2F1B; -fx-text-fill:#efeab9;");
            card_type.setStyle("-fx-text-fill: #f0cd8c");
            card_description.setStyle("-fx-background-color: #F1F7ED;");
        }
        else if(card.getElement() == Element.ENERGY){
            card_front.setStyle("-fx-background-color: #d796ea; -fx-border-color: #3C215F;");
            card_name.setStyle("-fx-background-color: #69499C; -fx-text-fill:#fdfbc8;");
            card_type.setStyle("-fx-text-fill: #921299");
            card_description.setStyle("-fx-background-color: #F0E6EF;");
        }
        String type;
        this.card_attribute_pane.getChildren().clear();
        FXMLLoader attribute_loader;
        try {
            if (card instanceof Character) {
                type = "CHARACTER";
                attribute_loader = new FXMLLoader(getClass().getResource("../view/CharacterAttribute.fxml"));
                card_attribute_box = attribute_loader.load();
                AtkDefAttributeController controller = attribute_loader.getController();
                controller.setAttribute(((Character)this.card).getAtk(), ((Character)this.card).getDef(), ((Character)this.card).getPower());
            } else if (card instanceof Land) {
                type = "LAND";
            } else {
                if (card instanceof Aura) {
                    type = "AURA";
                    attribute_loader = new FXMLLoader(getClass().getResource("../view/AuraAttribute.fxml"));
                    card_attribute_box = attribute_loader.load();
                    AtkDefAttributeController controller = attribute_loader.getController();
                    controller.setAttribute(((Aura)this.card).getDeltaAtk(), ((Aura)this.card).getDeltaDef(), ((Aura)this.card).getPower());
                } else if (card instanceof PowerUp) {
                    type = "POWER UP";
                    attribute_loader = new FXMLLoader(getClass().getResource("../view/OtherAttribute.fxml"));
                    card_attribute_box = attribute_loader.load();
                    OtherAttributeController controller = attribute_loader.getController();
                    controller.setAttribute("Powers up a character", ((Skill)this.card).getPower());
                } else {
                    type = "DESTROY";
                    attribute_loader = new FXMLLoader(getClass().getResource("../view/OtherAttribute.fxml"));
                    card_attribute_box = attribute_loader.load();
                    OtherAttributeController controller = attribute_loader.getController();
                    controller.setAttribute("Destroys a character card", ((Skill)this.card).getPower());
                }
            }
            if (!(card instanceof Land)) {
                card_description.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.7));
                card_attribute_pane.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.3));

                this.card_attribute_box.prefHeightProperty().bind(this.card_attribute_pane.prefHeightProperty());
                this.card_attribute_box.prefWidthProperty().bind(this.card_attribute_pane.prefWidthProperty());
                this.card_attribute_pane.getChildren().add(card_attribute_box);
            } else {
                card_description.prefHeightProperty().bind(card_bottom.prefHeightProperty());
                card_attribute_pane.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0));
            }
            this.card_type.setText(type);
        } catch (Exception e) {
            System.out.println("LOADING ATTRIBUTE: " + e);
        }



        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);

        try {
            file = new File(getClass().getResource("../" + this.card.getElement().toString()).toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        image = new Image(file.toURI().toString());
        this.card_element.setImage(image);
    }

    /**
     * Event handler for {@link HoverCardEvent} to the card in hand.
     * @param mouseEvent mouse events captured
     */
    public void onMouseEnter(MouseEvent mouseEvent) {
        if (isOpened()) {
            System.out.println("HOVERED: " + this.card.getName());
            zoomInTransition();
            publish(new HoverCardEvent(this.card));
        } else {
            // do nothing lmao
            System.out.println("HOVERED: EMPTY");
        }
    }

    /**
     * Event handler for {@link HoverCardEvent} to the card in hand.
     * @param mouseEvent mouse events captured
     */
    public void onMouseExit(MouseEvent mouseEvent) {
        if (isOpened()) {
            zoomOutTransition();
            publish(new HoverCardEvent(EmptyCard.getInstance()));
        }
    }

    /**
     * Animate card to zoom in when mouse hovering the card.
     */
    public void zoomInTransition(){
        ScaleTransition zoom_card = new ScaleTransition(Duration.millis(250), this.card_box);
        zoom_card.setFromX(1);
        zoom_card.setFromY(1);
        zoom_card.setToX(1.2);
        zoom_card.setToY(1.2);
        zoom_card.play();
    }
    
    /**
     * Animate card to zoom out when mouse finished hovering the card.
     */
    public void zoomOutTransition(){
        ScaleTransition zoom_card = new ScaleTransition(Duration.millis(250), this.card_box);
        zoom_card.setFromX(1.2);
        zoom_card.setFromY(1.2);
        zoom_card.setToX(1);
        zoom_card.setToY(1);
        zoom_card.play();
    }

    /**
     * Getter for {@code card_front}
     * @return controller's {@code card_front} 
     */
    public VBox getCardFront() {
        return this.card_front;
    }

    /**
     * Getter for {@code card_box}
     * @return controller's {@code card_box} 
     */
    public StackPane getContent() {
        return this.card_box;
    }

    /**
     * Getter for {@code card}
     * @return controller's {@code card} 
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Sets hinting effect to the card
     * @param is_hinting true to activate effect
     */
    public void setHinting(boolean is_hinting){
        if (is_hinting){
            this.card_front.setEffect(new DropShadow(20f, Color.PALEGREEN));
        }else{
            this.card_front.setEffect(null);
        }
    }

    /**
     * Destroys card's graphic.
     */
    public void destroy() {
        ((Pane)card_box.getParent()).getChildren().remove(card_box);
    }

    /**
     * Implemented from {@link Subscriber} to listen from {@link BoardChannel}.
     * @param event event sent from {@link BoardChannel}
     * @see HoverCardEvent
     */
    @Override
    public void onEvent(Event event) {
        if (event instanceof HoverCardEvent) {
            setCard((Card)event.getInfo());
        }
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
     * Rotates card's graphic by 90 degrees.
     */
    public void rotate() {
        if (card_box.getRotate() == 0) {
            card_box.setRotate(90);
        } else {
            card_box.setRotate(0);
        }
    }
}