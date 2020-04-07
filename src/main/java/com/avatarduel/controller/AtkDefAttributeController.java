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

public class AtkDefAttributeController implements Initializable {
    @FXML
    HBox card_attribute_box;

    @FXML
    Label atk_text, def_text, pow_text;

    @FXML
    Label atk_value, def_value, pow_value;

    public void setAttribute(int atk, int def, int pow) {
        this.atk_value.setText(Integer.toString(atk));
        this.def_value.setText(Integer.toString(def));
        this.pow_value.setText(Integer.toString(pow));
    }

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

    public HBox getContent() {
        return this.card_attribute_box;
    }
}
