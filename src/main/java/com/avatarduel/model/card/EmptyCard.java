package com.avatarduel.model.card;
import com.avatarduel.model.Element;

/**
 * A singleton object of a Card
 */
public final class EmptyCard extends Card{

    private static EmptyCard emptyCard;

    /**
     * Create a new EmptyCard, EmptyCard only have one instance
     */
    private EmptyCard() {
        super("Aangst", "The Last Euybender", Element.AIR, "card/image/placeholder_1.png");
    }

    /**
     * Returns the instance of EmptyCard
     * @return EmptyCard instance
     */
    public static EmptyCard getInstance(){
        if(EmptyCard.emptyCard == null)
            EmptyCard.emptyCard = new EmptyCard();
        return EmptyCard.emptyCard;
    }
}