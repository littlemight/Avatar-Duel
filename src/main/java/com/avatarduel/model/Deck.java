package com.avatarduel.model;

import java.util.ArrayList;
import java.util.List;

import com.avatarduel.model.card.*;

import com.avatarduel.model.card.Character;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Deck {
    private List<Card> deck;
    private IntegerProperty neff;
    private int size;    

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
        // startof custom draw
        // buat test, only draw 1 karakter + 1 destroy
        if (charcnt == 0) {
            charcnt = 1;
            for (Card card: this.deck) {
                if (card instanceof Character) {
                    return card;
                }
            }
        }
        if (destcnt == 0) {
            destcnt = 1;
            for (Card card: this.deck) {
                if (card instanceof Destroy) {
                    return card;
                }
            }
        }

        // dan cuman draw kartu skill
        while (this.deck.get(this.neff.getValue() - 1) instanceof Land ||
                this.deck.get(this.neff.getValue() - 1) instanceof Character ||
                this.deck.get(this.neff.getValue() - 1) instanceof Destroy
        ) {
            this.neff.setValue(this.neff.getValue()-1);
        }
        // endof custom draw

        this.neff.setValue(this.neff.getValue()-1);
        return this.deck.get(this.neff.getValue());
    }

    public void addCard(Card card){
        this.deck.add(card);
        this.neff.setValue(this.neff.getValue()+1);
    }
}