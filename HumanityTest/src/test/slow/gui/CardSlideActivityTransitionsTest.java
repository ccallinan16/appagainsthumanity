package test.slow.gui;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        
    @Test
    public void testTransitionDisplayToMain()
    {
     	MainActivity newActivity = new MainActivity();
    	Intent i = SelectionAndContextHelper.switchFromDisplayToMain(
    			csa, newActivity, ViewContext.CONFIRM_SINGLE,true);
    	Assert.assertThat(newActivity, new StartedMatcher(i));

    	newActivity = new MainActivity();

    	i = SelectionAndContextHelper.switchFromDisplayToMain(
    			csa, newActivity, ViewContext.CONFIRM_PAIR,true);
    	Assert.assertThat(newActivity, new StartedMatcher(i)); 	
    }
    
    @Test
    public void testTransitionBlackSelectToDisplay()
    {
    	CardSlideActivity activity = new CardSlideActivity();
    	Intent i = SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, activity, ViewContext.SELECT_BLACK,true);
        Assert.assertThat(activity, new StartedMatcher(i));
    }
    
    @Test
    public void testTransitionWhiteSelectToDisplay()
    {
    	CardSlideActivity activity = new CardSlideActivity();

    	Intent i = SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, activity, ViewContext.SELECT_WHITE,true);
        Assert.assertThat(activity, new StartedMatcher(i));
    }

}