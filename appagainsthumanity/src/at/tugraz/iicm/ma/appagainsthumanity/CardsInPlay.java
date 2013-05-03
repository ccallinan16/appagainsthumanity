package at.tugraz.iicm.ma.appagainsthumanity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mocks.MockDealer;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardsInPlay {

	public static CardsInPlay instance = new CardsInPlay();
	
	private HashMap<Integer,Card> white;
	private HashMap<Integer,Card> black; //possibly only one

	private CardsInPlay() {
		white = new HashMap<Integer,Card>();
		black = new HashMap<Integer,Card>();
	}
	
	public Card getCard(int id,CardType type)
	{
		if (type.equals(CardType.WHITE))
			return white.get(id);
		else if (type.equals(CardType.BLACK))
			return black.get(id);
		
		return null;
	}

	public void addCard(Card card) {
		if (card.getType().equals(CardType.WHITE))
			white.put(card.getId(), card);
		else if (card.getType().equals(CardType.BLACK))
			black.put(card.getId(), card);

	}

	public void replaceCardSet(CardType type, List<Card> white) {
		if (type.equals(CardType.WHITE))
		{
			this.white.clear();
		} else if (type.equals(CardType.BLACK))
			this.black.clear();
		
		for (Card card : white)
			addCard(card);

	}	
	
	public void toggleHightlight(Card c) {
		
		HashMap<Integer,Card> map = (c.getType().equals(CardType.WHITE))? white : black;
		
		boolean tmp = c.isHighlighted();
		
		for (Card card : map.values())
			card.setHighlighted(false);
		
		c.setHighlighted(!tmp);
	}

	public List<Card> getCards(CardType type, int i, MockDealer dealer) {

		replaceCardSet(type,dealer.dealCards(type, i));
		
		return new ArrayList<Card>((type.equals(CardType.WHITE))?white.values():black.values());
	}	
}
