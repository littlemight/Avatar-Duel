package com.avatarduel;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.controller.BoardController;
import com.avatarduel.event.BoardChannel;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AvatarDuel extends Application {
  @Override
  public void start(Stage stage) {
    try {
      BoardChannel channel = new BoardChannel();
      FXMLLoader board_loader = new FXMLLoader(getClass().getResource("view/Board.fxml"));
      board_loader.setControllerFactory(c -> new BoardController(channel));
      Parent root = board_loader.load();

      BoardController board_controller = board_loader.getController();
      channel.setMain(board_controller);

      Dealer dealer = new Dealer();
      Player player1 = new Player("Aang", dealer.getDeck(ThreadLocalRandom.current().nextInt(40, 60 + 1)), channel);
      Player player2 = new Player("The Aang", dealer.getDeck(ThreadLocalRandom.current().nextInt(40, 60 + 1)), channel);
      Game game_engine = new Game(player1, player2, channel);
      
      Media media = new Media(getClass().getResource("sfx/music.mp3").toURI().toString());
      MediaPlayer music = new MediaPlayer(media);
      music.setStartTime(Duration.seconds(0));
      music.setStopTime(Duration.seconds(59));
      music.setAutoPlay(true);
      music.setCycleCount(MediaPlayer.INDEFINITE);
      music.play();

      Scene scene = new Scene(root, 1280, 720);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();
      

      board_controller.startGame(game_engine);
    } catch (Exception e) {
        System.out.println("WTF: " + e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}