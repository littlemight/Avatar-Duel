package com.avatarduel;

import com.avatarduel.controller.BoardController;
import com.avatarduel.event.*;
import com.avatarduel.model.*;
import com.avatarduel.model.card.*;

import javafx.geometry.Pos;

public class Game implements Publisher{
    
    // components
    private Player players[];
    int cur_player;
    // defined game phases
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END};
    int phase_id = 0;

    // initialize
    public Game(Player p1, Player p2) {
        this.players = new Player[2];
        this.players[0] = p1;
        this.players[1] = p2;
    }

    public void stageController(Phase phase){
        if (phase == Phase.DRAW){
            this.draw();
        } else if (phase == Phase.MAIN){
            
        } else if (phase == Phase.BATTLE){
            this.battleStage();
        } else if (phase == Phase.END){
            this.endStage();
        } else {
            System.out.println("IN STAGE CONTROLLER" + phase);
        }
    }

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
    public void draw() {
        Player player = this.players[cur_player];
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
    public void battleStage(){
        // TODO: publish ke board_controller udah masuk ke phase battle
    }

    public void solveBattle(SummonedCharacter cur_player_card, SummonedCharacter enemy_player_card){
        if (cur_player_card.getPosition()==Position.ATTACK){
            if (cur_player_card.getCombatValue() > enemy_player_card.getCombatValue()){
                enemy_player_card.removeCard();
                // TODO: publish kartu yang di remove
                if (enemy_player_card.getPosition()==Position.ATTACK){
                    this.players[(cur_player+1)%2].decreaseHealth(cur_player_card.getCombatValue()-enemy_player_card.getCombatValue());
                    if (this.players[(cur_player+1)%2].getHealth()==0){
                        // TODO: publish player win
                    }
                }                    
            }
        }
    }

    // endturn
    public void endStage(){
        // TODO: publish ke board_controller utk udah masuk endPhase
        this.cur_player = (this.cur_player+1)%2;
    }

    @Override
    public void publish(Event event) {
        // TODO Auto-generated method stub

    }
}
