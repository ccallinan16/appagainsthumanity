package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

public class CardFragmentAdapter extends FragmentPagerAdapter {
	  private List<Integer> cardIDs;
	  private ViewContext context;
	  private boolean selectable;
	  private long turnID;
	  private int winnerID;
	  
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
			  boolean selectable, ViewContext type, long turnID, int winnerID) {
		    super(supportFragmentManager);
		    
		    this.context = type;
		    this.selectable = selectable;
		    this.turnID = turnID;
		    this.cardIDs = cardIDs;
		    this.winnerID = winnerID;
		    		    
	}

	@Override 
	  public Fragment getItem(int position) {
		
		boolean winner = (cardIDs.get(position) == winnerID);
		
		  return SingleCardFragment.newInstance(
				  cardIDs.get(position),
				  context, 
				  selectable,
				  turnID, 
				  winner);
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
}

