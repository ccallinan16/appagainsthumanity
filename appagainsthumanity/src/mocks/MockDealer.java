package mocks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class MockDealer {

	XMLReader reader;
	MockDB db;
	
	//knows rules and numPlayers:
	private int numBlackCards = 3;
	private int numWhiteCards = 4;
	private int numPlayers = 6;
	
	public MockDealer(Context context) {
    	reader = new XMLReader(context);
    	db = new MockDB();
	}
	
	public MockDealer(String xmlfilepath) {
    	reader = new XMLReader(xmlfilepath);
    	db = new MockDB();
	}

	
	public List<Card> getCardFromIDs(CardType type, List<Integer> cardIDs)
	{
		List<Card> cards = new ArrayList<Card>();
		
		for (Integer index : cardIDs)
		{
    		String tmp = reader.getText(type, index);
    		if (tmp == null)
    			tmp = "couldn't read from xml";
    		
    		cards.add(CardCollection.instance.makeCard(index,tmp,type));
		}
		
		return cards;
	}
	
	public List<Integer> getRandomBlackCardIDs(CardType cardType) {
		
    	return db.assignCards(numBlackCards);
    }

	
	public List<Card> dealCards(ViewContext context) {
					
    	List<Card> cards = new ArrayList<Card>();
    	
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
    	
    	List<Integer> cardNumbers = db.assignCards(numCards);
    	
    	for(Integer index : cardNumbers)
    	{
    		String tmp = reader.getText(type, index);
    		if (tmp == null)
    			tmp = "couldn't read from xml";
    		
    		cards.add(CardCollection.instance.makeCard(index,tmp,type));
    	}
		return cards;
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
