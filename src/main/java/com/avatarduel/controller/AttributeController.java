package com.avatarduel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AttributeController {
    @FXML
    private Label card_atk, card_def, card_power;

    public void initialize(int card_atk, int card_def, int card_power) {
        this.card_atk.setText(Integer.toString(card_atk));
        this.card_def.setText(Integer.toString(card_def));
        this.card_power.setText(Integer.toString(card_power));
    }
}
