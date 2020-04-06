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
    HBox card_attribute;

    private EventChannel channel;
    private Card card;
    private StringProperty type;

//    public CardController(Card card) {
//        this.card = card;
//        this.type = new SimpleStringProperty("AIR");
//    }

    public CardController(EventChannel channel) {
        this.channel = channel;
        this.card = EmptyCard.getInstance();
//        this.card = card;
        this.type = new SimpleStringProperty("DEFAULT");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.card_name.textProperty().bind(card.getNameProperty());
            this.card_description.textProperty().bind(card.getDescriptionProperty());
            this.card_element.textProperty().bind(card.getElementProperty().asString());
            this.card_type.textProperty().bind(type);
        } catch (Exception e) {
            System.out.println("PUNTEN: " + e);
        }

//        this.card_name.setText(this.card.getName());
//        this.card_description.setText(this.card.getDescription());
//        this.card_element.setText(this.card.getElement().toString());
//
//        if (card instanceof Character) {
//            type.setValue("Character");
//            this.card_type.setText("Character");
//        } else if (card instanceof Aura) {
//            type.setValue("Aura");
//            this.card_type.setText("Aura");
//        } else if (card instanceof Land) {
//            type.setValue("Land");
//            this.card_type.setText("Land");
//        }

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
            card_name.setFont(new Font(0.05 * card_box.getWidth()));
            card_type.setFont(new Font(0.05 * card_box.getWidth()));
            card_element.setFont(new Font(0.05 * card_box.getWidth()));
            card_description.setFont(new Font(0.05 * card_box.getWidth()));
        });

        card_image.fitWidthProperty().bind(card_box.widthProperty().multiply(0.6));
        card_image.fitHeightProperty().bind(card_image.fitWidthProperty());
        card_bottom.prefHeightProperty().bind(card_box.heightProperty().multiply((double) 240 / 700));
        card_bottom.prefWidthProperty().bind(card_bottom.prefHeightProperty().multiply((double) 480 / 240));
        card_description.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.8));
        card_description.prefWidthProperty().bind(card_bottom.prefWidthProperty());
        card_attribute.prefHeightProperty().bind(card_bottom.prefHeightProperty().multiply(0.2));
        card_attribute.prefWidthProperty().bind(card_bottom.prefWidthProperty());

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

//        card_front.visibleProperty().bind(new SimpleBooleanProperty(this.card instanceof EmptyCard));

        Circle back_logo = (Circle) card_back.getChildren().get(0);
        back_logo.radiusProperty().bind(card_box.widthProperty().multiply((double) 150 / 500));
        back_logo.layoutXProperty().bind(card_box.widthProperty().multiply(0.5));
        back_logo.layoutYProperty().bind(card_box.heightProperty().multiply(0.5));

//        setClosed();
        setOpened();
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
        this.card = card;
        this.card_name.textProperty().bind(card.getNameProperty());
        this.card_description.textProperty().bind(card.getDescriptionProperty());
        this.card_element.textProperty().bind(card.getElementProperty().asString());
        this.card_type.textProperty().bind(type);
        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error: " + e);
        }
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        card_box.setStyle("-fx-border-color: red");
        System.out.println("HOVERED: " + this.card.getName());
        publish(this, new HoverCardEvent(this.card));
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        card_box.setStyle("-fx-border-color: black");
        publish(this, new HoverCardEvent(EmptyCard.getInstance()));
    }

    public StackPane getContent() {
        return this.card_box;
    }

    @Override
    public void onEvent(Event event) {
        setCard((Card)event.getInfo());
    }

    @Override
    public void publish(Publisher publisher, Event event) {
        this.channel.sendEvent(publisher, event);
    }
}