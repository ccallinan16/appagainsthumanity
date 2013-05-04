package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardFragmentAdapter extends FragmentPagerAdapter {
	  private List<Integer> cardIDs;
	  private FragmentManager fm;
	  private CardType type;
	  private boolean selectable;
	  
	  public CardFragmentAdapter(FragmentManager fm, List<Card> fragments,boolean selectable) {
		    super(fm);
		    this.fm = fm;
		    cardIDs = new ArrayList<Integer>();
		    if (fragments != null && !fragments.isEmpty())
		    {
		    	type = fragments.get(0).getType();
		    }
		    this.selectable = selectable;
		    setCards(fragments);
		  }
	  
	  @Override 
	  public Fragment getItem(int position) {		  
		  return SingleCardFragment.newInstance(
				  cardIDs.get(position),
				  type, 
				  60f,
				  selectable);
	  }

	  @Override
	public int getItemPosition(Object object) {

		  //return super.getItemPosition(object);
		  return POSITION_NONE;
	}
	  
	  public int getCardID(int position)
	  {
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
	  
	  public FragmentManager getFm() {
		return fm;
	}

	public CardType getCardType() {
		return type;
	}
}

