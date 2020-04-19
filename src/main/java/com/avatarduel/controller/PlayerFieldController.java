package com.avatarduel.controller;

import com.avatarduel.event.*;
import com.avatarduel.model.Element;
import com.avatarduel.model.Phase;
import com.avatarduel.model.card.*;
import com.avatarduel.event.CharacterSelectedEvent;
import com.avatarduel.event.Event;
import com.avatarduel.event.NewCardDrawnEvent;
import com.avatarduel.event.Publisher;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Character;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
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
    public HBox player_deck;

    @FXML
    ImageView player_photo;

    @FXML
    public AnchorPane player_field;

    private List<CardController> cardcontrollers_on_hand;
    private List<SummonedCharacterController> summonedchara_controllers;
    private List<SummonedSkillController> summonedskill_controllers;
    private CardController dragged_card_controller;
    private BoardChannel channel;
    private Player player;
    private int player_id;
    private int character_row, skill_row;

    /**
     * Constructor for PlayerFieldController
     * @param channel channel for publishing/subscribing events
     * @see BoardController
     */
    public PlayerFieldController(BoardChannel channel) {
        this.channel = channel;

    }

    /**
     * Sets the player in control.
     * Loads information from the player and sets corresponding resources of the player.
     * Sets the {@code card_front} to be draggable
     * @param player_id player's id
     * @param player player to be controlled
     */
    public void setPlayer(int player_id, Player player) {
        this.player_id = player_id;
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

    private static final DataFormat vbox_format = new DataFormat("javafx.scene.layout.VBox");


    public void draw(Card drawn_card) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
        loader.setControllerFactory(c -> new CardController(this.channel));

        try{
            loader.load();
            CardController controller = loader.getController();
            cardcontrollers_on_hand.add(controller);
            channel.addSubscriber(controller, (Subscriber) channel.getMain());

            channel.addPublisher(controller);
            this.publish(new NewCardDrawnEvent(controller));

            controller.setCard(drawn_card);
            StackPane card_box = controller.getContent();

            card_box.prefHeightProperty().bind(this.player_hand.prefHeightProperty().subtract(10));
            this.player_hand.getChildren().add(card_box);
            drawTransition(card_box);

            VBox card_front = controller.getCardFront();
            card_front.setCursor(Cursor.HAND);
            card_front.setOnDragDetected(e -> {
                if (this.channel.getPhase()==Phase.MAIN) {
                    card_front.setEffect(null);
                    Dragboard db = card_front.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(card_front.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(vbox_format, "Card Front");
                    db.setContent(cc);
                    dragged_card_controller = controller;
                }
            });
            card_front.setOnDragDone(e -> {
                this.setHandHinting(true);
            });
        } catch (Exception e) {
            System.out.println("IN DRAW: " + e);
        }
    }

    /**
     * Animate card to move when mouse the player draws card.
     * @param card_box the card box to be animated
     */
    public void drawTransition(StackPane card_box){
        TranslateTransition move_card = new TranslateTransition(Duration.millis(700), card_box);
        move_card.setFromX(200);
        move_card.setToX(0);
        move_card.play();
    }

    /**
     * Put summoned character into a controller.
     * Adds its subscriber, owner, and position, then return the
     * summoned character's box.
     * @param summoned_character summoned character to be put into.
     * @param position the summoned character's position in field/zone
     * @return summoned character in box
     */
    public StackPane addSummonedCharacterBox(SummonedCharacter summoned_character, int position) {
        StackPane summoned_character_box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SummonedCharacter.fxml"));
            loader.setControllerFactory(c -> new SummonedCharacterController(this.channel));
            summoned_character_box = loader.load();

            SummonedCharacterController controller = loader.getController();
            this.channel.addSubscriber((BoardController) this.channel.getMain(), controller);
            controller.setSummonedCharacter(summoned_character);
            controller.setOwner(this.player_id);
            controller.setPosition(position);

            summoned_character_box.prefWidthProperty().bind(player_zone.prefWidthProperty().divide(6));
            summoned_character_box.prefHeightProperty().bind(player_zone.prefHeightProperty().divide(2));

            summonedchara_controllers.add(controller);
        } catch (Exception e) {
            System.out.println("ADDING CARD TO ZONE: " + e);
        }
        return summoned_character_box;
    }

    /**
     * Put summoned skill into a controller.
     * Adds its subscriber, owner, and position, then return the
     * summoned skill's box.
     * @param summoned_skill summoned skill to be put into.
     * @param position the summoned skill's position in field/zone
     * @return summoned skill in box
     */
    public StackPane addSummonedSkillBox(SummonedSkill summoned_skill, int position) {
        StackPane summoned_skill_box = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SummonedSkill.fxml"));
            loader.setControllerFactory(c -> new SummonedSkillController(this.channel));
            summoned_skill_box = loader.load();

            SummonedSkillController controller = loader.getController();
            controller.setSummonedSkill(summoned_skill);
            controller.setOwner(this.player_id);
            controller.setPosition(position);

            summoned_skill_box.prefWidthProperty().bind(player_zone.prefWidthProperty().divide(6));
            summoned_skill_box.prefHeightProperty().bind(player_zone.prefHeightProperty().divide(2));

            summonedskill_controllers.add(controller);
        } catch (Exception e) {
            System.out.println("ADDING CARD TO ZONE: " + e);
        }
        return summoned_skill_box;
    }

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the player in control.
     * Sets {@code setOnDragOver} and {@code setOnDragDropped} event handler for consuming {@link Land}.
     * Sets {@code setOnMouseClicked} event handler for Battle Phase interactions.
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

                if (dragged_card instanceof Land) {
                    Node dragged_card_box = dragged_card_controller.getContent();
                    ((Pane)dragged_card_box.getParent()).getChildren().remove(dragged_card_box);

                    this.player.addPower(dragged_card.getElement());
                    player.hasUsedLand = true;
                    player.getHand().remove(dragged_card);
                }
                dragged_card = null;
            }
        });

        player_home.setOnMouseClicked(e -> {
            if (this.channel.getPhase()==Phase.BATTLE){
                if (summonedchara_controllers.isEmpty()){
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
            if (db.hasContent(vbox_format) && dragged_card_controller != null) {
                if (dragged_card_controller.getCard() instanceof Summonable) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
            }
        });

        zone_panes[row][col].setOnDragDropped (e -> {
            Dragboard db = e.getDragboard();
            if (db.hasContent(vbox_format) && zone_panes[row][col].getChildren().isEmpty()) {
                Card dragged_card = dragged_card_controller.getCard();
                if (dragged_card instanceof Skill && !this.player.anyCharOnField) {
                    return;
                }

                if ((dragged_card instanceof Character && row == character_row) ||
                    (dragged_card instanceof Skill && row == skill_row)
                ) {
                    if (player.canSummon((Summonable)dragged_card)) {
                        Node dragged_card_box = dragged_card_controller.getContent();
                        Bounds b = dragged_card_box.localToScene(dragged_card_box.getBoundsInLocal());
                        Bounds target = zone_panes[row][col].localToScene(dragged_card_box.getBoundsInLocal());

                        StackPane cir = new StackPane();
                        cir.setMaxSize(85,119);
                        cir.setMinSize(85,119);
                        cir.setPrefSize(85,119);
                        cir.setStyle("-fx-background-color: BLACK");
                        StackPane mock = null;
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
                            loader.setControllerFactory(c -> new CardController(this.channel));
                            mock = loader.load();
                            mock.setMaxSize(85,119);
                            mock.setMinSize(85,119);
                            cir.getChildren().add(mock);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        // transition
                        TranslateTransition transition = new TranslateTransition();
                        transition.setDuration(Duration.seconds(0.5));
                        transition.setFromX(b.getMinX()-640-43-11);
                        transition.setFromY(b.getMinY()-360+60-30);
                        transition.setToX(target.getMinX()-640-43-11);
                        transition.setToY(target.getMinY()-360+60-30);
                        transition.setNode(cir);

                        // do this thing below
                        ((Pane)dragged_card_box.getParent().getParent()
                                .getParent().getParent().getParent()
                                .getParent().getParent().getParent()).getChildren().add(cir);
                        ((Pane)dragged_card_box.getParent()).getChildren().remove(dragged_card_box);

                        Summoned card = player.summonCard((Summonable) dragged_card_controller.getCard());
                        if (card instanceof SummonedCharacter) {
                            this.channel.addSubscriber((SummonedCharacter) card, this);
                            zone_panes[row][col].getChildren().add(this.addSummonedCharacterBox((SummonedCharacter) card, col));
                        } else {
                            this.channel.addSubscriber((SummonedSkill) card, this);
                            zone_panes[row][col].getChildren().add(this.addSummonedSkillBox((SummonedSkill) card, col));
                        }

                        // fade out
                        FadeTransition ft = new FadeTransition();
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);
                        ft.setDuration(Duration.seconds(0.2));
                        ft.setNode(cir);

                        // move
                        TranslateTransition getOut = new TranslateTransition();
                        getOut.setToX(3000);
                        getOut.setToY(3000);
                        getOut.setDuration(Duration.seconds(0.001));
                        getOut.setNode(cir);


                        SequentialTransition sq = new SequentialTransition();
                        sq.getChildren().addAll(transition,ft, getOut);
                        sq.play();
                        cir.getChildren().remove(mock);
                        ((Pane)dragged_card_box.getParent().getParent()
                                .getParent().getParent().getParent()
                                .getParent().getParent().getParent()).getChildren().remove(cir);
                        sq.stop();

                    }
                }
                dragged_card = null;
            }
        });
        
        zone_panes[row][col].setOnMouseClicked(e -> {
            if (zone_panes[row][col].getChildren().isEmpty()) {
                return;
            }
            switch (this.channel.getPhase()) {
                case MAIN:
                    break;
                case BATTLE:
                    for (SummonedCharacterController chara_controller : summonedchara_controllers){
                        if (chara_controller.getPosition()==col) {
                            publish(new CharacterSelectedEvent(chara_controller, this.player));
                            break;
                        }
                    }
                    break;
                default:
                    break;
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

    /**
     * Sets all controllers in hand to be closed.
     */
    public void closeHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.setClosed();
        }
    }

    /**
     * Sets all controllers in hand to be opened.
     */
    public void openHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.setOpened();
        }
    }

    /**
     * Sets all controllers in hand to be flipped.
     */
    public void flipHand() {
        for (CardController controller: cardcontrollers_on_hand) {
            controller.flipCard();
        }
    }

    /**
     * Getter for summoned character controllers
     * @return controller's {@code summonedchara_controllers} 
     */
    public List<SummonedCharacterController> getSummonedCharaController(){
        return this.summonedchara_controllers;
    }

    /**
     * Sets hinting effect to player's homebase.
     * @param is_hinting true to activate effect
     */
    public void setHinting(boolean is_hinting){
        if (is_hinting){
            this.player_home.setEffect(new DropShadow(50f, Color.CRIMSON));
        }else{
            this.player_home.setEffect(null);
        }
    }

    /**
     * Sets hinting effect to all cards in player's hand.
     * Can also be used to resets hand hinting effect.
     * @param is_hinting true to activate effect
     */
    public void setHandHinting(boolean is_hinting){
        if (is_hinting){
            for (CardController controller : this.cardcontrollers_on_hand){
                if (controller.getCard() instanceof Land){
                    if (!this.player.hasUsedLand){
                        controller.setHinting(true);
                    }else{
                        controller.setHinting(false);
                    }
                }
                else if (this.player.canSummon((Summonable) controller.getCard()))
                    controller.setHinting(true);
                else{
                    controller.setHinting(false);
                }
            }
        }else{
            for (CardController controller : this.cardcontrollers_on_hand){
                controller.setHinting(false);
            }
        }
    }

    /**
     * Implemented from {@link Publisher} to publish to {@link BoardChannel}.
     * @param event event sent to {@link BoardChannel}
     */
    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);
    }

    /**
     * Implemented from {@link Subscriber} to listen from {@link BoardChannel}.
     * @param event event sent from {@link BoardChannel}
     * @see DestroySummonedCardEvent
     * @see CardDrawnEvent
     */
    @Override
    public void onEvent(Event event) {
       if (event instanceof DestroySummonedCardEvent){
           Summoned destroyed_card = (Summoned) event.getInfo();
           this.channel.removeComponent(destroyed_card);
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
