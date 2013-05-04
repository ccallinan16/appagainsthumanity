package test.slow;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import test.util.SelectionAndContextHelper;
import test.util.TestBundleCreator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.matchers.StartedMatcher;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;
 
@RunWith(PathTestRunner.class)
public class CardSlideActivityTransitionsTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }

    
    private void testContextSwitchDisplayToPopupHelper(Bundle b)
    {
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	SelectionAndContextHelper.performClick(csa);

        AlertDialog alert = ShadowAlertDialog.getLatestAlertDialog();
        if (alert == null)
        	fail("no Popup created");
        
        ShadowAlertDialog sAlert = Robolectric.shadowOf(alert);
        
        assertTrue(sAlert.getTitle().toString().        		
        		equals(csa.getString(R.string.pop_title_select)));
        
    }
    
    @Test
    public void testContextSwitchSelectToPopup()
    {
    	testContextSwitchDisplayToPopupHelper(
    			TestBundleCreator.getSelectBlackBundle());
    	    	
    	testContextSwitchDisplayToPopupHelper(
    			TestBundleCreator.getSelectBlackBundle());
    	
    }

    
    private void testContextSwitchDisplaytoMain(ViewContext context1, ViewContext context2)
    {
    	Bundle b = TestBundleCreator.createBundle(context1);
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	SelectionAndContextHelper.performClick(csa);
    	
    	MainActivity newActivity = new MainActivity();
    	Intent i = SelectionAndContextHelper.createAndGetIntent(newActivity,ViewContext.UNKNOWN);
    	
    	//newActivity.onCreate(null);
        Assert.assertThat(newActivity, new StartedMatcher(i));
    }
    
    private void testContextSwitchSelectToDisplay(Bundle b, ViewContext context1, ViewContext context2)
    {
    	
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	Card c = SelectionAndContextHelper.getFirstCard(csa, 
    			(context1.equals(ViewContext.SELECT_BLACK))
    			?CardType.BLACK
    			:CardType.WHITE);
    	
    	SelectionAndContextHelper.selectCardAndPerformClick(csa,c);
    	
    	Activity newActivity = new CardSlideActivity();
    	
    	Intent i = new Intent(newActivity, CardSlideActivity.class);
    	
    	switch (context2)
    	{
    	case UNKNOWN:
    		newActivity = new MainActivity();
    		i = new Intent(newActivity,MainActivity.class);
    		break;
    	case CONFIRM_PAIR:
        	i.putExtras(TestBundleCreator.createBundle(context2));
        	break;
    	case CONFIRM_SINGLE:
        	i.putExtras(TestBundleCreator.createBundle(context2));
        	break;
        	default:
    	}
    	    	
        Assert.assertThat(newActivity, new StartedMatcher(i));

    }
    
    @Test
    public void testTransitionWhiteSelectToDisplay()
    {
    	testContextSwitchSelectToDisplay(
    			TestBundleCreator.getSelectBlackBundle(),
    			ViewContext.SELECT_BLACK, ViewContext.CONFIRM_SINGLE);
    	
    	testContextSwitchSelectToDisplay(
    			TestBundleCreator.getSelectWhiteBundle(),
    			ViewContext.SELECT_WHITE, ViewContext.CONFIRM_PAIR);
    	
    	/*
    	 * these tests only work with the other ones executed before them: singleton!
    	 */
    	testContextSwitchDisplaytoMain(ViewContext.CONFIRM_PAIR, ViewContext.UNKNOWN);
    	testContextSwitchDisplaytoMain(ViewContext.CONFIRM_PAIR, ViewContext.UNKNOWN);
    }

}