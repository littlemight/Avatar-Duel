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

public class OtherAttributeController implements Initializable {
    @FXML
    HBox card_attribute_box;

    @FXML
    Label desc, pow;

    public void setAttribute(String desc, int pow) {
        this.desc.setText(desc);
        this.pow.setText(Integer.toString(pow));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        card_attribute_box.heightProperty().addListener(e -> {
            desc.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
            pow.setFont(new Font(((double) 30 / 60) * card_attribute_box.getHeight()));
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
