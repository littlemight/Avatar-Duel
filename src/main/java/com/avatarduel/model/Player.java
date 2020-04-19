package com.avatarduel.model;

import com.avatarduel.event.BoardChannel;
import com.avatarduel.event.CardDrawnEvent;
import com.avatarduel.event.Event;
import com.avatarduel.event.Publisher;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Player class, holding information and states related to player.
 */
public class Player implements Publisher {
    public static final int MAX_HEALTH = 80;
    public static final int MAX_HAND = 7;
    public static final int MAX_ZONE = 6;

    private String name;
    private IntegerProperty health;
    private Map<Element, IntegerProperty> max_power;
    private Map<Element, IntegerProperty> power;
    private List<Card> hand;
    private Deck deck;

    private List<SummonedCharacter> character_zone;
    private List<SummonedSkill> skill_zone;
    public boolean hasUsedLand;
    public boolean anyCharOnField;

    public BoardChannel channel;

    /**
     * Default constructor.
     * Initialize name with "Placeholder".
     * Sets health with value defined in MAX_HEALTH
     * Prepares the rest of attributes.
     */
    public Player() {
        this.name = "Placeholder";
        this.health = new SimpleIntegerProperty(MAX_HEALTH);
        this.max_power = new HashMap<Element, IntegerProperty>();
        this.power = new HashMap<Element, IntegerProperty>();

        for (Element e : Element.values()) {
            max_power.put(e, new SimpleIntegerProperty(0));
            power.put(e, new SimpleIntegerProperty(0));
        }
        hand = new ArrayList<Card>();
        deck = new Deck();
        character_zone = new ArrayList<SummonedCharacter>();
        skill_zone = new ArrayList<SummonedSkill>();
        anyCharOnField = false;
    }

    /**
     * Constructor for Player class
     * @param name Accepts string as the player name
     * @param deck Reference to the deck object for this player
     * @param channel Reference to the BoardChannel object this player connected to
     * @see Deck
     * @see BoardChannel
     */
    public Player(String name, Deck deck, BoardChannel channel) {
        this.name = name;
        this.health = new SimpleIntegerProperty(MAX_HEALTH);
        this.max_power = new HashMap<Element, IntegerProperty>();
        this.power = new HashMap<Element, IntegerProperty>();

        for (Element e : Element.values()) {
//            max_power.put(e, new SimpleIntegerProperty(0));
            max_power.put(e, new SimpleIntegerProperty(0)); // buat testing
            power.put(e, new SimpleIntegerProperty(0));
        }
        hand = new ArrayList<Card>();
        this.deck = deck;

        character_zone = new ArrayList<SummonedCharacter>();
        skill_zone = new ArrayList<SummonedSkill>();
        anyCharOnField = false;

        this.channel = channel;
    }

    /**
     * Gets the value of max power property of this object as {@link IntegerProperty}.
     * @param e {@link Element} type to get the max power value on.
     * @return {@link IntegerProperty} of the max power element e
     * @see Element
     * @see IntegerProperty
     */
    public IntegerProperty getMaxPowerProperty(Element e) {
        return max_power.get(e);
    }

    /**
     * Gets the value of max power property of this object as integer primitive.
     * @param e {@link Element} type to get the max power value on.
     * @return integer primitive of the max power element e
     * @see Element
     */
    public int getMaxPower(Element e) {
        return max_power.get(e).getValue();
    }

    /**
     * Gets the player's power property according to the supplied element type as {@link IntegerProperty}
     * @param e {@link Element} type to get the power property on.
     * @return {@link IntegerProperty} of the power element e
     * @see Element
     * @see IntegerProperty
     */
    public IntegerProperty getPowerProperty(Element e) {
        return power.get(e);
    }

    /**
     * Gets the player's power property according to the supplied element type as integer primitive.
     * @param e {@link Element} type to get the power value on.
     * @return integer primitive of the power element e.
     * @see Element
     */
    public int getPower(Element e) {
        return power.get(e).getValue();
    }

