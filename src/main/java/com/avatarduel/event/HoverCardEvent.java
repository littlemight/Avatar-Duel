package com.avatarduel.event;

import com.avatarduel.model.card.Card;

public class HoverCardEvent implements Event {
    private Card card;

    public HoverCardEvent(Card card) {
        this.card = card;
    }

    @Override
    public Object getInfo() {
        return this.card;
    }
}
