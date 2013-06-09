package mocks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class IDToCardTranslator {

	XMLReader reader;
	
	//knows rules and numPlayers:
	private int numBlackCards = 3;
	private int numWhiteCards = 4;
	private int numPlayers = 6;
	
	public IDToCardTranslator(Context context) {
    	reader = new XMLReader(context);
	}
	
	public IDToCardTranslator(String xmlfilepath) {
    	reader = new XMLReader(xmlfilepath);
	}

	
	public Card getCardFromID(CardType type, int cardID) {
		String tmp = reader.getText(type, cardID);
		if (tmp == null)
			tmp = "couldn't read from xml";
		
		return Card.makeCard(cardID, tmp, type);
	}

	
	public List<Card> getCardFromIDs(CardType type, List<Integer> cardIDs)
	{
		List<Card> cards = new ArrayList<Card>();
		
		for (Integer index : cardIDs)
		{
			cards.add(getCardFromID(type, index));
		}
		
		return cards;
	}
	
	public List<Integer> getRandomBlackCardIDs(CardType cardType) {
		
    	return Shuffler.getRandomListOfInts(numBlackCards);
    }

	/**
	 * TODO: replace this function by a Preset + Gamemanager combination.
	 * @param context
	 * @return
	 */
	public List<Card> dealCards(ViewContext context) {
					   	
    	CardType type = CardType.WHITE;
    	int numCards = 1;
    	
    	switch (context)
    	{
    	case SELECT_BLACK:
    		numCards = numBlackCards;
    		type = CardType.BLACK;
    		break;
    		
    	case SELECT_WHITE:
    		type = CardType.WHITE;
    		numCards = numWhiteCards;
    		//TODO: aditional context, SELECT_WINNER, needs different nums of cards.
    		break;
    		
    	case SHOW_RESULT:
    		type = CardType.WHITE;
    		numCards = numPlayers;
    		break;
    		
    		default:
    			
    	}
    	
    	List<Integer> cardNumbers = Shuffler.getRandomListOfInts(numCards);
    	
		return getCardFromIDs(type, cardNumbers);
	}

	public void setNumBlack(int numBlackCards) {
		this.numBlackCards = numBlackCards;
	}

	public void setNumWhiteCards(int numWhiteCards) {
		this.numWhiteCards = numWhiteCards;
	}
	
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}


}