    /**
     * Get the player's health property as {@link IntegerProperty}
     * @return {@link IntegerProperty} of this object's health property.
     * @see IntegerProperty
     */
    public IntegerProperty getHealthProperty() {
        return this.health;
    }

    /**
     * Get the player's health property as integer primitive
     * @return integer primitive of this object's health property.
     */
    public int getHealth() {
        return this.health.getValue();
    }

    /**
     * Decrease this object's health by the argument.
     * If the value of the argument is larger than the player's current health, sets health to zero.
     * @param modifier integer value to substract from this player's health.
     */
    public void decreaseHealth(int modifier) {
        if (modifier > this.getHealth())
            modifier = this.getHealth();
        this.health.setValue(this.getHealth() - modifier);
    }

    /**
     * Getter for this player's name attribute
     * @return {@link String} of player's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for this player's deck object.
     * @return {@link Deck} object reference for this player.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Getter for the cards being hold by this player.
     * @return {@link List} with type of {@link Card} for this player.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Getter for the summoned character cards for this player.
     * @return {@link List} with type of {@link SummonedCharacter} for this player.
     */
    public List<SummonedCharacter> getCharacterZone() {
        return character_zone;
    }

    /**
     * Getter for the skill cards being summoned in field by this player.
     * @return {@link List} with type of {@link SummonedSkill} for this player.
     */
    public List<SummonedSkill> getSkillZone() {
        return skill_zone;
    }

    /**
     * Use the player's current element power with amount as parameter.
     * @param e {@link Element} power to be used.
     * @param amount integer value of the amount of power to be used.
     * @see Element
     */
    public void usePower(Element e, int amount) {
        power.get(e).set(power.get(e).getValue() - amount);
    }

    /**
     * Adds maximum power to given element by one.
     * @param e {@link Element} of the player to be added.
     * @see Element
     */
    public void addPower(Element e) {
        max_power.get(e).set(max_power.get(e).getValue() + 1);
        power.get(e).set(power.get(e).getValue() + 1);
    }

    /**
     * Resets each element power of the player to their respective maximum power value.
     */
    public void resetPower() {
        for (Element e : Element.values()) {
            power.get(e).set(max_power.get(e).getValue());
        }
    }

    /**
     * Draws one card from deck to this player.
     * Publishes a {@link CardDrawnEvent} object.
     */
    public void drawCard() {
        Card ret = deck.drawCard();
//        if (hand.size() >= MAX_HAND) {
//            // do something
//        }
        hand.add(ret);
        publish(new CardDrawnEvent(ret));
    }

    /**
     * Checks if player can summon a card into the field.
     * @param summonable reference to cards that implements {@link Summonable} interface.
     * @return boolean : true if player can summon the card specified in the argument.
     */
    public boolean canSummon(Summonable summonable) {
        Element el = ((Card) summonable).getElement();
        boolean enough_power = this.getPower(el) >= summonable.getPower();
        boolean enough_zone = true;
        if (summonable instanceof Character) {
            enough_zone = character_zone.size() < MAX_ZONE;
        } else { // its a skill
            enough_zone = skill_zone.size() < MAX_ZONE && anyCharOnField;
        }
        return enough_power && enough_zone;
    }

    /**
     * Summons a {@link Summonable} card into the field.
     * @param summonable The summonable card which will be summoned into a Summoned card
     * @return The summoned Card
     * @see Summoned
     */
    public Summoned summonCard(Summonable summonable) {
        usePower(summonable.getElement(), summonable.getPower());
        Summoned summoned_card;
        if (summonable instanceof Character) {
            summoned_card = new SummonedCharacter((Character) summonable);
            character_zone.add((SummonedCharacter) summoned_card);
        } else { // its a skill
            summoned_card = new SummonedSkill((Skill) summonable);
            skill_zone.add((SummonedSkill) summoned_card);
        }
        this.hand.remove((Card)summonable);
        return summoned_card;
    }

    /**
     * Implementation of {@link Publisher} interface.
     * @param event {@link Event} to be published by this object.
     */
    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
