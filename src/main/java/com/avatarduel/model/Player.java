package com.avatarduel.model;

import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.SummonedCharacter;
import com.avatarduel.model.card.SummonedSkill;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Player {
    public static final int MAX_HEALTH = 80;
    public static final int MAX_HAND = 7;
    public static final int MAX_ZONE = 8;

    private String name;
    private IntegerProperty health;
    private Map<Element, IntegerProperty> max_power;
    private Map<Element, IntegerProperty> power;
    private List<Card> hand;
    private Deck deck;

    private List<SummonedCharacter> character_zone;
    private List<SummonedSkill> skill_zone;

    public Player() {
        this.name = "Placeholder";
        this.health = new SimpleIntegerProperty(MAX_HEALTH);
        this.max_power = new HashMap<Element, IntegerProperty>();
        this.power = new HashMap<Element, IntegerProperty>();

        for (Element e: Element.values()) {
            max_power.put(e,  new SimpleIntegerProperty(0));
            power.put(e, new SimpleIntegerProperty(0));
        }
        hand = new ArrayList<Card>();
        Dealer dealer = new Dealer();
        deck = dealer.getDeck(0);
    }

    public Player(String name) {
        this.name = name;
        this.health = new SimpleIntegerProperty(MAX_HEALTH);
        this.max_power = new HashMap<Element, IntegerProperty>();
        this.power = new HashMap<Element, IntegerProperty>();

        for (Element e: Element.values()) {
            max_power.put(e,  new SimpleIntegerProperty(0));
            power.put(e, new SimpleIntegerProperty(0));
        }
        hand = new ArrayList<Card>();
        Dealer dealer = new Dealer();
        deck = dealer.getDeck(ThreadLocalRandom.current().nextInt(40, 60 + 1));
    }

    public IntegerProperty getMaxPowerProperty(Element e) {
        return max_power.get(e);
    }
    public int getMaxPower(Element e) {
        return max_power.get(e).getValue();
    }

    public IntegerProperty getPowerProperty(Element e) {
        return power.get(e);
    }
    public int getPower(Element e) {
        return power.get(e).getValue();
    }

    public IntegerProperty getHealthProperty() {
        return this.health;
    }

    public int getHealth() {
        return this.health.getValue();
    }

    public String getName() {
        return this.name;
    }

    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Asumsi: amount <= max_power(e)
     * @param e Element yang mana
     * @param amount Banyaknya power yang digunakan
     */
    public void usePower(Element e, int amount) {
//        power.put(e, power.get(e).substract(amount));
        power.get(e).subtract(amount);
    }

    /**
     * Menambahkan power element e (dipanggil kalo naroh kartu Land)
     * @param e element power yang ditambahin
     */
    public void addPower(Element e) {
//        max_power.put(e, max_power.get(e) + 1);
        max_power.get(e).add(1);
    }

    /**
     * Reset power (dipanggil pas awal turn)
     */
    public void resetPower() {
        for (Element e: Element.values()) {
//            power.put(e, max_power.get(e));
            power.get(e).setValue(0);
        }
    }

    public Card drawCard() {

        // if hand size < 7 then do this and public DrawedCardEvent, so that ui can update
        Card ret = deck.drawCard();
        hand.add(ret); // in the meantime this'll suffice
        return ret;
    }
}
