package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.Element;
import com.avatarduel.model.Phase;
import com.avatarduel.model.card.*;
import com.avatarduel.event.CharacterSelectedEvent;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerFieldController implements Initializable, Publisher, Subscriber {
    @FXML
    public GridPane player_zone;

    private Pane[][] zone_panes;

    @FXML
    public GridPane power_pane;

    @FXML
    public Label earth_power, fire_power, water_power, air_power, energy_power, earth_max, fire_max, water_max, air_max, energy_max;

    @FXML
    public HBox player_hand;

    @FXML
    public Label player_hp_value;

    @FXML
    public ProgressBar player_hp_bar;

    @FXML
    public Label player_name;

    @FXML
    public HBox player_home;

    @FXML
    public Label deck_neff, deck_size;

    @FXML
    ImageView player_photo;

    @FXML
    public AnchorPane player_field;

//    private Phase phase = Phase.DRAW;

    private List<CardController> cardcontrollers_on_hand;
    private List<SummonedCharacterController> summonedchara_controllers;
    private List<SummonedSkillController> summonedskill_controllers;

    private CardController dragged_card_controller; // dragging card from hand to field / element (buat land)

    private BoardChannel channel;

    private Player player;

    private int character_row, skill_row;

    public PlayerFieldController(BoardChannel channel) {
        this.channel = channel;
        this.player = new Player();

    }

    public void setPlayer(Player player) {
        this.player = player;
        this.channel.addSubscriber(this.player, this);

        this.player_hp_value.
                textProperty().
                bind(this.player.getHealthProperty().asString());
        this.player_hp_bar.
                progressProperty().
                bind(this.player.getHealthProperty().divide((double)80));
        this.deck_neff.
                textProperty().
                bind(this.player.getDeck().getNeff().asString());
        this.deck_size.
                setText(Integer.toString(this.player.getDeck().getSize()));
        this.player_name.
                setText(this.player.getName());

        this.earth_power.
                textProperty().
                bind(this.player.getPowerProperty(Element.EARTH).asString());
        this.earth_max.
                textProperty().
                bind(this.player.getMaxPowerProperty(Element.EARTH).asString());
        this.fire_power.
                textProperty().
                bind(this.player.getPowerProperty(Element.FIRE).asString());
        this.fire_max.
                textProperty().
                bind(this.player.getMaxPowerProperty(Element.FIRE).asString());
        this.water_power.
                textProperty().
                bind(this.player.getPowerProperty(Element.WATER).asString());
        this.water_max.
                textProperty().
                bind(this.player.getMaxPowerProperty(Element.WATER).asString());
        this.air_power.
                textProperty().
                bind(this.player.getPowerProperty(Element.AIR).asString());
        this.air_max.
                textProperty().
                bind(this.player.getMaxPowerProperty(Element.AIR).asString());
        this.energy_power.
                textProperty().
                bind(this.player.getPowerProperty(Element.ENERGY).asString());
        this.energy_max.
                textProperty().
                bind(this.player.getMaxPowerProperty(Element.ENERGY).asString());
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
    public void draw(Card drawn_card) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(this.channel));

        try{
            loader.load();
            CardController controller = loader.getController();
            cardcontrollers_on_hand.add(controller);
            // Card drawn_card = this.player.drawCard();
            // if draw_card instanceof emptycard, then dont do anything, maybe send FailedDrawEvent

            channel.addPublisher(controller);
            this.publish(new NewCardDrawnEvent(controller));

            controller.setCard(drawn_card);
            StackPane card_box = controller.getContent();

            card_box.prefHeightProperty().bind(this.player_hand.prefHeightProperty().subtract(10)); // kurang 10 gara2 padding
            this.player_hand.getChildren().add(card_box);


            /**
             * Sets the card front to be draggable (jadi kalo di flip, card_backnya ga bisa di drag)
             */
            VBox card_front = controller.getCardFront();
            card_front.setCursor(Cursor.HAND);
            card_front.setOnDragDetected(e -> {
                card_front.setEffect(null);
                if (this.channel.getPhase()==Phase.MAIN) {
                    Dragboard db = card_front.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(card_front.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(vbox_format, "Card Front");
                    db.setContent(cc);
                    dragged_card_controller = controller;
                }
            });
            card_front.setOnDragDone(e -> {
                card_front.setEffect(new DropShadow(50f, Color.PALEGREEN));
            });
        } catch (Exception e) {
            System.out.println("IN DRAW: " + e);
        }
    }

    public StackPane addSummonedCharacterBox(SummonedCharacter summoned_character, int position) {
        StackPane summoned_character_box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SummonedCharacter.fxml"));
            loader.setControllerFactory(c -> new SummonedCharacterController(this.channel));
            summoned_character_box = loader.load();

            SummonedCharacterController controller = loader.getController();
            this.channel.addSubscriber((BoardController) this.channel.getMain(), controller);
            controller.setSummonedCharacter(summoned_character);
            controller.setPosition(position);

            summoned_character_box.prefWidthProperty().bind(player_zone.prefWidthProperty().divide(6));
            summoned_character_box.prefHeightProperty().bind(player_zone.prefHeightProperty().divide(2));

            summonedchara_controllers.add(controller);
        } catch (Exception e) {
            System.out.println("ADDING CARD TO ZONE: " + e);
        }
        return summoned_character_box;
    }

    public StackPane addSummonedSkillBox(SummonedSkill summoned_skill, int position) {
        StackPane summoned_skill_box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SummonedSkill.fxml"));
            loader.setControllerFactory(c -> new SummonedSkillController(this.channel));
            summoned_skill_box = loader.load();

            SummonedSkillController controller = loader.getController();
            controller.setSummonedCharacter(summoned_skill);
            controller.setPosition(position);

            summoned_skill_box.prefWidthProperty().bind(player_zone.prefWidthProperty().divide(6));
            summoned_skill_box.prefHeightProperty().bind(player_zone.prefHeightProperty().divide(2));

            summonedskill_controllers.add(controller);
        } catch (Exception e) {
            System.out.println("ADDING CARD TO ZONE: " + e);
        }
        return summoned_skill_box;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        character_row = 0;
        skill_row = 1;
        zone_panes = new Pane[2][6];
        cardcontrollers_on_hand = new ArrayList<CardController>();
        summonedchara_controllers = new ArrayList<SummonedCharacterController>();
        summonedskill_controllers = new ArrayList<SummonedSkillController>();
        dragged_card_controller = null;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                addPane(j, i);
            }
        }

        power_pane.setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format)
                    && dragged_card_controller != null
            ) {
                if (!(dragged_card_controller.getCard() instanceof Summonable)) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        power_pane.setOnDragDropped (e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format)
                && player.hasUsedLand == false
            ) {
                Card dragged_card = dragged_card_controller.getCard();

                /**
                 * Only receive summonable card
                 */
                if (dragged_card instanceof Land) {
                    Node dragged_card_box = dragged_card_controller.getContent();
                    ((Pane)dragged_card_box.getParent()).getChildren().remove(dragged_card_box);

                    this.player.addPower(dragged_card.getElement());
                    player.hasUsedLand = true;
                }
                dragged_card = null;
            }
        });

        player_home.setOnMouseClicked(e -> {
            if (this.channel.getPhase()==Phase.BATTLE){
                System.out.println(summonedchara_controllers.size());
                if (summonedchara_controllers.isEmpty()){
                    // chara_controller.toggleSelected();
                    publish(new PlayerSelectedEvent(this.player));
                }
            }
        });
    }

    private void addPane(int col, int row) {
        zone_panes[row][col] = new Pane();
        player_zone.add(zone_panes[row][col], col, row);

        zone_panes[row][col].setOnDragOver(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format)
                    && dragged_card_controller != null
            ) {
                if (dragged_card_controller.getCard() instanceof Summonable) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        zone_panes[row][col].setOnDragDropped (e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format) && zone_panes[row][col].getChildren().isEmpty()) {
                Card dragged_card = dragged_card_controller.getCard();
                if (dragged_card instanceof Skill && !this.player.canSummonSkill) {
                    System.out.println("No character in both player fields.");
                    return;
                }


                /**
                 * Only receive summonable card
                 */
                if ((dragged_card instanceof Character && row == character_row) ||
                    (dragged_card instanceof Skill && row == skill_row)
                ) {
                    if (player.canSummon((Summonable)dragged_card)) {
                        Node dragged_card_box = dragged_card_controller.getContent();
                        ((Pane)dragged_card_box.getParent()).getChildren().remove(dragged_card_box);

                        Summoned card = player.summonCard((Summonable) dragged_card_controller.getCard());
                        if (card instanceof SummonedCharacter) {
                            this.channel.addSubscriber((SummonedCharacter) card, this);
                            zone_panes[row][col].getChildren().add(this.addSummonedCharacterBox((SummonedCharacter) card, col));
                        } else { // its a SummonedSkill
                            this.channel.addSubscriber((SummonedSkill) card, this);
                            zone_panes[row][col].getChildren().add(this.addSummonedSkillBox((SummonedSkill) card, col));
                        }
                    }
                }
                dragged_card = null;
            }
        });
        
        // if (row==this.character_row){
        zone_panes[row][col].setOnMouseClicked(e -> {
            if (this.channel.getPhase()==Phase.BATTLE){
                if (!zone_panes[row][col].getChildren().isEmpty()){
                    for (SummonedCharacterController chara_controller :summonedchara_controllers){
                        if (chara_controller.getPosition()==col) {
                            // chara_controller.toggleSelected();
                            publish(new CharacterSelectedEvent(chara_controller, this.player));
                            break;
                        }
                    }
                }
            }
        });
        // }
    }

    public void setSkillPickBehavior() {
        for (SummonedCharacterController controller: this.summonedchara_controllers) {
            controller.setSkillPickBehavior();
        }
    }

    public void undoSkillPickBehavior() {
        for (SummonedCharacterController controller: this.summonedchara_controllers) {
            controller.undoSkillPickBehavior();
        }
    }

    /**
     * Flips character and skill row, used for player2
     */
    public void flipRow() {
        character_row ^= 1;
        skill_row ^= 1;
    }

    public void closeHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.setClosed();
        }
    }

    public void openHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.setOpened();
        }
    }

    public void flipHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.flipCard();
        }
    }

    public List<SummonedCharacterController> getSummonedCharaController(){
        return this.summonedchara_controllers;
    }

    public void setHinting(boolean is_hinting){
        if (is_hinting){
            this.player_home.setEffect(new DropShadow(50f, Color.CRIMSON));
        }else{
            this.player_home.setEffect(null);
        }
    }

    public void setHandHinting(boolean is_hinting){
        if (is_hinting){
            for (CardController controller : this.cardcontrollers_on_hand){
                if (controller.getCard() instanceof Land){
                    controller.setHinting(true);
                }
                else if (this.player.canSummon((Summonable) controller.getCard()))
                    controller.setHinting(true);
            }
        }else{
            for (CardController controller : this.cardcontrollers_on_hand){
                controller.setHinting(false);
            }
        }
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }

    @Override
    public void onEvent(Event event) {
       if (event instanceof DestroyCardEvent){
           Summoned destroyed_card = (Summoned) event.getInfo();
           if (destroyed_card instanceof SummonedCharacter){
               for (SummonedCharacterController summonedcard_controller : this.summonedchara_controllers){
                   if (summonedcard_controller.summoned_character.equals(destroyed_card)){
                       this.summonedchara_controllers.remove(summonedcard_controller);
                       break;       
                    } 
                }
                this.player.getCharacterZone().remove(destroyed_card);
           } else if (destroyed_card instanceof SummonedSkill){
               for (SummonedSkillController summonedcard_controller : this.summonedskill_controllers){
                   if (summonedcard_controller.summoned_skill.equals(destroyed_card)){
                       this.summonedskill_controllers.remove(summonedcard_controller);
                       break;
                   } 
                }
                this.player.getSkillZone().remove(destroyed_card);
        }
       } else if (event instanceof CardDrawnEvent){
           this.draw((Card) event.getInfo());
       }
    }
}
