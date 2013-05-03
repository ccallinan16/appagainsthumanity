package mocks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class MockDealer {

	XMLReader reader;
	MockDB db;
	
	public MockDealer(Context context) {
    	reader = new XMLReader(context);
    	db = new MockDB();
	}
	
	public MockDealer(String xmlfilepath) {
    	reader = new XMLReader(xmlfilepath);
    	db = new MockDB();
	}

	public List<Card> dealCards(CardType type, int num) {
    	List<Card> cards = new ArrayList<Card>();
    	
    	List<Integer> cardNumbers = db.assignCards(num);
    	
    	for(Integer index : cardNumbers)
    	{
    		String tmp = reader.getText(type, index);
    		if (tmp == null)
    			tmp = "couldn't read from xml";
    		
    		cards.add(Card.makeCard(index,tmp,type));
    	}
		return cards;
	}
}
