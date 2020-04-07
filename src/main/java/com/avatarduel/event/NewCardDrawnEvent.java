package com.avatarduel.event;

import com.avatarduel.controller.CardController;

public class NewCardDrawnEvent implements Event {
    CardController new_card_controller;

    public NewCardDrawnEvent(CardController new_card_controller) {
        this.new_card_controller = new_card_controller;
    }

    @Override
    public Object getInfo() {
        return this.new_card_controller;
    }
}
