package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class OnCardSelectionListener implements OnClickListener {
	
	CardType type;
	long turnID;
	
	public OnCardSelectionListener(CardType type, long turnID) {
		this.type = type;
		this.turnID = turnID;
	}
	
    public void onClick(View v) {
  	  
    	ViewPager pager;
    	
    	if (v instanceof ViewPager)
    		pager = (ViewPager) v;
    	else
    		pager  = (ViewPager) v.getParent().getParent();
		CardFragmentAdapter cfa = (CardFragmentAdapter)pager.getAdapter();
		
		int currentCard = cfa.getCardID(pager.getCurrentItem());

		CardCollection.instance.setSelectedID(currentCard,type);
		
		//Card c = CardCollection.instance.getCard(currentCard,type);
		//CardCollection.instance.setSelected(c);
		
  		createAndStartNewActivity(v,turnID);

    }

	private void createAndStartNewActivity(View v,long turn_id) {
	  	Intent intent = new Intent(v.getContext(),CardSlideActivity.class);
	     	
	  	if (type.equals(CardType.BLACK))
	  		intent.putExtras(BundleCreator.createBundle(ViewContext.CONFIRM_SINGLE, turn_id));
	  	else
	  		intent.putExtras(BundleCreator.createBundle(ViewContext.CONFIRM_PAIR, turn_id));

	  	v.getContext().startActivity(intent);
	}
	
}
