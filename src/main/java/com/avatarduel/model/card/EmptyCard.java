package com.avatarduel.model.card;
import com.avatarduel.model.Element;

// Singleton Class
public final class EmptyCard extends Card{

    private static EmptyCard emptyCard;

    private EmptyCard() {
        super("Aangst", "The Last Euybender", Element.AIR, "card/image/placeholder_1.png");
    }

    public static EmptyCard getInstance(){
        if(EmptyCard.emptyCard == null)
            EmptyCard.emptyCard = new EmptyCard();
        return EmptyCard.emptyCard;
    }
}