package com.avatarduel.event;

import java.util.ArrayList;

import com.avatarduel.controller.SummonedCharacterController;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.SummonedCharacter;

/**
 * Event class for character card being selected action
 */
public class CharacterSelectedEvent implements Event {
    private SummonedCharacterController card;
    private Player player;

    /**
     * Constructor for CharacterSelectedEvent
     * @param card SummonedCharacterController reference object of the game
     * @param player player which created this event
     * @see SummonedCharacterController
     */
    public CharacterSelectedEvent(SummonedCharacterController card, Player player) {
        this.card = card;
        this.player = player;
    }

    /**
     * Getter for information contained in this object
     * @return an arraylist of object containing two items : controller to summonedcardcontroller and player_id
     */
    @Override
    public Object getInfo() {
        ArrayList<Object> tuple = new ArrayList<>();
        tuple.add(this.card);
        tuple.add(this.player);
        return tuple;
    }
}