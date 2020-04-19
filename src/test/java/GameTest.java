import com.avatarduel.Game;
import com.avatarduel.event.BoardChannel;
import com.avatarduel.event.Event;
import com.avatarduel.event.Publisher;
import com.avatarduel.event.WinEvent;
import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;
import com.avatarduel.model.card.Character;
import com.avatarduel.model.card.PowerUp;
import com.avatarduel.model.card.SummonedCharacter;
import com.avatarduel.model.card.SummonedSkill;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class GameTest {
    Game game;

    BoardChannel channel = Mockito.mock(BoardChannel.class);

    @Before
    public void initialize() {
        Dealer dealer = new Dealer();
        Deck d1 = dealer.getDeck(60);
        Deck d2 = dealer.getDeck(60);
        game = new Game(new Player("Uno", d1, channel), new Player("Dos", d2, channel), channel);
        doNothing().when(channel).sendEvent(any(Publisher.class), any(Event.class));
    }

    @Test
    public void test_initGame() {
        game.setup();
        assertTrue(game.getPlayer(1).getHand().size() == 8 && game.getPlayer(2).getHand().size() == 7);
    }

    @Test
    public void test_endStage() {
        game.setup();
        game.endStage();
        assertTrue(game.getCurPlayer() == 2 && game.getPlayer(1).hasUsedLand == false);
    }

    @Test
    public void test_attackingAtkCard() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        Character c2 = Mockito.mock(Character.class);
        when(c1.getAtk()).thenReturn(50);
        when(c2.getAtk()).thenReturn(20);
        when(c1.getPower()).thenReturn(0);
        when(c2.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);
        when(c2.getElement()).thenReturn(Element.WATER);

        SummonedCharacter sc1 = (SummonedCharacter) game.getPlayer(1).summonCard(c1);
        SummonedCharacter sc2 = (SummonedCharacter) game.getPlayer(2).summonCard(c2);

        game.solveBattle(sc1,  sc2);
        assertTrue(game.getPlayer(2).getHealth() == 50);
    }

    @Test
    public void test_attackingDefCard() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        Character c2 = Mockito.mock(Character.class);
        when(c1.getAtk()).thenReturn(50);
        when(c2.getDef()).thenReturn(20);
        when(c1.getPower()).thenReturn(0);
        when(c2.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);
        when(c2.getElement()).thenReturn(Element.WATER);

        SummonedCharacter sc1 = (SummonedCharacter) game.getPlayer(1).summonCard(c1);
        SummonedCharacter sc2 = (SummonedCharacter) game.getPlayer(2).summonCard(c2);
        sc2.rotate();

        game.solveBattle(sc1,  sc2);
        assertTrue(game.getPlayer(2).getHealth() == 80);
    }

    @Test
    public void test_attackingDefCardWithPowerUp() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        Character c2 = Mockito.mock(Character.class);
        when(c1.getAtk()).thenReturn(50);
        when(c2.getDef()).thenReturn(20);
        when(c1.getPower()).thenReturn(0);
        when(c2.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);
        when(c2.getElement()).thenReturn(Element.WATER);

        SummonedCharacter sc1 = (SummonedCharacter) game.getPlayer(1).summonCard(c1);
        SummonedSkill mock_powerup = mock(SummonedSkill.class);
        when(mock_powerup.getBaseCard()) .thenReturn(mock(PowerUp.class));
        sc1.addSkill(mock_powerup);

        SummonedCharacter sc2 = (SummonedCharacter) game.getPlayer(2).summonCard(c2);
        sc2.rotate();

        game.solveBattle(sc1,  sc2);
        assertTrue(game.getPlayer(2).getHealth() == 50);
    }


    @Test
    public void test_cantAttackPlayer() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        Character c2 = Mockito.mock(Character.class);
        when(c1.getPower()).thenReturn(0);
        when(c2.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);
        when(c2.getElement()).thenReturn(Element.WATER);

        game.getPlayer(1).summonCard(c1);
        game.getPlayer(2).summonCard(c2);

        assertTrue(!game.canDirectAttack());
    }

    @Test
    public void test_attackPlayer() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        when(c1.getAtk()).thenReturn(50);
        when(c1.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);

        SummonedCharacter sc1 = (SummonedCharacter) game.getPlayer(1).summonCard(c1);

        assertTrue(game.canDirectAttack());
        game.solveDirectAttack(sc1);
        assertTrue(game.getPlayer(2).getHealth() == 30);
    }

    @Test
    public void test_attackPlayerOverkill() {
        game.setup();
        Character c1 = Mockito.mock(Character.class);
        when(c1.getAtk()).thenReturn(100);
        when(c1.getPower()).thenReturn(0);
        when(c1.getElement()).thenReturn(Element.WATER);

        SummonedCharacter sc1 = (SummonedCharacter) game.getPlayer(1).summonCard(c1);

        assertTrue(game.canDirectAttack());
        game.solveDirectAttack(sc1);
        assertTrue(game.getPlayer(2).getHealth() == 0);
    }
}
