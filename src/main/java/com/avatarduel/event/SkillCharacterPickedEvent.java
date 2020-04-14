package com.avatarduel.event;

import com.avatarduel.controller.SummonedCharacterController;

public class SkillCharacterPickedEvent implements Event {
    SummonedCharacterController info;

    public SkillCharacterPickedEvent(SummonedCharacterController info) {
        this.info = info;
    }

    @Override
    public Object getInfo() {
        return this.info;
    }
}
