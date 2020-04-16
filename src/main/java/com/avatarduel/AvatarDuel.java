package com.avatarduel;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.controller.BoardController;
import com.avatarduel.controller.CardController;
import com.avatarduel.event.BoardChannel;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;
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
import javafx.stage.StageStyle;

// currently for testing purposes
public class AvatarDuel extends Application {

  @Override
  public void start(Stage stage) {

//    ArrayList<CardController> cardControllers = new ArrayList<CardController>();
    try {
      BoardChannel channel = new BoardChannel();
      FXMLLoader board_loader = new FXMLLoader(getClass().getResource("view/Board.fxml"));
      board_loader.setControllerFactory(c -> new BoardController(channel));
      Parent root = board_loader.load();

      BoardController board_controller = board_loader.getController();
      channel.setMain(board_controller);

      Dealer dealer = new Dealer();
      Player player1 = new Player("Aang", dealer.getDeck(ThreadLocalRandom.current().nextInt(40, 60 + 1)), channel);
      Player player2 = new Player("orAang Ganteng", dealer.getDeck(ThreadLocalRandom.current().nextInt(40, 60 + 1)), channel);

      board_controller.setPlayer(1, player1);
      board_controller.setPlayer(2, player2);


      Scene scene = new Scene(root, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();

      Game game_engine = new Game(player1, player2, channel);
      board_controller.startGame(game_engine);
    } catch (Exception e) {
        System.out.println("WTF: " + e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}