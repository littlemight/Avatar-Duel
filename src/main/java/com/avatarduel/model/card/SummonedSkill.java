package com.avatarduel.model.card;

public class SummonedSkill {
    private Skill card;
    private SummonedCharacter applied_to; // dia apply ke mana

    public SummonedSkill(Skill card) {
        this.card = card;
    }

    public void setAppliedTo(SummonedCharacter applied_to) {
        this.applied_to = applied_to;
        this.applied_to.addSkill(this.card);
    }
}
