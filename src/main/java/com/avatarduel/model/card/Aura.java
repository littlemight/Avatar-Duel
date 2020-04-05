package com.avatarduel.model.card;

import com.avatarduel.model.Element;

public class Aura extends Skill {
    private int delta_atk;
    private int delta_def;

    public Aura() {
        super();
        this.delta_atk = 0;
        this.delta_def = 0;
    }

    public Aura(String name, String description, Element element, String IMG_PATH, int power, int delta_atk, int delta_def) {
        super(name, description, element, IMG_PATH, power);
        this.delta_atk = delta_atk;
        this.delta_def = delta_def;
    }

    public int getDeltaAtk() {
        return this.delta_atk;
    }

    public int getDeltaDef() {
        return this.delta_def;
    }
}
