package mocks;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class MockDealer {

	XMLReader reader;
	MockDB db;
	
	public MockDealer(int numCards,Context context) {
    	reader = new XMLReader(context);
    	db = new MockDB(numCards);
	}
	
	public MockDealer(int numCards,String xmlfilepath) {
    	reader = new XMLReader(xmlfilepath);
    	db = new MockDB(numCards);
	}

	public List<Card> dealCards(CardType type, int num) {
    	List<Card> cards = new ArrayList<Card>();
    	
    	List<Integer> cardNumbers = db.assignCards(num);
    	
    	for(Integer index : cardNumbers)
    	{
    		String tmp = reader.getText(type, index);
    		if (tmp == null)
    			tmp = "couldn't read from xml";
    		
    		cards.add(new Card(tmp,index,type));
    	}
		return cards;
	}
}
