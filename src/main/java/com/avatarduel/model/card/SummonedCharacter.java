package com.avatarduel.model.card;

import com.avatarduel.event.*;
import com.avatarduel.model.Position;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * A Summoned class for Character
 */
public class SummonedCharacter implements Summoned, Publisher {
    private Character card;
    private int d_atk;
    private int d_def;
    /**
     * Net attack which is an integer property, used for binding to the GUI
     */
    private IntegerProperty net_atk;
    /**
     * Net defense which is an integer property, used for binding to the GUI
     */
    private IntegerProperty net_def;
    private Position position;
    private boolean has_attacked;
    private boolean just_summoned;
    private int powered_up;

    BoardChannel channel;

    /**
     * List of attached skills on this card
     */
    private List<SummonedSkill> attached_skills;

    /**
     * Create a new SummonedCharacter from a character card, this SummonedCharacter instance will communicate with the player and the board
     * @param card character card
     */
    public SummonedCharacter(Character card) {
        this.card = card;
        this.d_atk = 0;
        this.d_def = 0;
        this.position = Position.ATTACK;
        this.has_attacked = false;
        this.just_summoned = true;
        this.powered_up = 0;
        this.net_atk = new SimpleIntegerProperty(this.card.getAtk());
        this.net_def = new SimpleIntegerProperty(this.card.getDef());
        this.attached_skills = new ArrayList<SummonedSkill>();
    }

    /**
     * Getter for has_attacked
     * @return True if this card has attacked this turn, false otherwise
     */
    public boolean getHasAttacked(){
        return this.has_attacked;
    }

    /**
     * Getter for just_summoned
     * @return True if this card has just been summoned this turn, false otherwise
     */
    public boolean getJustSummoned(){
        return this.just_summoned;
    }

    /**
     * Setter for card's has_attack property
     * @param status has attacked status
     */
    public void setHasAttacked(boolean status){
        this.has_attacked = status;
    }

    /**
     * Setter for card's just_summoned property
     * @param status just summoned status
     */
    public void setJustSummoned(boolean status){
        this.just_summoned = status;
    }

    public Card getBaseCard() {
        return this.card;
    }

    /**
     * Get the delta attack from the attached skills
     * @return delta attack value
     */
    public int getDAtk() {
        return this.d_atk;
    }

    /**
     * Get the delta defense from the attached skills
     * @return delta defense value
     */
    public int getDDef() {
        return this.d_def;
    }

    /**
     * Get the property of the netatk, used to bind with GUI
     * @return net_atk property
     */
    public IntegerProperty getNetAtkProperty() {
        return this.net_atk;
    }

    /**
     * Get the value of netatk
     * @return net_atk value
     */
    public int getNetAtk() {
        return this.net_atk.getValue();
    }

    /**
     * Get the property of the netdef, used to bind with GUI
     * @return net_def property
     */
    public IntegerProperty getNetDefProperty() {
        return this.net_def;
    }

    /**
     * Get the value of netdef
     * @return net_def value
     */
    public int getNetDef() {
        return this.net_def.getValue();
    }

    /**
     * Get the combat value which will be used to evaluate when attacking another card or being attacked by another card.
     * The combat value depends on the position of the summoned character.
     * @return combat value
     */
    public int getCombatValue() {
        if (this.position == Position.ATTACK) {
            return this.getNetAtk();
        } else {
            return this.getNetDef();
        }
    }

    /**
     * Change the summoned card position
     */
    public void rotate() {
        if (this.position == Position.ATTACK) {
            this.position = Position.DEFENSE;
        } else if (this.position == Position.DEFENSE) {
            this.position = Position.ATTACK;
        }
    }

    /**
     * Apply the skill to the card and adds it to the attached_skills list
     * @param summonedskill_card the skill that is being applied
     */
    public void addSkill(SummonedSkill summonedskill_card) {
        attached_skills.add(summonedskill_card);
        Skill skill_card = (Skill) summonedskill_card.getBaseCard();
        if (skill_card instanceof Aura) {
            this.d_atk += ((Aura) skill_card).getDeltaAtk();
            this.d_def += ((Aura) skill_card).getDeltaDef();
            this.net_atk.set(this.d_atk + this.card.getAtk());
            this.net_def.set(this.d_def + this.card.getDef());
        } else if (skill_card instanceof PowerUp) {
            this.powered_up++;
        } else {
            for (SummonedSkill summoned_skill: attached_skills) {
                summoned_skill.publish(new DestroySummonedCardEvent(summoned_skill));
            }
            publish(new DestroySummonedCardEvent(this));
        }
    }

    /**
     * Remove the effect of the skill from the card and remove it from the attached_skills list.
     * This assumes that the skill card has already been applied previously.
     * @param summonedskill_card the removed skill
     */
    public void removeSkill(SummonedSkill summonedskill_card) {
        attached_skills.remove(summonedskill_card);
        Skill skill_card = (Skill) summonedskill_card.getBaseCard();
        if (skill_card instanceof Aura) {
            this.d_atk -= ((Aura) skill_card).getDeltaAtk();
            this.d_def -= ((Aura) skill_card).getDeltaDef();
            this.net_atk.set(this.d_atk + this.card.getAtk());
            this.net_def.set(this.d_def + this.card.getDef());
        } else if (skill_card instanceof PowerUp) {
            this.powered_up--;
        }
    }

    /**
     * Return all the attached skill to this card
     * @return list of summoned skill
     */
    public List<SummonedSkill> getAttachedSkills() {
        return this.attached_skills;
    }

    /**
     * Get the position of this card on the board, attack or defense.
     * @return position of card
     */
    public Position getPosition(){
        return this.position;
    }

    public void removeCard(){
        for (SummonedSkill summoned_skill: attached_skills) {
            summoned_skill.publish(new DestroySummonedCardEvent(summoned_skill));
        }
        publish(new DestroySummonedCardEvent(this));
    }

    /**
     * Check if this card is poweredup
     * @return poweredup value
     */
    public int checkPowerUp(){
        return this.powered_up;
    }

    public void setChannel(BoardChannel channel) {
        this.channel = channel;
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
