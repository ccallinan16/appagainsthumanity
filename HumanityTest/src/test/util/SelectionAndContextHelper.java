package test.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class SelectionAndContextHelper {
	
    public static Intent switchFromSelectionToDisplay(
    		CardSlideActivity origin, 
    		CardSlideActivity activity, 
    		ViewContext context,
    		long turnid)
    {
   		origin.getIntent().putExtras(BundleCreator.createBundle(context,turnid));
    	origin.onCreate(null);
    	
    	Card c = getFirstCard(origin, 
    			(context.equals(ViewContext.SELECT_BLACK))
    			?CardType.BLACK
    			:CardType.WHITE);
    	    	
    	selectCardAndPerformClick(origin,c);
    	    	
    	Intent i = new Intent(activity, CardSlideActivity.class);
    	
    	switch (context)
    	{
    	case SELECT_WHITE:
        	i.putExtras(BundleCreator.createBundle(ViewContext.CONFIRM_PAIR,turnid));
        	break;
    	case SELECT_BLACK:
        	i.putExtras(BundleCreator.createBundle(ViewContext.CONFIRM_SINGLE,turnid));
        	break;
        	
        	default:
        		return null;
    	}
    	
    	return i;
    }

    public static Intent switchFromDisplayToMain(CardSlideActivity origin, 
    		Activity activity, ViewContext context,long turnid)
    {
    	Intent origIntent = new Intent();
    	origIntent.putExtras(BundleCreator.createBundle(context,turnid));
    	origin.setIntent(origIntent);
    	origin.onCreate(null);
    	
    	performClick(origin);
    	
    	Intent i = createAndGetIntent(activity,ViewContext.UNKNOWN);

    	return i;
    }
	
    public static Card getFirstCard(CardSlideActivity activity, CardType type)
    {
    	ViewPager pager = (ViewPager) activity.findViewById(R.id.cs_card_slider);
    	CardFragmentAdapter cfa = (CardFragmentAdapter) pager.getAdapter();
    	return CardCollection.instance.getCard(cfa.getCardID(0), type);
    }
    
    public static Card getTopCard(CardSlideActivity activity)
    {
    	return CardCollection.instance.getBlackCard(); //activity.topCardId);
    }
        
    public static void selectCardAndPerformClick(CardSlideActivity activity, Card c)
    {
    	performTap(activity);
    }
    
    public static void performTap(CardSlideActivity activity)
    {
    	ViewPager fm = (ViewPager) activity.findViewById(R.id.cs_card_slider);
    	fm.performClick();
    }
    
    public static void performClick(CardSlideActivity activity)
    {
    	Button btn = (Button) activity.findViewById(R.id.okButton);
    	btn.performClick();
    }
    
    public static CardSlideActivity createNewCSActivity(ViewContext second)
    {
    	CardSlideActivity newActivity = new CardSlideActivity();
    	createAndGetIntent(newActivity,second);
    	
    	newActivity.onCreate(null);
    	return newActivity;
    }

    public static Intent createAndGetIntent(Activity activity, ViewContext context)
    {
    	Intent i = new Intent(activity, CardSlideActivity.class);
    	i.putExtras(BundleCreator.createBundle(context,0));

    	if(context == ViewContext.UNKNOWN)
			i = new Intent(activity, MainActivity.class);
 
    	activity.setIntent(i);
    	return i;
    }    
    
    
        

}
