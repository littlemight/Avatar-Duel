package com.avatarduel.model.card;

public class SummonedSkill implements Summoned{
    private Skill card;
    private SummonedCharacter applied_to; // dia apply ke mana

    public SummonedSkill(Skill card) {
        this.card = card;
        applied_to = null;
    }

    public void setAppliedTo(SummonedCharacter applied_to) {
        this.applied_to = applied_to;
        this.applied_to.addSkill(this.card);
    }

    public Card getBaseCard() {
        return this.card;
    }

    public SummonedCharacter getAppliedTo() {
        return this.applied_to;
    }
}
