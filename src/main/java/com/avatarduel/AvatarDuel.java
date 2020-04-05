package com.avatarduel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.avatarduel.controller.CardController;
import com.avatarduel.model.card.Aura;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Land;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.avatarduel.model.Element;
import com.avatarduel.util.CSVReader;

// currently for testing purposes
public class AvatarDuel extends Application {
  private static final String LAND_CSV_FILE_PATH = "card/data/land.csv";
  private static final String CHARACTER_CSV_FILE_PATH = "card/data/character.csv";
  private static final String SKILL_AURA_CSV_FILE_PATH = "card/data/skill_aura.csv";

  private static ArrayList<Card> cards;

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

    cards = new ArrayList<Card>();

    for (String[] row: landRows) {
      cards.add(new Land(row[1], row[3], Element.valueOf(row[2]), row[4]));
    }
    for (String[] row: characterRows) {
      cards.add(new Character(row[1],
              row[3],
              Element.valueOf(row[2]),
              row[4],
              Integer.parseInt(row[7]),
              Integer.parseInt(row[5]),
              Integer.parseInt(row[6])));
    }
    for (String[] row: skillAuraRows) {
      cards.add(new Aura(row[1],
              row[3],
              Element.valueOf(row[2]),
              row[4],
              Integer.parseInt(row[5]),
              Integer.parseInt(row[6]),
              Integer.parseInt(row[7])));
    }
  }

  @Override
  public void start(Stage stage) {
    Text text = new Text();
    text.setText("Loading...");
    text.setX(50);
    text.setY(50);


    FlowPane root = new FlowPane();
    root.setColumnHalignment(HPos.CENTER);
    root.setRowValignment(VPos.CENTER);
    root.setVgap(20);

    ScrollPane scroll = new ScrollPane(root);
    root.getChildren().add(text);

    ArrayList<CardController> cardControllers = new ArrayList<CardController>();

    try {
      this.loadCards();
      Scene scene = new Scene(scroll, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();
      for (Card card: cards) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("card/view/Card.fxml"));
        loader.setController(new CardController(card));
        root.getChildren().add(loader.load());
        cardControllers.add(loader.getController());
      }
    } catch (Exception e) {
      text.setText("Failed to load cards: " + e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}