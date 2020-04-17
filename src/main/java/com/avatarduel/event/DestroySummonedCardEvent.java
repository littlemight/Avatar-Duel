package com.avatarduel.event;

import com.avatarduel.model.card.Summoned;

public class DestroySummonedCardEvent implements Event {
    private Summoned summoned;

    public DestroySummonedCardEvent(Summoned summoned) {
        this.summoned = summoned;
    }

    @Override
    public Object getInfo() {
        return this.summoned;
    }
}
