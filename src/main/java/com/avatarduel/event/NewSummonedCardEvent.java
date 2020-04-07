package com.avatarduel.event;

import com.avatarduel.controller.CardController;

public class NewSummonedCardEvent implements Event {
    CardController info;

    public NewSummonedCardEvent(CardController info) {
        this.info = info;
    }

    @Override
    public Object getInfo() {
        return this.info;
    }
}
