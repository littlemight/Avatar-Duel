package com.avatarduel.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for Character and Aura cards attribute information
 */
public class AtkDefAttributeController implements Initializable {
    @FXML
    HBox card_attribute_box;

    @FXML
    Label atk_text, def_text, pow_text;

    @FXML
    Label atk_value, def_value, pow_value;

    /**
     * Constructor for AtkDefAttributeController
     * @param atk attack attribute of the card
     * @param def defense attribute of the card
     * @param pow power attribute of the card
     * @see CardController
     */
    public void setAttribute(int atk, int def, int pow) {
        this.atk_value.setText(Integer.toString(atk));
        this.def_value.setText(Integer.toString(def));
        this.pow_value.setText(Integer.toString(pow));
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the controller values (atk, def, pow).
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
        card_attribute_box.prefWidthProperty().bind(card_attribute_box.prefHeightProperty().multiply(8));
        card_attribute_box.heightProperty().addListener(e -> {
            atk_value.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
            def_value.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
            pow_value.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));

            atk_text.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
            def_text.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
            pow_text.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
        });
        card_attribute_box.
                paddingProperty().
                bind(
                        Bindings.createObjectBinding(() -> new Insets(
                            card_attribute_box.prefHeightProperty().multiply((double) 5 / 60).getValue()),
                            card_attribute_box.prefHeightProperty()
                        )
                );
        card_attribute_box.spacingProperty().bind(card_attribute_box.prefHeightProperty().multiply((double) 5 / 60));
    }

    /**
     * Getter for {@code card_attribute_box}
     * @return controller's {@code card_attribute_box} 
     */
    public HBox getContent() {
        return this.card_attribute_box;
    }
}
