package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class OnCardSelectionListener implements OnClickListener {
	
	CardType type;
	
	public OnCardSelectionListener(CardType type) {
		this.type = type;
	}
	
    public void onClick(View v) {
  	  
    	ViewPager pager;
    	
    	if (v instanceof ViewPager)
    		pager = (ViewPager) v;
    	else
    		pager  = (ViewPager) v.getParent().getParent();
		CardFragmentAdapter cfa = (CardFragmentAdapter)pager.getAdapter();
		
		int currentCard = cfa.getCardID(pager.getCurrentItem());
			
		Card c = CardCollection.instance.getCard(currentCard,type);
			  			
		CardCollection.instance.setSelected(c);
  		createAndStartNewActivity(v,currentCard);

    }

	private void createAndStartNewActivity(View v,int id) {
	  	Intent intent = new Intent(v.getContext(),CardSlideActivity.class);
	     	
	  	if (type.equals(CardType.BLACK))
	  		intent.putExtras(BundleCreator.getConfirmBlack());
	  	else
	  		intent.putExtras(BundleCreator.getConfirmWhite());

	  	v.getContext().startActivity(intent);
	}
	
}
