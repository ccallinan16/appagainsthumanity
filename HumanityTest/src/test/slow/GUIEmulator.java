package test.slow;

import test.util.SelectionAndContextHelper;
import android.content.Intent;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
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
	}
	
	
	public Card createSelectionTransitionReturnSelected(ViewContext context, long turnid)
	{
		CardSlideActivity activity = new CardSlideActivity();		
    	Intent i = new Intent();
    	activity.setIntent(i);

		Card selected = SelectionAndContextHelper.selectCardFromSelection(activity,context,turnid);

		SelectionAndContextHelper.switchFromSelectionToDisplay(csa, activity, context,turnid);
						    	    	
		ViewContext follows = (context.equals(ViewContext.SELECT_BLACK)
				?ViewContext.CONFIRM_BLACK:ViewContext.CONFIRM_WHITE);
		
		MainActivity main = new MainActivity();
		SelectionAndContextHelper.switchFromDisplayToMain(activity, main, follows,turnid);
		
		return selected;

	}
		
	public Card getSelectedCard() {
		return null;
	}
	
}
