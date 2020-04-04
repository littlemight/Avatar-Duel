package com.avatarduel.model.card;

import com.avatarduel.model.Element;

public abstract class Skill extends Card implements Summonable {
    private int power;

    public Skill() {
        super();
        this.power = 0;
    }

    public Skill(String name, String description, Element element, String IMG_PATH, int power) {
        super(name, description, element, IMG_PATH);
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }
}
