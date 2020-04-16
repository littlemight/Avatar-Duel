package com.avatarduel.model.card;

import com.avatarduel.event.*;
import com.avatarduel.model.Position;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class SummonedCharacter implements Summoned, Publisher {
    private Character card;
    private int d_atk;
    private int d_def;
    private IntegerProperty net_atk;
    private IntegerProperty net_def;
    private Position position; // attack or defense or destroyed(????)
    private boolean has_attacked;
    private boolean just_summoned;
    private int powered_up; // bisa aja di powered_up oleh 2 kartu, meskipun efeknya sama aja
    // berguna kalo kita mau discard kartu

    BoardChannel channel;

    private List<SummonedSkill> attached_skills;

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

    public boolean getHasAttacked(){
        return this.has_attacked;
    }

    public boolean getJustSummoned(){
        return this.just_summoned;
    }

    public void setHasAttacked(boolean status){
        this.has_attacked = status;
    }

    public void setJustSummoned(boolean status){
        this.just_summoned = status;
    }

    public Card getBaseCard() {
        return this.card;
    }

    public int getDAtk() {
        return this.d_atk;
    }

    public int getDDef() {
        return this.d_def;
    }

    public IntegerProperty getNetAtkProperty() {
        return this.net_atk;
    }

    public int getNetAtk() {
        return this.net_atk.getValue();
    }

    public IntegerProperty getNetDefProperty() {
        return this.net_def;
    }

    public int getNetDef() {
        return this.net_def.getValue();
    }

    public int getCombatValue() {
        if (this.position == Position.ATTACK) {
            return this.getNetAtk();
        } else {
            return this.getNetDef();
        }
    }

    public void rotate() {
        if (this.position == Position.ATTACK) {
            this.position = Position.DEFENSE;
        } else if (this.position == Position.DEFENSE) {
            this.position = Position.ATTACK;
        }
    }

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
        } else { // destroy
//            this.position = Position.DESTROYED;
            for (SummonedSkill summoned_skill: attached_skills) {
                summoned_skill.publish(new DestroyCardEvent(summoned_skill));
            }
            publish(new DestroyCardEvent(this));
        }
    }

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

    public List<SummonedSkill> getAttachedSkills() {
        return this.attached_skills;
    }

    public Position getPosition(){
        return this.position;
    }

    public void removeCard(){
        this.position = Position.DESTROYED;
        for (SummonedSkill summoned_skill: attached_skills) {
            summoned_skill.publish(new DestroyCardEvent(summoned_skill));
        }
        publish(new DestroyCardEvent(this));
    }

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
