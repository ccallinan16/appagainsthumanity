package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import java.util.HashMap;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;


@Root
public class CardCollection {
	
	@ElementMap
	private HashMap<Integer,Card> white;
	
	@ElementMap
	private HashMap<Integer,Card> black; 

	
	public CardCollection() {
		white = new HashMap<Integer, Card>();
		black = new HashMap<Integer, Card>();
	}

	public void makeAndAddCardFromText(CardType type, String text) throws CardTypeException
	{
		if (type.equals(CardType.WHITE))
		{
			Card c = Card.makeCard(white.size()+1, text, type);
			white.put(c.getId(),c);
		}
		else if (type.equals(CardType.BLACK))
		{
			Card c = Card.makeCard(black.size()+1, text, type);
			black.put(c.getId(),c);
		}

		else throw new CardTypeException(type);
	}
	
	
}
