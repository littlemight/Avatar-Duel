package com.avatarduel.event;

import com.avatarduel.model.card.Summoned;

public class DestroyCard implements Event {
    private Summoned summoned;

    public DestroyCard(Summoned summoned) {
        this.summoned = summoned;
    }

    @Override
    public Object getInfo() {
        return this.summoned;
    }
}
