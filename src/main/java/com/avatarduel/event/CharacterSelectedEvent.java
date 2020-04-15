package com.avatarduel.event;

import com.avatarduel.controller.SummonedCharacterController;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.SummonedCharacter;

public class CharacterSelectedEvent implements Event {
    private SummonedCharacterController card;

    public CharacterSelectedEvent(SummonedCharacterController card) {
        this.card = card;
    }

    @Override
    public Object getInfo() {
        return this.card;
    }
}