package com.avatarduel.event;

import com.avatarduel.model.Phase;

public class PhaseChangedEvent implements Event {
    private Phase cur_phase;

    public PhaseChangedEvent(Phase cur_phase) {
        this.cur_phase = cur_phase;
    }

    @Override
    public Object getInfo() {
        return this.cur_phase;
    }
}
