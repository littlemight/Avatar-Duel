import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.event.BoardChannel;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Land;
import com.avatarduel.model.card.Skill;
import com.avatarduel.model.card.Summonable;
import com.avatarduel.model.card.Character;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
    Deck deck;
    int deck_size=50;
    BoardChannel channel;
    Player player;

    @Before
    public void initialize(){
        Dealer dealer = new Dealer();
        this.deck = dealer.getDeck(deck_size);
        this.channel = new BoardChannel();
        this.player = new Player("test", this.deck, this.channel);
    }

    @Test
    public void test_playerHealthConsumption() {
        assertEquals(80, player.getHealth());
        player.decreaseHealth(90);
        assertEquals(0, player.getHealth());
    }

    @Test
    public void test_playerPowerConsumption() {
        Element e = Element.ENERGY;
        assertEquals(0, player.getPower(e));
        player.addPower(e);
        assertEquals(1, player.getPower(e));
        player.usePower(e, 1);
        player.resetPower();
        assertEquals(1, player.getPower(e));
    }

    @Test
    public void test_playerSummonCard(){
        Element e = Element.AIR;
        while (deck.getNeffValue()>0){
            Card card = deck.drawCard();
            if (card instanceof Character) {
                assertFalse(player.canSummon((Summonable)card));
                break;
            }
        }
        for (int i=0;i<10;i++){
            player.addPower(e);
        }
        while (deck.getNeffValue()>0){
            Card card = deck.drawCard();
            if (card instanceof Character && card.getElement()==e) {
                assertTrue(player.canSummon((Summonable)card));
                player.getHand().add(card);
                assertTrue(player.getHand().size()==1);
                player.summonCard((Summonable)card);
                break;
            }
        }
        assertTrue(player.getPower(e) < 10);
        assertTrue(player.getCharacterZone().size()==1);
        assertTrue(player.getSkillZone().isEmpty());
        assertTrue(player.getHand().isEmpty());
    }
}