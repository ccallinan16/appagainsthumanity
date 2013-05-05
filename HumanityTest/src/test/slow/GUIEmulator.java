package test.slow;

import test.util.SelectionAndContextHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class GUIEmulator {
	CardSlideActivity csa;
	
	public GUIEmulator() {
		csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);
	}
	

	public void createTransitionBetweenListViewAndSelection(ViewContext context)
	{
		MainActivity activity = new MainActivity();
		activity.onCreate(null);
		activity.onStart();
		
		ListView list = (ListView) activity.findViewById(R.id.game_list_view);
		
		GamelistAdapter adapter = (GamelistAdapter) list.getAdapter();

		adapter.simulateClick(context);

		CardSlideActivity csa = new CardSlideActivity();

	}
	
	
	public Card createSelectionTransitionReturnSelected(ViewContext context)
	{
		CardSlideActivity activity = new CardSlideActivity();
		SelectionAndContextHelper.switchFromSelectionToDisplay(csa, activity, context,true);
				
    	Card selected = SelectionAndContextHelper.getFirstCard(csa, 
    			(context.equals(ViewContext.SELECT_BLACK))
    			?CardType.BLACK
    			:CardType.WHITE);
		   	
    	
		ViewContext follows = (context.equals(ViewContext.SELECT_BLACK)
				?ViewContext.CONFIRM_SINGLE:ViewContext.CONFIRM_PAIR);
		
		MainActivity main = new MainActivity();
		SelectionAndContextHelper.switchFromDisplayToMain(activity, main, follows,false);
		
		return selected;

	}
		
	public Card getSelectedCard() {
		return null;
	}
	
}
