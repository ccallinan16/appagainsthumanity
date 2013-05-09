package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.db.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardFragmentAdapter extends FragmentPagerAdapter {
	  private List<Integer> cardIDs;
	  private CardType type;
	  private boolean selectable;
	  private long turnID;
	  
	  /*
	  private CardFragmentAdapter(FragmentManager fm, List<Card> fragments,boolean selectable, long turnID) {
		    super(fm);
		    cardIDs = new ArrayList<Integer>();
		    if (fragments != null && !fragments.isEmpty())
		    {
		    	type = fragments.get(0).getType();
		    }
		    this.selectable = selectable;
		    this.turnID = turnID;
		    setCards(fragments);
		  }
	  */
	  
	  public CardFragmentAdapter(FragmentManager supportFragmentManager, 
			  List<Integer> cardIDs,
			  boolean selectable, CardType type, long turnID) {
		    super(supportFragmentManager);
		    
		    this.type = type;
		    this.selectable = selectable;
		    this.turnID = turnID;
		    this.cardIDs = cardIDs;
		    		    
	}

	@Override 
	  public Fragment getItem(int position) {		  
		  return SingleCardFragment.newInstance(
				  cardIDs.get(position),
				  type, 
				  selectable,
				  turnID);
	  }

	  @Override
	public int getItemPosition(Object object) {

		  //return super.getItemPosition(object);
		  return POSITION_NONE;
	}
	  
	  public int getCardID(int position)
	  {
		  if (cardIDs.size() <= position)
			  return 0;
		  return cardIDs.get(position);
	  }
	  

	  @Override
	  public int getCount() {
	    return this.cardIDs.size();
	  }
	  
	  public void setCards(List<Card> cards) {
		  		  
		  this.cardIDs.clear();
		  
		  for (Card c : cards)
		  {
			  this.cardIDs.add(c.getId());
		  }
	  }
	  

	public CardType getCardType() {
		return type;
	}
}

