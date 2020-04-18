package com.avatarduel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.model.card.*;

import com.avatarduel.model.card.Character;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Deck class is a set of players card
 */
public class Deck {
    private List<Card> deck;
    private IntegerProperty neff;
    private int size;
    private int turn=0; // increase the chance of getting land for the first 12 draws

    /**
     * Default constructor of the deck
     */
    public Deck(){
        this.deck = new ArrayList<Card>();
        this.neff = new SimpleIntegerProperty(0);
        this.size = 0;
    }
    /**
     * Deck constructor with the number of the deck's size
     * @param n size of the deck
     */
    public Deck(int n){
        this.deck = new ArrayList<Card>();
        this.neff = new SimpleIntegerProperty(0);
        this.size = n;
    }
    /**
     * Deck constructor with the list of card as parameter
     * @param deck list of the card
     */
    public Deck(List<Card> deck){
        this.deck = deck;
        this.size = deck.size();
        this.neff = new SimpleIntegerProperty(this.size);
    }
    /**
     * Get Neff of the deck
     * and return in integerproperty. So we can track changes 
     * with the listener and automatically updates to the GUI
     * @return neff of the deck
     */
    public IntegerProperty getNeff(){
        return this.neff;
    }

    /**
     * Get Neff value
     * @return neff in int
     */
    public int getNeffValue(){
        return this.neff.getValue();
    }

    /**
     * Get Size of the deck
     * @return Size of the deck
     */
    public int getSize(){
        return this.size;
    }

    int charcnt = 0;
    int destcnt = 0;

    /**
     * Get card from the Balanced Card Draw
     * @return Selected card
     */
    public Card drawCard(){

        int[] count = {0,0,0};
        Card pickedCharacter = null;
        Card pickedLand = null;
        Card pickedSkill = null;
        for (Card card : deck){
            if (card instanceof Character) {
                if (count[0]==0) pickedCharacter=card;
                count[0]++;
            } else if (card instanceof Land) {
                if (count[1]==0) pickedLand=card;
                count[1]++;
            } else if (card instanceof Skill) {
                if (count[2]==0) pickedSkill=card;
                count[2]++;
            }
        }
        count[1]+=(ThreadLocalRandom.current().nextInt((turn%3), 5)*(turn%3));
        int pickedCard = Math.max((Math.max(count[0], count[1])), count[2]*(2*((turn+1)%2)));
        if (turn < 12) turn++;
        this.neff.setValue(this.neff.getValue()-1);
        if (pickedCard==count[1]){
            this.deck.remove(pickedLand);
            return pickedLand;
        } else if (pickedCard==count[0]){
            this.deck.remove(pickedCharacter);
            return pickedCharacter;
        } else {
            this.deck.remove(pickedSkill);
            return pickedSkill;
        }
    }

    /**
     * Void to add the card to the deck
     * @param card card want to be added
     */
    public void addCard(Card card){
        this.deck.add(card);
        this.neff.setValue(this.neff.getValue()+1);
    }
}