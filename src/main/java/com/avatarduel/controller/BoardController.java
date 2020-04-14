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
import java.util.ResourceBundle;

public class BoardController implements Initializable, Subscriber {
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

    AnchorPane player1_field, player2_field;


    /**
     * The model
     * It should be a class Game which has the deck, 2 players, and other game rules for each phase
     */
    Player player1, player2;
    PlayerFieldController player1_controller, player2_controller;
    Game game_engine;

    StackPane hover_card_box;
    private CardController hover_card_controller;
    private BoardChannel channel;

    public BoardController(BoardChannel channel) {
        this.channel = channel;
        this.player1 = new Player(); // should be a singleton object
        this.player2 = new Player();
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
            player1_field = player1_loader.load();
            this.player1_controller = player1_loader.getController();
            this.player1_pane.getChildren().add(player1_field);

            /**
             * Initialize player 2 field
             */
            FXMLLoader player2_loader = new FXMLLoader(getClass().getResource("../view/Player2Field.fxml"));
            player2_loader.setControllerFactory(c -> new PlayerFieldController(this.channel));
            player2_field = player2_loader.load();
            this.player2_controller = player2_loader.getController();
            this.player2_pane.getChildren().add(player2_field);

            this.channel.addSubscriber(player1_controller, this);
            this.channel.addSubscriber(player2_controller, this);

            summoned_name.setText("");
            summoned_description.setText("");

            /**
             * Initialize the phase change mid bar
             */
            phase_bar = new Label[]{dp_label, mp_label, bp_label, ep_label};
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void setPlayer1(Player player) {
        try {
            this.player1 = player;
            player1_controller.setPlayer(this.player1);
        } catch (Exception e) {
            System.out.println("In Player Controller: " + e);
        }
    }

    public void setPlayer2(Player player) {
        this.player2 = player;
        player2_controller.setPlayer(this.player2);
        player2_controller.flipRow();
    }

    public void startGame(Game game_engine){
        this.game_engine = game_engine;
        this.channel.addSubscriber(game_engine, this);
        this.drawBoth();
    }

    /**
     * For testing purposes.
     */
    public void drawBoth() {
        for (int i = 0; i < 7; i++) {
            player1_controller.draw();
            player2_controller.draw();
            this.sleep(5000);
        }
    }

    SummonedSkill placed_skill;
    @Override
    public void onEvent(Event event) {
        if (event instanceof NewCardDrawnEvent || event instanceof NewSummonedCardEvent) {
            CardController controller = (CardController) event.getInfo();
            this.channel.addSubscriber(controller, this.hover_card_controller);
            if (event instanceof NewSummonedCardEvent) {
                Card card = controller.getCard();
                if (card instanceof Character) {
                    this.game_engine.getPlayer(0).canSummonSkill = true;
                    this.game_engine.getPlayer(1).canSummonSkill = true;
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
                description += "ATK: " + ((SummonedCharacter)summoned_card).getNetAtk() + "\n";
                description += "DEF: " + ((SummonedCharacter)summoned_card).getNetDef() + "\n";
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
                    description += "Powers up a character when attacking a defense character\n";
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
            // TODO: apply the skill
            SummonedCharacterController controller = (SummonedCharacterController) event.getInfo();
            placed_skill.setAppliedTo(controller.getSummonedCharacter());
            controller.getSummonedCharacter().addSkill(placed_skill);

            // TODO: unlock the board
            this.undoSkillPickBehavior();
            placed_skill = null;
        }
    }

    public void setSkillPickBehavior() {
        player1_controller.setSkillPickBehavior();
        player2_controller.setSkillPickBehavior();
    }

    public void undoSkillPickBehavior() {
        player1_controller.undoSkillPickBehavior();
        player2_controller.undoSkillPickBehavior();
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
    }

    public void sleep(int ms){
        PauseTransition pause = new PauseTransition(
            Duration.millis(ms)
        );
        pause.play();
    }
}
