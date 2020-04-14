package com.avatarduel.event;

import com.avatarduel.controller.CardController;
import com.avatarduel.model.card.SummonedSkill;

public class NewSkillCardPlaced implements Event {
    SummonedSkill info;

    public NewSkillCardPlaced(SummonedSkill info) {
        this.info = info;
    }

    @Override
    public Object getInfo() {
        return this.info;
    }
}
