package com.avatarduel;

import com.avatarduel.event.*;
import com.avatarduel.model.*;
import com.avatarduel.model.card.*;

import javafx.geometry.Pos;

public class Game implements Publisher, Subscriber{
    
    // components
    EventChannel channel;
    private Player players[];
    int cur_player;
    // defined game phases
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END};
    int phase_id = 0;


    // initialize
    public Game(Player p1, Player p2, EventChannel channel) {
        this.players = new Player[2];
        this.players[0] = p1;
        this.players[1] = p2;
        this.channel = channel;
    }

    public Player getPlayer(int id) {
        return this.players[id];
    }

    public void stageController(Phase phase){
        System.out.println("Masuk stage!");
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
        Card card = player.drawCard();
        // when safe, reset player power
        player.resetPower();

    }
    // main

    // battle
    public void battleStage(){
        // TODO: publish ke board_controller udah masuk ke phase battle
    }

    public int solveBattle(SummonedCharacter cur_player_card, SummonedCharacter enemy_player_card){
        if (cur_player_card.getPosition()==Position.ATTACK){
            System.out.print(cur_player_card.getCombatValue());
            System.out.print(" vs ");
            System.out.println(enemy_player_card.getCombatValue());
            if (cur_player_card.getCombatValue() > enemy_player_card.getCombatValue()){
                // TODO: publish kartu yang di remove
                if (enemy_player_card.getPosition()==Position.ATTACK || cur_player_card.checkPowerUp()>0){
                    System.out.println(this.players[(cur_player+1)%2].getHealth());
                    this.players[(cur_player+1)%2].decreaseHealth(cur_player_card.getCombatValue()-enemy_player_card.getCombatValue());
                    enemy_player_card.removeCard();
                    if (this.players[(cur_player+1)%2].getHealth()==0){
                        // TODO: publish player win
                    }
                    return (cur_player+1)%2+1;
                }                    
            }
        }
        return -1;
    }

    // endturn
    public void endStage(){
        // TODO: publish ke board_controller utk udah masuk endPhase
        this.cur_player = (this.cur_player+1)%2;
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);

    }

    @Override
    public void onEvent(Event event) {
        // if (event instanceof ChangePhaseEvent){
        //     stageController((Phase)event.getInfo());
        // }

    }
}
