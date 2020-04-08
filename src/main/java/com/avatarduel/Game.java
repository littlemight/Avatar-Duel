package com.avatarduel;

import com.avatarduel.event.*;
import com.avatarduel.model.*;
import com.avatarduel.model.card.*;

public class Game {
    // initialize player components
    private Player player1, player2;
    private Dealer dealer;
    private BoardChannel boardChannel;
    // defined game phases
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN1, Phase.BATTLE, Phase.MAIN2, Phase.END};
    int phase_id = 0;

    /**
     * Draw function
     * Checks :
     * Resets mana
     * Buka kartu
     * "ngebuka kartu current player"
     * ensure balance (gak aura semua, gak land semua, dll). Constraints dsb dsb
     * Draw one card.
     * @param player : The player which going to draw phase
     */
    public void draw(Player player) {
        // initialize variable to hold count
        int landCardCount = 0, skillCardCount = 0, charaCardCount = 0, destroyCardCount = 0;
        int powerUpCardCount = 0, auraCardCount = 0, totalCardCount;
        Card card;
        boolean isSafe = false;
        // loop until safe
        while (!isSafe) {
            // traverse each card in player hand
            for (Card c : player.getHand()) {
                if (c instanceof Aura) {
                    auraCardCount++;
                } else if (c instanceof com.avatarduel.model.card.Character) {
                    charaCardCount++;
                } else if (c instanceof Destroy) {
                    destroyCardCount++;
                } else if (c instanceof Land) {
                    landCardCount++;
                } else if (c instanceof PowerUp) {
                    powerUpCardCount++;
                } else if (c instanceof Skill) {
                    skillCardCount++;
                }
            }
            // check if no card takes it all
            totalCardCount = player.getHand().size();
            isSafe = auraCardCount != totalCardCount && charaCardCount != totalCardCount && destroyCardCount != totalCardCount && landCardCount != totalCardCount && powerUpCardCount != totalCardCount && skillCardCount != totalCardCount;
            // draw new card if the status is not safe
            player.getHand().remove(0);
            card = player.drawCard();
        }
        // when safe, reset player power
        player.resetPower();

    }
    // main

    // battle

    // endturn

}
