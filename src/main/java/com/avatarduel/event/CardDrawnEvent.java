package com.avatarduel.event;

import com.avatarduel.model.card.Card;

public class CardDrawnEvent implements Event {
    private Card card;

    public CardDrawnEvent(Card card) {
        this.card = card;
    }

    @Override
    public Object getInfo() {
        return this.card;
    }
}
