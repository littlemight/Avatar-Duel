package com.avatarduel.model.card;

import com.avatarduel.model.Position;

import java.util.ArrayList;
import java.util.List;

public class SummonedCharacter implements Summoned {
    private Character card;
    private int d_atk;
    private int d_def;
    private Position position; // attack or defense or destroyed(????)
    private boolean has_attacked;
    private boolean just_summoned;
    private int powered_up; // bisa aja di powered_up oleh 2 kartu, meskipun efeknya sama aja
    // berguna kalo kita mau discard kartu

    private List<Skill> attached_skills;

    public SummonedCharacter(Character card) {
        this.card = card;
        this.d_atk = 0;
        this.d_def = 0;
        this.position = Position.ATTACK;
        this.has_attacked = false;
        this.just_summoned = true;
        this.powered_up = 0;
        this.attached_skills = new ArrayList<Skill>();
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

    public int getNetAtk() {
        return card.getAtk() + this.getDAtk();
    }

    public int getNetDef() {
        return card.getDef() + this.getDDef();
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

    public void addSkill(Skill skill_card) {
        attached_skills.add(skill_card);
        if (skill_card instanceof Aura) {
            this.d_atk += ((Aura) skill_card).getDeltaAtk();
            this.d_def += ((Aura) skill_card).getDeltaDef();
        } else if (skill_card instanceof PowerUp) {
            this.powered_up++;
        } else { // destroy
            this.position = Position.DESTROYED;
        }
    }

    public void removeSkill(Skill skill_card) {
        attached_skills.remove(skill_card);
        if (skill_card instanceof Aura) {
            this.d_atk -= ((Aura) skill_card).getDeltaAtk();
            this.d_def -= ((Aura) skill_card).getDeltaDef();
        } else if (skill_card instanceof PowerUp) {
            this.powered_up--;
        }
    }

    public List<Skill> getAttachedSkills() {
        return this.attached_skills;
    }

    public Position getPosition(){
        return this.position;
    }

    public void removeCard(){
        this.position = Position.DESTROYED;
    }

    public int checkPowerUp(){
        return this.powered_up;
    }
}
