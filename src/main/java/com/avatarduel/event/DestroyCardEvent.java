package com.avatarduel.event;

import com.avatarduel.model.card.Card;

public class DestroyCardEvent implements Event {
    private Card card;

    public DestroyCardEvent(Card card) {
        this.card = card;
    }

    @Override
    public Object getInfo() {
        return this.card;
    }
}
