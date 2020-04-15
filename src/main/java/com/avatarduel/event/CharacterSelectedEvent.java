package com.avatarduel.event;

import java.util.ArrayList;

import com.avatarduel.controller.SummonedCharacterController;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.SummonedCharacter;

public class CharacterSelectedEvent implements Event {
    private SummonedCharacterController card;
    private Player player;

    public CharacterSelectedEvent(SummonedCharacterController card, Player player) {
        this.card = card;
        this.player = player;
    }

    @Override
    public Object getInfo() {
        ArrayList<Object> tuple = new ArrayList<>();
        tuple.add(this.card);
        tuple.add(this.player);
        return tuple;
    }
}