package com.avatarduel.controller;

import com.avatarduel.event.Event;
import com.avatarduel.event.EventChannel;
import com.avatarduel.event.NewCardDrawnEvent;
import com.avatarduel.event.Publisher;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerFieldController implements Initializable, Publisher {
    @FXML
    public GridPane player_zone;

    @FXML
    public Label earth_power, fire_power, water_power, air_power;

    @FXML
    public Label earth_max, fire_max, water_max, air_max;

    @FXML
    public HBox player_hand;

    @FXML
    public Label player_hp_value;

    @FXML
    public ProgressBar player_hp_bar;

    @FXML
    public Label player_name;

    @FXML
    public Label deck_neff, deck_size;

    @FXML
    ImageView player_photo;

    @FXML
    public AnchorPane player_field;

    private EventChannel channel;

    private Player player;


    public PlayerFieldController(EventChannel channel) {
        this.channel = channel;
        this.player = new Player();
    }

    public void setPlayer(Player player) {
        this.player = player;

        this.player_hp_value.textProperty().bind(this.player.getHealthProperty().asString());
        this.player_hp_bar.progressProperty().bind(this.player.getHealthProperty().divide(80));
        this.deck_neff.textProperty().bind(this.player.getDeck().getNeff().asString());
        this.deck_size.setText(Integer.toString(this.player.getDeck().getSize()));
        this.player_name.setText(this.player.getName());
    }

    /**
     * The draw function should be called a method of player/a MainController, this controller should only update the player field UI
     * In the meantime, this function is for testing purposes only
     *
     * Draws from a card and puts it into the hand field
     */
    public void draw() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(this.channel));

        try{
            // publish new AddedHoverableCard event
            loader.load();
            CardController controller = loader.getController();
            Card drawn_card = this.player.drawCard();
            // if draw_card instanceof emptycard, then dont do anything, maybe send FailedDrawEvent

            channel.addPublisher(controller);
            this.publish(new NewCardDrawnEvent(controller));

            controller.setCard(drawn_card);
            StackPane card_box = controller.getContent();

            card_box.prefHeightProperty().bind(this.player_hand.prefHeightProperty().subtract(10)); // kurang 10 gara2 padding
            this.player_hand.getChildren().add(card_box);
        } catch (Exception e) {
            System.out.println("IN DRAW: " + e);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
