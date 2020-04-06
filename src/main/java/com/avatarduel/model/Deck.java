package com.avatarduel.model;

import java.util.ArrayList;
import java.util.List;

import com.avatarduel.model.card.Card;

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

    public Card drawCard(){
        this.neff.setValue(this.neff.getValue()-1);
        return this.deck.get(this.neff.getValue());
    }

    public void addCard(Card card){
        this.deck.add(card);
        this.neff.setValue(this.neff.getValue()+1);
    }
}