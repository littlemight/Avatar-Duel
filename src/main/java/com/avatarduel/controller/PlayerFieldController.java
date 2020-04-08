package com.avatarduel.controller;

import com.avatarduel.model.card.*;
import com.avatarduel.event.Event;
import com.avatarduel.event.EventChannel;
import com.avatarduel.event.NewCardDrawnEvent;
import com.avatarduel.event.Publisher;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Character;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerFieldController implements Initializable, Publisher {
    @FXML
    public GridPane player_zone;

    private Pane[][] zone_panes;

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

    private List<CardController> cardcontrollers_on_hand;

    private CardController dragged_card_controller; // dragging card from hand to field / element (buat land)

    private EventChannel channel;

    private Player player;

    private int character_row, skill_row;

    int col = 0, row = 0;

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
     * Ini supaya vbox nya bisa di drag and drop :')
     */
    private static final DataFormat vbox_format = new DataFormat("javafx.scene.layout.VBox");

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
            loader.load();
            CardController controller = loader.getController();
            cardcontrollers_on_hand.add(controller);
            Card drawn_card = this.player.drawCard();
            // if draw_card instanceof emptycard, then dont do anything, maybe send FailedDrawEvent

            channel.addPublisher(controller);
            this.publish(new NewCardDrawnEvent(controller));

            controller.setCard(drawn_card);
            StackPane card_box = controller.getContent();

            card_box.prefHeightProperty().bind(this.player_hand.prefHeightProperty().subtract(10)); // kurang 10 gara2 padding
            this.player_hand.getChildren().add(card_box);


            if (drawn_card instanceof Character) {
                VBox card_front = controller.getCardFront();
                card_front.setCursor(Cursor.HAND);
                card_front.setOnDragDetected(e -> {
                    Dragboard db = card_front.startDragAndDrop(TransferMode.ANY);
                    db.setDragView(card_front.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(vbox_format, "Card Front");
                    db.setContent(cc);
                    System.out.println("YA");
                    dragged_card_controller = controller;
                });
            }
        } catch (Exception e) {
            System.out.println("IN DRAW: " + e);
        }
    }

    public StackPane addSummonedCharacterBox(SummonedCharacter summoned_character) {
        StackPane summoned_character_box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SummonedCharacter.fxml"));
            loader.setControllerFactory(c -> new SummonedCharacterController(this.channel));
            summoned_character_box = loader.load();

            SummonedCharacterController controller = loader.getController();
            controller.setSummonedCharacter(summoned_character);

            summoned_character_box.prefWidthProperty().bind(player_zone.prefWidthProperty().divide(8));
            summoned_character_box.prefHeightProperty().bind(player_zone.prefHeightProperty().divide(2));
        } catch (Exception e) {
            System.out.println("ADDING CARD TO ZONE: " + e);
        }
        return summoned_character_box;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        character_row = 0;
        skill_row = 1;
        zone_panes = new Pane[2][8];
        cardcontrollers_on_hand = new ArrayList<CardController>();
        dragged_card_controller = null;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                addPane(j, i);
            }
        }
    }

    private void addPane(int col, int row) {
        zone_panes[row][col] = new Pane();
        player_zone.add(zone_panes[row][col], col, row);

        zone_panes[row][col].setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format)
                    && dragged_card_controller != null
            ) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
        });

        zone_panes[row][col].setOnDragDropped (e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format) && zone_panes[row][col].getChildren().isEmpty()) {
                Card dragged_card = dragged_card_controller.getCard();

                if ((dragged_card instanceof Character && row == character_row) ||
                    (dragged_card instanceof Skill && row == skill_row)
                ) {
                    if (player.canSummon((Summonable)dragged_card)) {
                        Node dragged_card_box = dragged_card_controller.getContent();
                        ((Pane)dragged_card_box.getParent()).getChildren().remove(dragged_card_box);

                        // coba character dulu
                        SummonedCharacter card = player.summonCharacter((Character)dragged_card_controller.getCard());
                        zone_panes[row][col].getChildren().add(this.addSummonedCharacterBox(card));
                    }
                }
            }
        });
    }

    /**
     * Flips character and skill row, used for player2
     */
    public void flipRow() {
        character_row ^= 1;
        skill_row ^= 1;
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }
}
