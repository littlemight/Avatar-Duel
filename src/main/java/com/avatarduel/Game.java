package com.avatarduel;

import com.avatarduel.event.*;
import com.avatarduel.model.*;
import com.avatarduel.model.card.*;

import javafx.geometry.Pos;

public class Game implements Publisher, Subscriber{
    
    // components
    EventChannel channel;
    private Player players[];
    int cur_player = 1;
    // defined game phases
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END};
    int phase_id = 0;


    // initialize
    public Game(Player p1, Player p2, EventChannel channel) {
        this.players = new Player[3];
        this.players[1] = p1;
        this.players[2] = p2;
        this.channel = channel;
        this.cur_player = 1;
    }

    public void setup(){
        this.drawBoth();
        this.players[1].resetPower();
        this.players[2].resetPower();
        publish(new PlayerChangedEvent(this.cur_player));
        stageController(phases[phase_id]); //masuk phase draw
    }

    public void drawBoth(){
        for (int i = 0; i < 7; i++) {
            this.players[1].drawCard();
            this.players[2].drawCard();
        }
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
        if (this.players[cur_player].getDeck().getNeff().getValue()==0){
            publish(new WinEvent(this.players[cur_player%2+1]));
        }else{
            this.players[this.cur_player].drawCard();
            // when safe, reset player power
            this.players[this.cur_player].resetPower();
        }

    }
    // main
    public void main(Player player) {
        // 
    }
    // battle
    public void battleStage(){
        // TODO: publish ke board_controller udah masuk ke phase battle
    }

    public void solveBattle(SummonedCharacter cur_player_card, SummonedCharacter enemy_player_card){
        if (cur_player_card.getPosition()==Position.ATTACK){
            if (isStronger(cur_player_card, enemy_player_card)){
                cur_player_card.setHasAttacked(true);
                if (enemy_player_card.getPosition()==Position.ATTACK || cur_player_card.checkPowerUp()>0){
                    this.players[cur_player%2+1].decreaseHealth(cur_player_card.getCombatValue()-enemy_player_card.getCombatValue());
                    if (this.players[cur_player%2+1].getHealth()==0){
                        publish(new WinEvent(this.players[cur_player]));
                    }
                }
                enemy_player_card.removeCard();
            }
        }
    }

    public void solveDirectAttack(SummonedCharacter cur_player_card){
        if (cur_player_card.getPosition()==Position.ATTACK){
            cur_player_card.setHasAttacked(true);
            this.players[cur_player%2+1].decreaseHealth(cur_player_card.getCombatValue());
            if (this.players[cur_player%2+1].getHealth()==0){
                publish(new WinEvent(this.players[cur_player]));
            }
        }
    }

    public boolean canTarget(SummonedCharacter chara){
        return !(chara.getPosition()==Position.DEFENSE) && (!chara.getHasAttacked() && !chara.getJustSummoned());
    }

    public boolean canDirectAttack(){
        return this.players[cur_player%2+1].getCharacterZone().isEmpty();
    }

    public boolean isStronger(SummonedCharacter targets, SummonedCharacter targeted){
        return targets.getCombatValue() > targeted.getCombatValue();
    }

    public int getCurPlayer() {
        return this.cur_player;
    }

    // endturn
    public void endStage(){
        for (SummonedCharacter summoned_chara : this.players[this.cur_player].getCharacterZone()){
            if (summoned_chara.getHasAttacked()==true){
                summoned_chara.setHasAttacked(false);
            }
            if (summoned_chara.getJustSummoned()==true){
                summoned_chara.setJustSummoned(false);
            }
        }
        this.players[this.cur_player].hasUsedLand=false;
        this.cur_player = this.cur_player%2+1;
        publish(new PlayerChangedEvent(this.cur_player));
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof PhaseChangedEvent){
            stageController((Phase)event.getInfo());
        }

    }
}
