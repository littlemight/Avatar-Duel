package com.avatarduel.controller;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Player;

/**
 * Controller class for Win Scene
 */
public class WinController implements Initializable {
    @FXML
    SplitPane board;

    @FXML
    Label winner;

    @FXML
    MediaView video_port;

    Player player_winner;

    /**
     * Constructor for WinController
     * @param winner player that wins the game
     * @see BoardController
     */
    public WinController(Player winner) {
        this.player_winner = winner;
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Loads transition, player name, and win celebration video resource.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     * @see Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        board.setOpacity(0);
        loadCelebration();
        fadeInTransition();
    }

    /**
     * Make fade in transition to the scene for 2 seconds when it loads
     */
    public void fadeInTransition() {
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.seconds(2));
        fadeIn.setNode(board);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Loads player name and win celebration video resource.
     */
    public void loadCelebration() {
        winner.setText("Congrats! " + this.player_winner.getName() + " wins!");
        try {
            Media media = new Media(getClass().getResource("../video/win.mp4").toURI().toString());
            MediaPlayer video = new MediaPlayer(media);
            video.setStartTime(Duration.seconds(0));
            video.setStopTime(Duration.seconds(19));
            video.setAutoPlay(true);
            video.setCycleCount(MediaPlayer.INDEFINITE);
            video_port.setMediaPlayer(video);
            video.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * exits the Game when window is clicked
     */
    public void exitGame(){
        Platform.exit();
        System.exit(0);
    }
}
