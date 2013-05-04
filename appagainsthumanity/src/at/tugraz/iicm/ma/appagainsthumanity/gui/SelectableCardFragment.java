package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

public class SelectableCardFragment extends SingleCardFragment {

	
    private void setBackground(View v, boolean highlight)
    {
    	if (highlight)
    		v.setBackgroundColor(getResources().getColor(R.color.highlight_card));
    	else
    		v.setBackgroundColor(TYPE.getBGColor());
    }
    
    private void actionSelect(View v)
    {
	    ViewPager pager = (ViewPager) v.getParent().getParent();
		CardFragmentAdapter cfa = (CardFragmentAdapter)pager.getAdapter();
		
		int currentCard = cfa.getCardID(pager.getCurrentItem());
		
		Card c = CardCollection.instance.getCard(currentCard,TYPE);
			  			
		CardCollection.instance.setSelected(c);
			  			
		setBackground(v,c.isHighlighted());
		cfa.notifyDataSetChanged();

    }
    
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 
		 View v = super.onCreateView(inflater, container, savedInstanceState);
		 
		 
		 ScrollView scroller = (ScrollView) v.findViewById(R.id.my_scroll_view);
		 TextView child = (TextView) scroller.getChildAt(0);
		 
		 child.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionSelect((View)v.getParent().getParent());
			}
		});
		 
	      v.setOnClickListener(new OnClickListener() {
	  		
	  		@Override
	  		public void onClick(View v) {	  			
	  			actionSelect(v);
	  		}
	  	});
	      			      
		   Card card = CardCollection.instance.getCard(getArguments().getInt(ID), TYPE );
		   setBackground(v,card.isHighlighted());
	      
	      return v;
	 }

}
