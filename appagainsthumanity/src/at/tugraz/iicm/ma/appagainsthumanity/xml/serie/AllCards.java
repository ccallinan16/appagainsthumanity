package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class AllCards {
	
	public AllCards()
	{
		whitecards = new ArrayList<Card>();
		blackcards = new ArrayList<Card>();

	}
	
	@ElementList
	List<Card> whitecards;
	
	@ElementList
	List<Card> blackcards;

	public void addCard(CardType type, String text) throws CardTypeException
	{
		if (type.equals(CardType.WHITE))
			whitecards.add(Card.makeCard(whitecards.size()+1,text,CardType.WHITE));
		else if (type.equals(CardType.BLACK))
			blackcards.add(Card.makeCard(blackcards.size()+1,text,CardType.BLACK));
		else throw new CardTypeException(type);
		
	}
	
}