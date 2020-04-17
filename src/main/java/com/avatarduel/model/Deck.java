package com.avatarduel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.model.card.*;

import com.avatarduel.model.card.Character;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Deck {
    private List<Card> deck;
    private IntegerProperty neff;
    private int size;
    private int turn=0; // increase the chance of getting land for the first 10 turns

    public Deck(){
        this.deck = new ArrayList<Card>();
        this.neff = new SimpleIntegerProperty(0);
        this.size = 0;
    }

    public Deck(int n){
        this.deck = new ArrayList<Card>();
        this.neff = new SimpleIntegerProperty(0);
        this.size = n;
    }

    public Deck(List<Card> deck){
        this.deck = deck;
        this.size = deck.size();
        this.neff = new SimpleIntegerProperty(this.size);
    }
    
    public IntegerProperty getNeff(){
        return this.neff;
    }

    public int getNeffValue(){
        return this.neff.getValue();
    }

    public int getSize(){
        return this.size;
    }

    int charcnt = 0;
    int destcnt = 0;
    public Card drawCard(){
        // // startof custom draw
        // // buat test, only draw 1 karakter + 1 destroy
        // if (charcnt == 0) {
        //     charcnt = 1;
        //     for (Card card: this.deck) {
        //         if (card instanceof Character) {
        //             return card;
        //         }
        //     }
        // }
        // if (destcnt == 0) {
        //     destcnt = 1;
        //     for (Card card: this.deck) {
        //         if (card instanceof Destroy) {
        //             return card;
        //         }
        //     }
        // }

        // // dan cuman draw kartu skill
        // while (this.deck.get(this.neff.getValue() - 1) instanceof Land ||
        //         this.deck.get(this.neff.getValue() - 1) instanceof Character ||
        //         this.deck.get(this.neff.getValue() - 1) instanceof Destroy
        // ) {
        //     this.neff.setValue(this.neff.getValue()-1);
        // }
        // // endof custom draw

        // Make Balanced Card Draw
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
        int pickedCard = Math.max((Math.max(count[0], count[1])), count[2]*2);
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

    public void addCard(Card card){
        this.deck.add(card);
        this.neff.setValue(this.neff.getValue()+1);
    }
}