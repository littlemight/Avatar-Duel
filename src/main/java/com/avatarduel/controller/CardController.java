package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable, Subscriber, Publisher {
    @FXML
    Label card_name, card_type, card_element, card_description;

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

    private EventChannel channel;
    private Card card;


    public CardController(EventChannel channel) {
        this.channel = channel;
        this.card = EmptyCard.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Maintains aspect ratio
        this.card_box.prefWidthProperty().bind(this.card_box.prefHeightProperty().multiply((double) 5 / 7));
        this.card_front.spacingProperty().bind(this.card_box.prefHeightProperty().multiply((double) 10 / 700));

        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error: " + e);
        }
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);
        card_box.widthProperty().addListener(e -> {
            card_name.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            card_type.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            type_left_bracket.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            type_right_bracket.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            card_type.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            card_element.setFont(new Font(((double)30 / 500) * card_box.getWidth()));
            card_description.setFont(new Font(((double)24 / 500) * card_box.getWidth()));
        });

        card_image.
                fitWidthProperty().bind(card_box.widthProperty().multiply(0.6));
        card_image.fitHeightProperty().bind(card_image.fitWidthProperty());
        card_bottom.prefHeightProperty().bind(card_box.heightProperty().multiply((double) 240 / 700));
        card_bottom.prefWidthProperty().bind(card_bottom.prefHeightProperty().multiply((double) 480 / 240));
        card_description.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.8));
        card_description.prefWidthProperty().bind(card_bottom.prefWidthProperty());
        card_attribute_pane.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.2));
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
    }

    public boolean isOpened() {
        return card_front.isVisible();
    }

    public boolean isClosed() {
        return card_back.isVisible();
    }

    public void setClosed() {
        card_front.setVisible(false);
        card_back.setVisible(true);
    }

    public void setOpened() {
        card_front.setVisible(true);
        card_back.setVisible(false);
    }

    public void flipCard() {
        if (card_front.isVisible()) {
            setClosed();
        } else {
            setOpened();
        }
    }

    public void setCard(Card card) {
        if (card instanceof EmptyCard) {
            setClosed();
            return;
        }

        setOpened();
        this.card = card;
        this.card_name.textProperty().bind(card.getNameProperty());
        this.card_description.textProperty().bind(card.getDescriptionProperty());
        this.card_element.textProperty().bind(card.getElementProperty().asString());

        String type;
        if (card instanceof Character) {
            type = "CHARACTER";
        } else if (card instanceof Land) {
            type = "LAND";
        } else {
            // harusnya type = SKILL aja sih
            if (card instanceof Aura) {
                type = "AURA";
            } else if (card instanceof PowerUp) {
                type = "POWER UP";
            } else {
                type = "DESTROY";
            }
        }
        this.card_type.setText(type);

        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        if (isOpened()) {
            System.out.println("HOVERED: " + this.card.getName());
            publish(new HoverCardEvent(this.card));
        } else {
            // do nothing lmao
            System.out.println("HOVERED: EMPTY");
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        card_box.setStyle("-fx-border-color: black");
        if (isOpened()) {
            publish(new HoverCardEvent(EmptyCard.getInstance()));
        }
    }

    public StackPane getContent() {
        return this.card_box;
    }

    @Override
    public void onEvent(Event event) {
        setCard((Card)event.getInfo());
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}