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
    	CardCollection.instance.setSelected(c);
    	performClick(activity);
    }
    
    public static void performClick(CardSlideActivity activity)
    {
    	Button btn = (Button) activity.findViewById(R.id.btn_ok);
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
    	Intent i; 
    	
    	switch (context)
    	{
		case CONFIRM_PAIR:
	    	i = new Intent(activity, CardSlideActivity.class);
	    	i.putExtras(BundleCreator.getConfirmWhite());
	    	break;
	    	
		case CONFIRM_SINGLE:
	    	i = new Intent(activity, CardSlideActivity.class);
	    	i.putExtras(BundleCreator.getConfirmBlack());
	    	break;
	    	
		case UNKNOWN:
			default:
			i = new Intent(activity, MainActivity.class);
			
    	}

    	activity.setIntent(i);
    	//activity.onCreate(null);
    	return i;

    }    

}
