package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import at.tugraz.iicm.ma.appagainsthumanity.CardsInPlay;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

public class SelectableCardFragment extends SingleCardFragment {

	
    private void setBackground(View v, boolean highlight)
    {
    	if (highlight)
    		v.setBackgroundColor(Color.YELLOW);
    	else
    		v.setBackgroundColor(TYPE.getBGColor());
    }
    
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 
		 View v = super.onCreateView(inflater, container, savedInstanceState);
		 
	      v.setOnClickListener(new OnClickListener() {
	  		
	  		@Override
	  		public void onClick(View v) {
	  			  			  			
	  			ViewPager pager = (ViewPager) v.getParent().getParent();
	  			CardFragmentAdapter cfa = (CardFragmentAdapter)pager.getAdapter();
	  						
	  			int currentCard = cfa.getCardID(pager.getCurrentItem());
	  			
	  			Card c = CardsInPlay.instance.getCard(currentCard,TYPE);
	  				  			
	  			CardsInPlay.instance.toggleHightlight(c);
	  				  			
	  			setBackground(v,c.isHighlighted());
	  			cfa.notifyDataSetChanged();

	  		}
	  	});
	      			      
		   Card card = CardsInPlay.instance.getCard(getArguments().getInt(ID), TYPE );
		   setBackground(v,card.isHighlighted());
	      
	      return v;
	 }

}
