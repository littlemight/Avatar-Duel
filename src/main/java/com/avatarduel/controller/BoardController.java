package com.avatarduel.controller;

import com.avatarduel.Game;
import com.avatarduel.event.*;
import com.avatarduel.model.AlertBox;
import com.avatarduel.model.Phase;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable, Publisher, Subscriber {
    @FXML
    SplitPane board;

    @FXML
    Pane hover_card_pane;

    @FXML
    VBox hover_card_view;

    @FXML
    VBox summoned_box;

    @FXML
    Label summoned_name, summoned_description;

    @FXML
    Pane player1_pane;

    @FXML
    Pane player2_pane;

    @FXML
    Label dp_label, mp_label, bp_label, ep_label;

    @FXML
    Button next_phase_btn;

    Label[] phase_bar;
    AnchorPane[] player_fields;
    MediaPlayer music;

    /**
     * The model It should be a class Game which has the deck, 2 players, and other
     * game rules for each phase
     */
    PlayerFieldController[] player_controllers;
    Game game_engine;

    StackPane hover_card_box;
    private CardController hover_card_controller;
    private BoardChannel channel;
    private ArrayList<SummonedCharacterController> targeting;
    SummonedSkill placed_skill;

    /**
     * Constructor for BoardController
     * @param channel channel for publishing/subscribing events
     */
    public BoardController(BoardChannel channel) {
        this.channel = channel;
        this.player_controllers = new PlayerFieldController[3];
        this.player_fields = new AnchorPane[3];
    }

    Phase[] phases = new Phase[] { Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END };
    int phase_id = 0;

    /**
     * JavaFX FMXL initialize method.
     * It is automatically called after loading the controller and its
     * parameters is automatically injected by JavaFX<br>
     * Binds FXML properties with the elements on board.
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
        try {
            board.setOpacity(0);
            /**
             * Initialize the hover card pane
             */
            FXMLLoader hover_card_loader = new FXMLLoader(getClass().getResource("../view/Card.fxml"));
            hover_card_loader.setControllerFactory(c -> new CardController(this.channel));
            hover_card_box = hover_card_loader.load();

            hover_card_controller = hover_card_loader.getController();

            hover_card_box.prefHeightProperty().bind(hover_card_pane.prefHeightProperty());
            hover_card_box.prefWidthProperty().bind(hover_card_pane.prefWidthProperty());
            hover_card_pane.getChildren().add(hover_card_box);

            /**
             * Initialize player 1 field
             */
            FXMLLoader player1_loader = new FXMLLoader(getClass().getResource("../view/Player1Field.fxml"));
            player1_loader.setControllerFactory(c -> new PlayerFieldController(this.channel));
            player_fields[1] = player1_loader.load();
            this.player_controllers[1] = player1_loader.getController();
            this.player1_pane.getChildren().add(player_fields[1]);

            /**
             * Initialize player 2 field
             */
            FXMLLoader player2_loader = new FXMLLoader(getClass().getResource("../view/Player2Field.fxml"));
            player2_loader.setControllerFactory(c -> new PlayerFieldController(this.channel));
            player_fields[2] = player2_loader.load();
            this.player_controllers[2] = player2_loader.getController();
            this.player2_pane.getChildren().add(player_fields[2]);

            this.channel.addSubscriber(player_controllers[1], this);
            this.channel.addSubscriber(player_controllers[2], this);
            this.channel.addSubscriber(this, player_controllers[1]);
            this.channel.addSubscriber(this, player_controllers[2]);

            summoned_name.setText("");
            summoned_description.setText("");

            /**
             * Initialize the phase change mid bar
             */
            phase_bar = new Label[] { dp_label, mp_label, bp_label, ep_label };
            this.next_phase_btn.setOnAction(e -> {
                switch (this.channel.getPhase()) {
                    case SKILLPICK:
                        break;
                    default:
                        this.proceedPhase(e);
                        break;
                }
            });
            this.targeting = new ArrayList<SummonedCharacterController>(2);

            Media media = new Media(getClass().getResource("../sfx/music.mp3").toURI().toString());
            this.music = new MediaPlayer(media);
            this.music.setStartTime(Duration.seconds(0));
            this.music.setStopTime(Duration.seconds(59));
            this.music.setAutoPlay(true);
            this.music.setCycleCount(MediaPlayer.INDEFINITE);
            this.music.play();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Starts the game engine and subscribe to it.
     * @param game_engine the game engine
     */
    public void startGame(Game game_engine) {
        this.game_engine = game_engine;
        this.channel.addSubscriber(game_engine, this);
        this.channel.addSubscriber(this, game_engine);
        this.channel.setPlayerID(1);
        for (int i = 1; i <= 2; i++) {
            player_controllers[i].setPlayer(i, game_engine.getPlayer(i));
            if (i == 2) {
                player_controllers[i].flipRow();
            }
        }
        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setDuration(Duration.seconds(1));
        fadeIn.setNode(this.board);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(e->{
        this.game_engine.setup();
        this.player_controllers[2].closeHand();
            sleep(500,true);
        });
        fadeIn.play();
    }

    /**
     * Implemented from {@link Subscriber} to listen from {@link BoardChannel}.
     * @param event event sent from {@link BoardChannel}
     * @see NewCardDrawnEvent
     * @see NewSummonedCardEvent
     * @see NewSkillCardPlaced
     * @see SkillCharacterPickedEvent
     * @see CharacterSelectedEvent
     * @see PlayerSelectedEvent
     * @see WinEvent
     * @see DestroyCardEvent
     * @see HoverCardEvent
     */
    @Override
    public void onEvent(Event event) {
        if (event instanceof NewCardDrawnEvent || event instanceof NewSummonedCardEvent) {
            CardController controller = (CardController) event.getInfo();
            this.channel.addSubscriber(controller, this.hover_card_controller);
        } else if (event instanceof HoverSummonedCardEvent) {
            Summoned summoned_card = (Summoned) event.getInfo();
            if (summoned_card == null) {
                this.summoned_name.setText("");
                this.summoned_description.setText("");
                return;
            }

            this.summoned_name.setText(summoned_card.getBaseCard().getName());
            String description = "";
            description += summoned_card.getBaseCard().getDescription() + "\n\n";
            if (summoned_card instanceof SummonedCharacter) {
                int d_atk = ((SummonedCharacter) summoned_card).getDAtk();
                int d_def = ((SummonedCharacter) summoned_card).getDDef();
                Character character = (Character) summoned_card.getBaseCard();
                int atk = character.getAtk();
                int def = character.getDef();

                description += "ATK: " + atk + (d_atk != 0 ? (d_atk > 0 ? " + " : " - ") + Math.abs(d_atk) : "") + "\n";
                description += "DEF: " + def + (d_def != 0 ? (d_def > 0 ? " + " : " - ") + Math.abs(d_def) : "") + "\n";
                description += "Skills atttached:\n";
                if (((SummonedCharacter) summoned_card).getAttachedSkills().isEmpty()) {
                    description += "None\n";
                } else {
                    for (SummonedSkill summoned_kill : ((SummonedCharacter) summoned_card).getAttachedSkills()) {
                        Skill skill = (Skill) summoned_kill.getBaseCard();
                        description += skill.getName() + "\n";
                    }
                }
            } else if (summoned_card instanceof SummonedSkill) {
                Skill base_card = (Skill) summoned_card.getBaseCard();
                if (base_card instanceof Aura) {
                    description += "+ATK: " + ((Aura) base_card).getDeltaAtk() + "\n";
                    description += "+DEF: " + ((Aura) base_card).getDeltaAtk() + "\n";
                } else if (base_card instanceof PowerUp) {
                    description += "Powers up a character\n";
                } else if (base_card instanceof Destroy) {
                    description += "Destroys a chosen character card\n";
                }
                description += "Attached to:\n";
                if (((SummonedSkill) summoned_card).getAppliedTo() == null) {
                    description += "NONE";
                } else {
                    description += ((SummonedSkill) summoned_card).getAppliedTo().getBaseCard().getName();
                }
            }
            this.summoned_description.setText(description);
        } else if (event instanceof NewSkillCardPlaced) {
            board.setCursor(Cursor.CROSSHAIR);
            placed_skill = (SummonedSkill) event.getInfo();
            this.channel.setPhase(Phase.SKILLPICK);
        } else if (event instanceof SkillCharacterPickedEvent) {
            board.setCursor(Cursor.DEFAULT);
            SummonedCharacterController controller = (SummonedCharacterController) event.getInfo();
            placed_skill.setAppliedTo(controller.getSummonedCharacter());
            controller.getSummonedCharacter().addSkill(placed_skill);
            this.channel.setPhase(Phase.MAIN);
            placed_skill = null;
        } else if (event instanceof CharacterSelectedEvent) {
            ArrayList selected = (ArrayList) event.getInfo();
            SummonedCharacterController selected_card = (SummonedCharacterController) selected.get(0);
            Player selected_card_player = (Player) selected.get(1);
            if (this.targeting.isEmpty()) {
                if (game_engine.canTarget(selected_card.summoned_character)
                        && selected_card_player == game_engine.getPlayer(this.channel.getPlayerID())) {
                    selected_card.toggleSelected();
                    for (SummonedCharacterController summonedchara_controller : this.player_controllers[this.channel
                            .getPlayerID() % 2 + 1].getSummonedCharaController()) {
                        if (game_engine.isStronger(selected_card.summoned_character,
                                summonedchara_controller.summoned_character)) {
                            summonedchara_controller.setHinting(true);
                        }
                    }
                    if (game_engine.canDirectAttack()) {
                        this.player_controllers[this.channel.getPlayerID() % 2 + 1].setHinting(true);
                    }
                    this.targeting.add(selected_card);
                }
            }
            else {
                selected_card.toggleSelected();
                this.targeting.add(selected_card);
                if (selected_card_player == game_engine.getPlayer(this.channel.getPlayerID() % 2 + 1)) {
                    game_engine.solveBattle(this.targeting.get(0).summoned_character,
                            this.targeting.get(1).summoned_character);
                }
                this.targeting.get(0).toggleSelected();
                this.targeting.get(1).toggleSelected();
                for (SummonedCharacterController summonedchara_controller : this.player_controllers[this.channel
                        .getPlayerID() % 2 + 1].getSummonedCharaController()) {
                    summonedchara_controller.setHinting(false);
                }
                this.player_controllers[this.channel.getPlayerID() % 2 + 1].setHinting(false);
                this.targeting.clear();
            }
        } else if (event instanceof PlayerSelectedEvent) {
            Player selected_player = (Player) event.getInfo();
            if (!this.targeting.isEmpty()) {
                if (selected_player == game_engine.getPlayer(this.channel.getPlayerID() % 2 + 1)) {
                    game_engine.solveDirectAttack(this.targeting.get(0).summoned_character);
                }
                this.targeting.get(0).toggleSelected();
                this.player_controllers[this.channel.getPlayerID() % 2 + 1].setHinting(false);
                this.targeting.clear();
            }
        } else if (event instanceof WinEvent) {
            this.music.stop();
            this.changeWinScene((Player) event.getInfo());
        } else if (event instanceof DestroyCardEvent) {
            Card card = (Card) event.getInfo();
            channel.removeComponent(card);
            int prev_player = game_engine.getCurPlayer() % 2 + 1;
            this.game_engine.getPlayer(prev_player).getHand().remove(card);
            this.channel.setPhase(Phase.END);
        }
        this.game_engine.getPlayer(1).anyCharOnField = this.game_engine.getPlayer(2).anyCharOnField =
                this.game_engine.getPlayer(1).getCharacterZone().size() > 0 || this.game_engine.getPlayer(2).getCharacterZone().size() > 0;
    }

    /**
     * Event handler for NP button ({@code next_phase_btn}) clicked.
     * Circulates effects and phase-specific events.
     * @param actionEvent event captured.
     */
    public void proceedPhase(ActionEvent actionEvent) {
        phase_bar[phase_id].setStyle("-fx-background-color: darkgray;" + "-fx-color: dimgray");
        phase_id++;

        switch (phase_id) {
            case 1:
                this.player_controllers[this.channel.getPlayerID()].setHandHinting(true);
                break;
            case 2:
                this.player_controllers[this.channel.getPlayerID()].setHandHinting(false);
                break;
            case 3:
                if (!this.targeting.isEmpty()) {
                    this.targeting.get(0).toggleSelected();
                    for (SummonedCharacterController summonedchara_controller : this.player_controllers[this.channel
                            .getPlayerID() % 2 + 1].getSummonedCharaController()) {
                        summonedchara_controller.setHinting(false);
                    }
                    this.player_controllers[this.channel.getPlayerID() % 2 + 1].setHinting(false);
                    this.targeting.clear();
                }
                break;
            case 4:
                int prev_player = game_engine.getCurPlayer() % 2 + 1;
                if (this.game_engine.getPlayer(prev_player).getHand().size() > Player.MAX_HAND) {
                    phase_id--;
                    phase_bar[phase_id].setStyle("-fx-background-color: aquamarine;" + "-fx-color: black");
                    this.channel.setPhase(Phase.DISCARD);
                    AlertBox.display(1280 / 1.5, 720 / 1.5, "Hand card limit exceeded", "Discard one card to continue.\nDouble Click to Discard.");
                    return;
                }

                for (int i = 1; i <= 2; i++) {
                    this.player_controllers[i].flipHand();
                }
                break;
            default:
                break;
        }

        phase_id %= 4;
        this.game_engine.stageController(phases[phase_id]);
        if (phase_id == 0) {
            this.channel.setPlayerID(this.game_engine.getCurPlayer());
        }
        this.channel.setPhase(phases[phase_id]);

        phase_bar[phase_id].setStyle("-fx-background-color: aquamarine;" + "-fx-color: black");
        if (phase_id==0) {sleep(500, true);}
    }

    /**
     * Make delay transition and activate NP Button click if 
     * {@code is_skip} is active. Also disables NP Button for awhile
     * and after transition it will be enabled again.
     * @param ms delay time
     * @param is_skip triggers next_phase_btn
     */
    public void sleep(double ms, boolean is_skip) {
        next_phase_btn.setDisable(true);
        TranslateTransition pause = new TranslateTransition();
        pause.setNode(board);
        pause.setDelay(Duration.millis(ms));
        pause.setOnFinished(e->{
            next_phase_btn.setDisable(false);
            if (is_skip){next_phase_btn.fire();}
        });
        pause.play();
    }

    /**
     * Triggers transition before changing into win scene.
     * @param winner the winner of the game
     */
    public void changeWinScene(Player winner) {
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setDuration(Duration.seconds(2));
        fadeOut.setNode(board);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            this.loadWinScene(winner);
        });
        fadeOut.play();
    }

    /**
     * Load win scene for the specified winner.
     * @param winner the winner of the game
     */
    public void loadWinScene(Player winner){
        try {
            FXMLLoader win_loader = new FXMLLoader(getClass().getResource("../view/Win.fxml"));
            win_loader.setControllerFactory(c -> new WinController(winner));
            Parent root = (SplitPane) win_loader.load();
            Scene win_scene = new Scene(root);
            
            Stage cur_stage = (Stage) board.getScene().getWindow();
            cur_stage.setScene(win_scene);
        } catch (IOException e) {
            System.out.println("IN WINNING: "+ e);
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
}
