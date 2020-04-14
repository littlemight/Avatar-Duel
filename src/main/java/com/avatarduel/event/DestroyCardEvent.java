package com.avatarduel.event;

import com.avatarduel.model.card.Summoned;

public class DestroyCardEvent implements Event {
    private Summoned summoned;

    public DestroyCardEvent(Summoned summoned) {
        this.summoned = summoned;
    }

    @Override
    public Object getInfo() {
        return this.summoned;
    }
}
