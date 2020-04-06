package com.avatarduel;

import java.util.ArrayList;

import com.avatarduel.controller.BoardController;
import com.avatarduel.controller.CardController;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.card.Card;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// currently for testing purposes
public class AvatarDuel extends Application {

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
      Dealer dealer = new Dealer();
      Deck deck = dealer.getDeck(40);
      FXMLLoader board_loader = new FXMLLoader(getClass().getResource("view/Board.fxml"));
      Parent root = board_loader.load();
      BoardController board_controller = board_loader.getController();

      Scene scene = new Scene(root, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();

      while (deck.getNeffValue()>0) {
        Card card = deck.drawCard();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(card));
        StackPane card_view = loader.load();
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