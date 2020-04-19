import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ThreadLocalRandom;

import com.avatarduel.model.Dealer;
import com.avatarduel.model.Deck;
import com.avatarduel.model.card.Card;
import com.avatarduel.model.card.Land;
import com.avatarduel.model.card.Skill;
import com.avatarduel.model.card.Character;

import org.junit.Before;
import org.junit.Test;

public class DeckTest {
    Dealer dealer;
    Deck deck;
    int deck_size=50;

    @Before
    public void initialize(){
        dealer = new Dealer();
    }

    @Test
    public void test_deckBalancedDrawIn12Draws() {
        for (int i=0;i<100;i++){
            System.out.println("iteration=" + i);
            this.deck = dealer.getDeck(deck_size);
            int c_count=0;
            int l_count=0;
            int s_count=0;
            while (deck.getNeffValue()>deck_size-12){
                Card card = deck.drawCard();
                if (card instanceof Character) {
                    c_count++;
                } else if (card instanceof Land) {
                    l_count++;
                } else if (card instanceof Skill) {
                    s_count++;
                }
            }
            System.out.println("num=" + deck_size + " c_count=" + c_count + " l_count=" + l_count + " s_count=" + s_count + " tolerance=" + 1);
            assertEquals(5,l_count,1);
            assertTrue(c_count<6);
            assertTrue(s_count<c_count);
        }
    }
}