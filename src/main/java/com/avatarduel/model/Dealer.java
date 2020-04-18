package com.avatarduel.model;
import java.io.File;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.net.URISyntaxException;

import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;
import com.avatarduel.util.CSVReader;
/**
 * Dealer class is used to load the list of the card 
 * and deck with good proportion
 */
public class Dealer {
    private List<Card> cards;
    private static final String LAND_CSV_FILE_PATH = "../card/data/land.csv";
    private static final String CHARACTER_CSV_FILE_PATH = "../card/data/character.csv";
    private static final String SKILL_AURA_CSV_FILE_PATH = "../card/data/skill_aura.csv";
    private static final String SKILL_POWER_UP_CSV_FILE_PATH = "../card/data/skill_power_up.csv";
    private static final String SKILL_DESTROY_CSV_FILE_PATH = "../card/data/skill_destroy.csv";

    /**
     * Default constructor for dealer
     */
    public Dealer(){
      try{
        this.loadCards();
      }
      catch(Exception e){
        System.out.println("Failed to load cards: " + e);
      }
    }
    /**
     * Load the card from databases
     * @throws IOException
     * @throws URISyntaxException
     */
    private void loadCards() throws IOException, URISyntaxException {
        File landCSVFile = new File(getClass().getResource(LAND_CSV_FILE_PATH).toURI());
        File characterCSVFile = new File(getClass().getResource(CHARACTER_CSV_FILE_PATH).toURI());
        File skillAuraCSVFile = new File(getClass().getResource(SKILL_AURA_CSV_FILE_PATH).toURI());
        File skillPowerUpCSVFile = new File(getClass().getResource(SKILL_POWER_UP_CSV_FILE_PATH).toURI());
        File skillDestroyCSVFile = new File(getClass().getResource(SKILL_DESTROY_CSV_FILE_PATH).toURI());

        CSVReader landReader = new CSVReader(landCSVFile, "\t");
        CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        CSVReader skillAuraReader= new CSVReader(skillAuraCSVFile, "\t");
        CSVReader skillPowerUpReader= new CSVReader(skillPowerUpCSVFile, "\t");
        CSVReader skillDestroyReader= new CSVReader(skillDestroyCSVFile, "\t");

        landReader.setSkipHeader(true);
        characterReader.setSkipHeader(true);
        skillAuraReader.setSkipHeader(true);
        skillPowerUpReader.setSkipHeader(true);
        skillDestroyReader.setSkipHeader(true);

        List<String[]> landRows = landReader.read();
        List<String[]> characterRows = characterReader.read();
        List<String[]> skillAuraRows = skillAuraReader.read();
        List<String[]> skillPowerUpRows = skillPowerUpReader.read();
        List<String[]> skillDestroyRows = skillDestroyReader.read();

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
        for (String[] row: skillPowerUpRows) {
            this.cards.add(new PowerUp(row[1],
                    row[3],
                    Element.valueOf(row[2]),
                    row[4],
                    Integer.parseInt(row[5])));
        }
        for (String[] row: skillDestroyRows) {
            this.cards.add(new Destroy(row[1],
                    row[3],
                    Element.valueOf(row[2]),
                    row[4],
                    Integer.parseInt(row[5])));
        }
      }
  
    /**
     * Getter for the deck
     * with card proportion land : karakter : skill is 2 : 2 : 1
     * @param n number of the card in the deck we want
     * @return deck
     */
    public Deck getDeck(int n){

        Deck deck = new Deck(n);
        Collections.shuffle(cards);

        float portion = n/5;
        int nk=0;int nl=0;int ns=0;

        ListIterator<Card> it = cards.listIterator();
        while(nk+nl+ns<n){
            if (!it.hasNext()) it = cards.listIterator();
            Card card = it.next();
            if ((card instanceof Character) && nk<=Math.round(portion*2)){
                deck.addCard(card);
                nk++;
            }
            else if ((card instanceof Land) && nl<=Math.round(portion*2)){
                deck.addCard(card);
                nl++;
            }
            else if ((card instanceof Skill) && (ns<=Math.round(portion) || (ns>=Math.round(portion) && nk+nl>=Math.round(portion*2)*2))){
                deck.addCard(card);
                ns++;
            }
        }
        return deck;
    }
}