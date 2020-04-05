package com.avatarduel.model;

import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.SummonedCharacter;
import com.avatarduel.model.card.SummonedSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    public static final int MAX_HEALTH = 80;
    public static final int MAX_HAND = 7;
    public static final int MAX_ZONE = 8;

    private String name;
    private int health;
    private Map<Element, Integer> max_power;
    private Map<Element, Integer> power;
    private List<Card> hand;
    // tunggu class Deck
    // private Deck deck;

    private List<SummonedCharacter> character_zone;
    private List<SummonedSkill> skill_zone;

    public Player() {
        this.name = "Placeholder";
        this.health = MAX_HEALTH;
        for (Element e: Element.values()) {
            max_power.put(e, 0);
            power.put(e, 0);
        }
        hand = new ArrayList<Card>();
        // tunggu class Deck
    }

    public Player(String name) {
        this.name = name;
        this.health = MAX_HEALTH;
        for (Element e: Element.values()) {
            max_power.put(e, 0);
            power.put(e, 0);
        }
        hand = new ArrayList<Card>();
        // tunggu class Deck
    }

    public int getMaxPower(Element e) {
        return max_power.get(e);
    }

    public int getPower(Element e) {
        return power.get(e);
    }

    /**
     * Asumsi: amount <= max_power(e)
     * @param e Element yang mana
     * @param amount Banyaknya power yang digunakan
     */
    public void usePower(Element e, int amount) {
        power.put(e, power.get(e) - amount);
    }

    /**
     * Menambahkan power element e (dipanggil kalo naroh kartu Land)
     * @param e element power yang ditambahin
     */
    public void addPower(Element e) {
        max_power.put(e, max_power.get(e) + 1);
    }

    /**
     * Reset power (dipanggil pas awal turn)
     */
    public void resetPower() {
        for (Element e: Element.values()) {
            power.put(e, max_power.get(e));
        }
    }
}
