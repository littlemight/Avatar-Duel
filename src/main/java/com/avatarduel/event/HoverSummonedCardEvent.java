package com.avatarduel.event;

import com.avatarduel.controller.CardController;
import com.avatarduel.model.card.Summoned;

public class HoverSummonedCardEvent implements Event {
    Summoned summoned_card;

    public HoverSummonedCardEvent(Summoned summoned_card) {
        this.summoned_card = summoned_card;
    }

    public Object getInfo() {
        return this.summoned_card;
    }
}
