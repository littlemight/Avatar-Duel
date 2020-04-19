package com.avatarduel;

import com.avatarduel.event.*;
import com.avatarduel.model.*;
import com.avatarduel.model.card.*;

import javafx.geometry.Pos;

/**
 * Main game controller class.
 * Contains method to calculate the result of each action in each phases.
 */
public class Game implements Publisher, Subscriber{
    
    // components
    EventChannel channel;
    private Player players[];
    int cur_player = 1;
    // defined game phases
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END};
    int phase_id = 0;


    /**
     * Constructor for Game class
     * @param p1 reference to player1 object.
     * @param p2 reference to player2 object
     * @param channel reference to channel to listen.
     * @see Player
     * @see EventChannel
     * @see BoardChannel
     */
    public Game(Player p1, Player p2, EventChannel channel) {
        this.players = new Player[3];
        this.players[1] = p1;
        this.players[2] = p2;
        this.channel = channel;
        this.cur_player = 1;
    }

    /**
     * Setting up the game.
     */
    public void setup(){
        this.drawBoth();
        this.players[1].resetPower();
        this.players[2].resetPower();
        publish(new PlayerChangedEvent(this.cur_player));
        stageController(phases[phase_id]); //masuk phase draw
    }

    /**
     * Draw a card from deck to each players.
     * Used when setting up the game.
     * Cards were drawn until each player have 7 total of cards.
     */
    public void drawBoth(){
        for (int i = 0; i < 7; i++) {
            this.players[1].drawCard();
            this.players[2].drawCard();
        }
    }

    /**
     * Getting the player object based on their id (or index).
     * @param id player id (or index)
     * @return {@link Player} object with index of id
     * @see Player
     */
    public Player getPlayer(int id) {
        return this.players[id];
    }

    /**
     * Executing game logic based from the phase given as parameter
     * @param phase Phase where the current player has gotten into
     * @see Phase
     */
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
     * Draw a card to a player.
     * If there is no card left on deck, WinEvent will be published.
     * Otherwise, new card will be drawn from deck and the player's power will be reset.
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

    public void main(Player player) {
        // 
    }

    public void battleStage(){
        // TODO: publish ke board_controller udah masuk ke phase battle
    }

    /**
     * Executes game logic when in a character card attacks enemy character card
     * @param cur_player_card current player character card
     * @param enemy_player_card enemy character card
     * @see SummonedCharacter
     */
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

    /**
     * Executes game logic when current player character card attacks the player directly.
     * @param cur_player_card current player character card
     * @see SummonedCharacter
     * @see Player
     */
    public void solveDirectAttack(SummonedCharacter cur_player_card){
        if (cur_player_card.getPosition()==Position.ATTACK){
            cur_player_card.setHasAttacked(true);
            this.players[cur_player%2+1].decreaseHealth(cur_player_card.getCombatValue());
            if (this.players[cur_player%2+1].getHealth()==0){
                publish(new WinEvent(this.players[cur_player]));
            }
        }
    }

    /**
     * Checks if a character card on field can target enemy
     * @param chara selected character card
     * @return boolean : true if card can target enemy.
     */
    public boolean canTarget(SummonedCharacter chara){
        return !(chara.getPosition()==Position.DEFENSE) && (!chara.getHasAttacked() && !chara.getJustSummoned());
    }

    /**
     * Checks if a character card can perform direct attack to enemy player
     * @return boolean : true if enemy doesn't have character card in field.
     */
    public boolean canDirectAttack(){
        return this.players[cur_player%2+1].getCharacterZone().isEmpty();
    }

    /**
     * Compares current player selected character card and enemy's character card
     * @param targets current player character card
     * @param targeted enemy's character card
     * @return true if combat value of the selected card is bigger
     * @see SummonedCharacter
     */
    public boolean isStronger(SummonedCharacter targets, SummonedCharacter targeted){
        return targets.getCombatValue() > targeted.getCombatValue();
    }

    /**
     * Get the current player in game
     * @return int of the id of current playing player
     */
    public int getCurPlayer() {
        return this.cur_player;
    }

    /**
     * Executes game logic when phase in end phase
     * Sets every character card in field for the current player hasAttacked attribute to false.
     * Sets every character card summoned status of justSummoned to false.
     * Sets the status of current player to not used a land card.
     * Changed the current playing player to other player.
     * Publishing PlayerChangedEvent event to channel.
     */
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

    /**
     * Publish method implementation from Publisher interface
     * @param event event to be sent into this object's assigned channel
     */
    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);

    }

    /**
     * OnEvent method implementation from Subscriber interface.
     * Only executes event that is an instance of PhaseChangedEvent
     * @param event Event to be received by this object.
     * @see PhaseChangedEvent
     */
    @Override
    public void onEvent(Event event) {
        if (event instanceof PhaseChangedEvent){
            stageController((Phase)event.getInfo());
        }

    }
}
