package com.avatarduel.controller;

import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;
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

public class CardController implements Initializable {
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

    private final Card card;

    public CardController(Card card) {
        this.card = card;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.card_name.setText(this.card.getName());
        this.card_description.setText(this.card.getDescription());
        this.card_element.setText(this.card.getElement().toString());
        if (card instanceof Character) {
            this.card_type.setText("Character");
        } else if (card instanceof Aura) {
            this.card_type.setText("Aura");
        } else if (card instanceof Land) {
            this.card_type.setText("Land");
        }
        File file = null;
        try {
            file = new File(getClass().getResource("../" + this.card.getIMGPath()).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
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

        Circle back_logo = (Circle) card_back.getChildren().get(0);
        back_logo.radiusProperty().bind(card_box.widthProperty().multiply((double) 150 / 500));
        back_logo.layoutXProperty().bind(card_box.widthProperty().multiply(0.5));
        back_logo.layoutYProperty().bind(card_box.heightProperty().multiply(0.5));

        card_front.setVisible(false);
        card_back.setVisible(true);
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        // how to communicate with BoardController?
        card_box.setStyle("-fx-border-color: red");
        System.out.println("HOVERED: " + this.card.getName());
        card_front.setVisible(true);
        card_back.setVisible(false);
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        card_box.setStyle("-fx-border-color: black");
        card_front.setVisible(false);
        card_back.setVisible(true);
    }
}