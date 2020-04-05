package com.avatarduel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.avatarduel.controller.BoardController;
import com.avatarduel.controller.CardController;
import com.avatarduel.model.card.Aura;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.Land;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
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
//    FlowPane root = new FlowPane();
//    ScrollPane scroll = new ScrollPane(root);
//    root.setMinHeight(720);
//    root.setMinWidth(1280);
//    root.setAlignment(Pos.CENTER);
//
//    root.setHgap(10);
//    root.setVgap(10);

    ArrayList<CardController> cardControllers = new ArrayList<CardController>();

    try {
      this.loadCards();
      FXMLLoader board_loader = new FXMLLoader(getClass().getResource("view/Board.fxml"));
      Parent root = board_loader.load();
      BoardController board_controller = board_loader.getController();

      Scene scene = new Scene(root, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();

      for (Card card: cards) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(card));
        VBox card_view = loader.load();
        board_controller.addCard(card_view);
        cardControllers.add(loader.getController());
      }
    } catch (Exception e) {
        System.out.println(e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}