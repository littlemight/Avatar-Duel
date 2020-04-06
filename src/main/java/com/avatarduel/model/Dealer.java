package com.avatarduel.model;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.net.URISyntaxException;

import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Aura;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Land;
import com.avatarduel.model.card.Skill;
import com.avatarduel.util.CSVReader;

public class Dealer {
    private List<Card> cards;
    private static final String LAND_CSV_FILE_PATH = "../card/data/land.csv";
    private static final String CHARACTER_CSV_FILE_PATH = "../card/data/character.csv";
    private static final String SKILL_AURA_CSV_FILE_PATH = "../card/data/skill_aura.csv";

    public Dealer(){
      try{
        this.loadCards();
      }
      catch(Exception e){
        System.out.println("Failed to load cards: " + e);
      }
    }
    
    public void loadCards() throws IOException, URISyntaxException {
        File landCSVFile = new File(getClass().getResource(LAND_CSV_FILE_PATH).toURI());
        File characterCSVFile = new File(getClass().getResource(CHARACTER_CSV_FILE_PATH).toURI());
        File skillAuraCSVFile = new File(getClass().getResource(SKILL_AURA_CSV_FILE_PATH).toURI());

        CSVReader landReader = new CSVReader(landCSVFile, "\t");
        CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        CSVReader skillAuraReader= new CSVReader(skillAuraCSVFile, "\t");
    
        landReader.setSkipHeader(true);
        characterReader.setSkipHeader(true);
        skillAuraReader.setSkipHeader(true);
    
        List<String[]> landRows = landReader.read();
        List<String[]> characterRows = characterReader.read();
        List<String[]> skillAuraRows = skillAuraReader.read();
    
        this.cards = new ArrayList<Card>();
    
        for (String[] row: landRows) {
          this.cards.add(new Land(row[1], row[3], Element.valueOf(row[2]), row[4]));
        }
        for (String[] row: characterRows) {
          this.cards.add(new Character(row[1],
                  row[3],
                  Element.valueOf(row[2]),
                  row[4],
                  Integer.parseInt(row[7]),
                  Integer.parseInt(row[5]),
                  Integer.parseInt(row[6])));
        }
        for (String[] row: skillAuraRows) {
          this.cards.add(new Aura(row[1],
                  row[3],
                  Element.valueOf(row[2]),
                  row[4],
                  Integer.parseInt(row[5]),
                  Integer.parseInt(row[6]),
                  Integer.parseInt(row[7])));
        }
      }
  
    public Deck getDeck(int n){
      // Proporsi yang disarankan perbandingan land : karakter : skill adalah 2 : 2 : 1
      Deck deck = new Deck(n);
      Collections.shuffle(cards);
      
      int portion = n/5;
      int nk=0;int nl=0;int ns=0;
      for (Card card : cards){
        if (nk+nl+ns==n) break;
        if ((card instanceof Character) && nk<portion*2){
          deck.addCard(card);
          nk++;
        }
        else if ((card instanceof Land) && nl<portion*2){
          deck.addCard(card);
          nl++;
        }
        else if ((card instanceof Skill) && ns<portion){
          deck.addCard(card);
          ns++;
        }
      }
      return deck;
    }
}