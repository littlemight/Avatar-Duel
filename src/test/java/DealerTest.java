import static org.junit.Assert.assertEquals;

import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Land;
import com.avatarduel.model.card.Skill;
import com.avatarduel.model.card.Character;

import org.junit.Before;
import org.junit.Test;

public class DealerTest {
    Dealer dealer;

    @Before
    public void initialize(){
        this.dealer = new Dealer();
    }

    @Test
    public void test_dealerGetEmptyDeck() {
        Deck deck = dealer.getDeck(0);
        assertEquals(deck.getNeffValue(), 0);
        assertEquals(deck.getSize(), 0);
    }

    @Test
    public void test_dealerGetDeckIsProportional() {
        int num = ThreadLocalRandom.current().nextInt(40,60);
        int tolerance = 4;
        Deck deck = dealer.getDeck(num);
        int c_count=0;
        int l_count=0;
        int s_count=0;
        while (deck.getNeffValue()>0){
            Card card = deck.drawCard();
            if (card instanceof Character) {
                c_count++;
            } else if (card instanceof Land) {
                l_count++;
            } else if (card instanceof Skill) {
                s_count++;
            }
        }
        System.out.println("num=" + num + " c_count=" + c_count + " l_count=" + l_count + " s_count=" + s_count + " tolerance=" + tolerance);
        assertEquals(Math.round(num*2/5), c_count, tolerance);
        assertEquals(Math.round(num*2/5), l_count, tolerance);
        assertEquals(Math.round(num/5), s_count, tolerance);
    }
}