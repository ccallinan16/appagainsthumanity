package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mocks.MockDealer;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardTypeException;

@Root
public class CardCollection {

	public static CardCollection instance = new CardCollection();
	
	@ElementMap
	private HashMap<Integer,Card> white;
	
	@ElementMap
	private HashMap<Integer,Card> black; //possibly only one
	
	private int selectedId;
	private int blackCardId;
	private MockDealer dealer;

	private CardCollection() {
		white = new HashMap<Integer,Card>();
		black = new HashMap<Integer,Card>();
		selectedId = -1;
		blackCardId = -1;
		dealer = null;
	}
	
	public Card getCard(int id, CardType type)
	{
		if (type.equals(CardType.WHITE))
			return white.get(id);
		else if (type.equals(CardType.BLACK))
			return black.get(id);
		
		return null;
	}

	public Card makeCard(int id, String text, CardType type)
	{
		Card c = Card.makeCard(id, text, type);
		switch(type){
		case WHITE:
			white.put(id,c);
			break;
			
		case BLACK:
			black.put(id,c);
		}
		
		return c;
	}
	
	public void makeAndAddCardFromText(CardType type, String text) throws CardTypeException
	{
		if (type.equals(CardType.WHITE))
			makeCard(white.size()+1, text, type);
		else if (type.equals(CardType.BLACK))
			makeCard(black.size()+1, text, type);
		else throw new CardTypeException(type);
	}

	public void setSelected(Card c) {

		//HashMap<Integer,Card> map = (c.getType().equals(CardType.WHITE))? white : black;
		
		//boolean tmp = c.isHighlighted();
		
		//System.out.println("set selected : " + c + ", is highlighted: " + tmp);

		selectedId = -1;
		
		//for (Card card : map.values())
		//	card.setHighlighted(false);
		
		//if (!tmp)
		selectedId = c.getId();
		
		//c.setHighlighted(!tmp);
	}

	public void setBlackCard(int id)
	{
		System.out.println("set black called, id: " + id);

		//TODO: call to server / DB
		blackCardId = id;
	}
	
	public Card getBlackCard()
	{
		System.out.println("get black called, id: " + blackCardId);
		System.out.println("black: " + black);
		//TODO: call to server
		return getCard(blackCardId,CardType.BLACK);
	}
	
	public int getSelectedID() {
		return selectedId;
	}

	public Card getSelectedCard(CardType type) {
		return getCard(selectedId, type);
	}

	public void clearAll() {
		white.clear();
		black.clear();
		selectedId = -1;
		blackCardId = -1;
	}	
	
	private void clearWhite() {
		selectedId = -1;
		white.clear();
	}

	public int getCardCount(CardType type) {
		if (type.equals(CardType.WHITE))
			return white.size();
		if (type.equals(CardType.BLACK))
			return black.size();
		return 0;
	}

	public void setupContextTESTING(ViewContext context, MockDealer ctxDealer)
	{
		//overwrite the usual dealer
		this.dealer = ctxDealer;
		
		String ws = "W";
		
		while (ws.length() < 27)
			ws += "W";
		
		String longest = "While the United States raced the Soviet Union to the moon, the Mexican government funneled millions of pesos into research on ____.";

		Card black = makeCard(77, longest, CardType.BLACK);
		
		Card white = makeCard(66, longest, CardType.WHITE);

		  switch(context)
		  {
		  case SELECT_BLACK:
			  break;
			  
		  case SELECT_WHITE:
		  case SHOW_RESULT:
			  setBlackCard(black.getId());
			  //already added to CardsInPlay, TODO: change
			  break;
			  
		  case CONFIRM_SINGLE:
			  setSelected(black);
			  break;

		  case CONFIRM_PAIR:
			  setBlackCard(black.getId());
			  setSelected(white);
			  break;
			  
		  default:
				  //do nothing
		  }
	}
	
	public void setupContext(ViewContext context, MockDealer ctxDealer) {
				
	  switch(context)
	  {
	  case SELECT_BLACK:
		  clearAll();
		  break;
		  
	  case SELECT_WHITE:
	  case SHOW_RESULT:
		  assert(blackCardId != -1);
		  clearWhite();
		  break;
		  
	  case CONFIRM_SINGLE:
	  case CONFIRM_PAIR:
		  assert(selectedId != -1);
		  break;
		  
	  default:
			  //do nothing
	  }
	  
	if (dealer == null)
		this.dealer = ctxDealer;

	  dealer.dealCards(context); //already added to CardsInPlay, TODO: change

	}

	public List<Card> getCardsForPager(ViewContext context) {
		
		ArrayList<Card> cards = new ArrayList<Card>();
		  switch(context)
		  {
		  case SELECT_BLACK:
			  cards.addAll(black.values());
			  break;
			  
		  case SELECT_WHITE:
		  case SHOW_RESULT:
			  cards.addAll(white.values());
			  break;
			  
		  case CONFIRM_SINGLE:
		  case CONFIRM_PAIR:
			  cards.add(getSelectedCard((
					  context.equals(ViewContext.CONFIRM_SINGLE)
					  ?CardType.BLACK:CardType.WHITE)));

			  break;
			  
		  default:
				  //do nothing
		  }
		  
		  return cards;
	}

	public MockDealer getDealer() {
		return dealer;
	}

	public void setDealer(MockDealer dealer) {
		this.dealer = dealer;
	}

}
