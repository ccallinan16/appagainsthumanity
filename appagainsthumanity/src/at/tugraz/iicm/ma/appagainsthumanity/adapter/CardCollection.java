package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mocks.IDToCardTranslator;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import android.annotation.SuppressLint;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardTypeException;

public class CardCollection {

	public static CardCollection instance = new CardCollection();
	
	private HashMap<Integer,Card> white;
	private HashMap<Integer,Card> black; //possibly only one
	
	private int selectedId;
	private int blackCardId;
	private IDToCardTranslator dealer;

	private CardCollection() {
		white = new HashMap<Integer,Card>();
		black = new HashMap<Integer,Card>();
		selectedId = -1;
		blackCardId = -1;
		dealer = null;
	}
	
	public Card getCardSafe(int id, CardType type)
	{
		Card card = null;
		if (type.equals(CardType.WHITE))
			card = white.get(id);
		else if (type.equals(CardType.BLACK))
			card = black.get(id);
		
		if (card == null)
		{
			card = dealer.getCardFromID(type, id);
			//and, for now, add the card.
			addCard(card);
		}
		
		return card;
	}

	public void addCard(Card c) {
		switch(c.getType()){
		case WHITE:
			white.put(c.getId(),c);
			break;
			
		case BLACK:
			black.put(c.getId(),c);
		}
		
	}
	
	public void setBlackCard(int id)
	{
		//TODO: call to server / DB
		blackCardId = id;
	}
	
	public Card getBlackCard()
	{
		//TODO: call to server
		return getCardSafe(blackCardId,CardType.BLACK);
	}
	
	public int getSelectedID() {
		return selectedId;
	}

	public void setSelectedID(int currentCard, CardType type) {
		selectedId = currentCard;			
	}
	
	public Card getSelectedCard(CardType type) {
		
		Card c = getCardSafe(selectedId, type);
		
		if (c == null)
			addCard(dealer.getCardFromID(type, selectedId));

		return c;
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
	
	public void setupContext(ViewContext context, IDToCardTranslator ctxDealer) {
				
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

	  for (Card c : dealer.dealCards(context))
		  addCard(c); 

	}

	public void setCards(List<Integer> list, CardType type)
	{
		for(Card c : dealer.getCardFromIDs(type, list))
			addCard(c);
	}
	
	public List<Integer> getCardsForPager(ViewContext context) {
		
		ArrayList<Integer> cards = new ArrayList<Integer>();
			
		  switch(context)
		  {
		  case SELECT_BLACK:
			  cards.addAll(black.keySet());
			  break;
			  
		  case SELECT_WHITE:
		  case SHOW_RESULT:
			  cards.addAll(white.keySet());
			  break;
			  
		  case CONFIRM_SINGLE:
		  case CONFIRM_PAIR:
			  cards.add(getSelectedID());
			  break;
			  
		  default:
				  //do nothing
		  }
		  
		  return cards;
	}

	public IDToCardTranslator getTranslator() {
		return dealer;
	}

	public void setTranslator(IDToCardTranslator dealer) {
		this.dealer = dealer;
	}



}
