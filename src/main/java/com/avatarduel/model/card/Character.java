package com.avatarduel.model.card;

import com.avatarduel.model.Element;

public class Character extends Card implements Summonable {
    private int atk;
    private int def;
    private int power;

    public Character(String name, String description, Element element, String IMG_PATH, int power, int atk, int def) {
        super(name, description, element, IMG_PATH);
        this.atk = atk;
        this.def = def;
        this.power = power;
    }

    public int getAtk() {
        return this.atk;
    }

    public int getDef() {
        return this.def;
    }

    public int getPower() {
        return this.power;
    }
}
