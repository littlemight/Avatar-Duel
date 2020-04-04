package com.avatarduel.controller;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class CardController {
    @FXML
    private Label card_name, card_description, card_element, card_type;

    @FXML
    private ImageView card_image;

    @FXML
    private AnchorPane card_atr;

    private final Card card;

    public CardController(Card card) {
        this.card = card;
    }

    public void initialize() throws URISyntaxException, IOException {
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

        FXMLLoader loader;
        if (card instanceof Character) { // masih coba-coba
            loader = new FXMLLoader(AvatarDuel.class.getResource("card/view/CharacterAttribute.fxml"));
            GridPane gp = loader.load();
            gp.prefWidthProperty().bind(this.card_atr.widthProperty());
            gp.prefHeightProperty().bind(this.card_atr.heightProperty());

            this.card_atr.getChildren().add(gp);
            AttributeController c = loader.getController();
            if (c == null) {
                System.out.println("bad");
            }
            c.initialize(((Character) this.card).getAtk(), ((Character) this.card).getDef(), ((Character) this.card).getPower());
        }
        File file = new File(AvatarDuel.class.getResource(this.card.getIMGPath()).toURI());
        Image image = new Image(file.toURI().toString());
        this.card_image.setImage(image);
    }


}
