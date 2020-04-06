package com.avatarduel;

import java.util.ArrayList;

import com.avatarduel.controller.CardController;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.card.Card;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// currently for testing purposes
public class AvatarDuel extends Application {

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
      Dealer dealer = new Dealer();
      Deck deck = dealer.getDeck(40);
      Scene scene = new Scene(scroll, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();
      while (deck.getNeffValue()>0) {
        Card card = deck.drawCard();
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