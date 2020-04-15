package com.avatarduel.controller;

import com.avatarduel.Game;
import com.avatarduel.event.*;
import com.avatarduel.model.Phase;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.*;
import com.avatarduel.model.card.Character;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoardController implements Initializable, Publisher, Subscriber {
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

    Label[] phase_bar;

//    AnchorPane player1_field, player2_field;
    AnchorPane[] player_fields;

    /**
     * The model
     * It should be a class Game which has the deck, 2 players, and other game rules for each phase
     */
//    Player player1, player2;
    Player[] players;
//    PlayerFieldController[] player1_controller, player2_controller;
    PlayerFieldController[] player_controllers;
    Game game_engine;

    StackPane hover_card_box;
    private CardController hover_card_controller;
    private BoardChannel channel;
    private ArrayList<SummonedCharacterController> targeting;
    private int cur_player;

    public BoardController(BoardChannel channel) {
        this.channel = channel;
        this.players = new Player[3];
        this.player_controllers = new PlayerFieldController[3];
        this.player_fields = new AnchorPane[3];
    }

    /**
     * For testing purposes
     */
    Phase[] phases = new Phase[]{Phase.DRAW, Phase.MAIN, Phase.BATTLE, Phase.END};
    int phase_id = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
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
            this.channel.addSubscriber(this,player_controllers[1]);
            this.channel.addSubscriber(this,player_controllers[2]);

            summoned_name.setText("");
            summoned_description.setText("");

            /**
             * Initialize the phase change mid bar
             */
            phase_bar = new Label[]{dp_label, mp_label, bp_label, ep_label};

            this.targeting = new ArrayList<SummonedCharacterController>(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * id nya 1 atau 2
     * @param id
     * @param player
     */
    public void setPlayer(int id, Player player) {
        try {
            this.players[id] = player;
            player_controllers[id].setPlayer(this.players[id]);
            if (id == 2) {
                player_controllers[id].flipRow();
            }
        } catch (Exception e) {
            System.out.println("In Player Controller: " + e);
        }
    }

    public void startGame(Game game_engine){
        this.game_engine = game_engine;
        this.channel.addSubscriber(game_engine, this);
        this.channel.addSubscriber(this, game_engine);
        this.drawBoth(); // harusnya lewat game
        this.game_engine.setup();
    }

    /**
     * For testing purposes.
     */
    public void drawBoth() {
        for (int i = 0; i < 7; i++) {
            player_controllers[1].draw();
            player_controllers[2].draw();
            this.sleep(5000);
        }
    }

    SummonedSkill placed_skill;
    @Override
    public void onEvent(Event event) {
//        boolean canSummonSkill = this.game_engine.getPlayer(0).getCharacterZone().size() > 0 || this.game_engine.getPlayer(0).getCharacterZone().size() > 0;
//        this.game_engine.getPlayer(0).canSummonSkill = canSummonSkill;
//        this.game_engine.getPlayer(1).canSummonSkill = canSummonSkill;

        if (event instanceof NewCardDrawnEvent || event instanceof NewSummonedCardEvent) {
            CardController controller = (CardController) event.getInfo();
            this.channel.addSubscriber(controller, this.hover_card_controller);
            if (event instanceof NewSummonedCardEvent) {
                Card card = controller.getCard();
                if (card instanceof Character) {
                    this.game_engine.getPlayer(1).canSummonSkill = true;
                    this.game_engine.getPlayer(2).canSummonSkill = true;
                }
            }
        } else if (event instanceof HoverSummonedCardEvent) {
            Summoned summoned_card = (Summoned)event.getInfo();
            if (summoned_card == null) {
                this.summoned_name.setText("");
                this.summoned_description.setText("");
                return;
            }

            this.summoned_name.setText(summoned_card.getBaseCard().getName());
            String description = "";
            if (summoned_card instanceof SummonedCharacter) {
                int d_atk = ((SummonedCharacter)summoned_card).getDAtk();
                int d_def = ((SummonedCharacter)summoned_card).getDDef();
                Character character = (Character) summoned_card.getBaseCard();
                int atk = character.getAtk();
                int def = character.getDef();

                description += "ATK: " + atk + (d_atk != 0 ? (d_atk > 0 ? " + " : " - ") + Math.abs(d_atk) : "") + "\n";
                description += "DEF: " + def + (d_def != 0 ? (d_def > 0 ? " + " : " - ") + Math.abs(d_def) : "") + "\n";
                description += "Skills atttached:\n";
                if (((SummonedCharacter) summoned_card).getAttachedSkills().isEmpty()) {
                    description += "None\n";
                } else {
                    for (SummonedSkill summoned_kill: ((SummonedCharacter) summoned_card).getAttachedSkills()) {
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
                    description += ((SummonedSkill) summoned_card).
                            getAppliedTo().
                            getBaseCard().
                            getName();
                }
            }
            this.summoned_description.setText(description);
        } else if (event instanceof NewSkillCardPlaced) {
            System.out.println("New skill card placed");

            // TODO: lock player1 + 2 field except SummonedCharacter cards
            placed_skill = (SummonedSkill) event.getInfo();
            // make all the summonedcharacter listen to a clicked event
            this.setSkillPickBehavior();
        } else if (event instanceof SkillCharacterPickedEvent) {
            SummonedCharacterController controller = (SummonedCharacterController) event.getInfo();
            placed_skill.setAppliedTo(controller.getSummonedCharacter());
            controller.getSummonedCharacter().addSkill(placed_skill);

            // TODO: unlock the board
            this.undoSkillPickBehavior();
            placed_skill = null;
        } else if (event instanceof CharacterSelectedEvent){
            //Setting Penarget
            ArrayList selected = (ArrayList) event.getInfo();
            SummonedCharacterController selected_card = (SummonedCharacterController) selected.get(0);
            Player selected_card_player = (Player) selected.get(1);
            if (this.targeting.isEmpty()){
                // dibawah ini if yang asli
                // if (selected_card_player==game_engine.getPlayer(this.cur_player) && (!selected_card.summoned_character.getHasAttacked() && !selected_card.summoned_character.getJustSummoned())){
                // kalo yg dibawah ini buat testing purpose
                if (selected_card_player==game_engine.getPlayer(this.cur_player)){
                    selected_card.toggleSelected();
                    this.targeting.add(selected_card);
                }
            }
            //Setting target
            else{
                selected_card.toggleSelected();
                this.targeting.add(selected_card);
                if (selected_card_player==game_engine.getPlayer(this.cur_player%2+1)){
                    game_engine.solveBattle(this.targeting.get(0).summoned_character, this.targeting.get(1).summoned_character);
                }
                this.targeting.get(0).toggleSelected();
                this.targeting.get(1).toggleSelected();
                this.targeting.clear();
                
            }
            System.out.println(selected_card.base_card_controller.card_name);
            System.out.println(this.targeting.size());
        } else if (event instanceof PlayerChangedEvent){
            this.cur_player = (int)event.getInfo();
            System.out.println("CURRENT PLAYER:" + this.cur_player);
        }
    }

    public void setSkillPickBehavior() {
        for (int i = 1; i <= 2; i++) {
            player_controllers[i].setSkillPickBehavior();
        }
    }

    public void undoSkillPickBehavior() {
        for (int i = 1; i <= 2; i++) {
            player_controllers[i].undoSkillPickBehavior();
        }
    }

    public void proceedPhase(ActionEvent actionEvent) {
        phase_bar[phase_id].setStyle(
                "-fx-background-color: darkgray;" +
                "-fx-color: dimgray"
        );
        phase_id++;
        phase_id %= 4;
        phase_bar[phase_id].setStyle(
                "-fx-background-color: aquamarine;" +
                "-fx-color: black"
        );
        System.out.println("CURRENT PHASE: " + phases[phase_id]);
        publish(new PhaseChangedEvent(phases[phase_id]));
    }

    public void sleep(double ms){
        PauseTransition pause = new PauseTransition(
            Duration.millis(ms)
        );
        pause.play();
    }

    @Override
    public void publish(Event event) {
        this.channel.sendEvent(this, event);

    }
}
