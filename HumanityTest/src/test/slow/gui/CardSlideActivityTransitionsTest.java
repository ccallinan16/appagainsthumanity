package test.slow.gui;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import test.util.SelectionAndContextHelper;
import android.content.Intent;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

import com.xtremelabs.robolectric.matchers.StartedMatcher;
 
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
    	PresetHelper.setPreset(new DBProxy(newActivity), PresetHelper.SELECT_BLACK);

    	Intent i = SelectionAndContextHelper.switchFromDisplayToMain(
    			csa, newActivity, ViewContext.CONFIRM_SINGLE,PresetHelper.man.getLastTurnID());
    	Assert.assertThat(newActivity, new StartedMatcher(i));

    	newActivity = new MainActivity();
    	PresetHelper.setPreset(new DBProxy(newActivity), PresetHelper.SELECT_WHITE);

    	i = SelectionAndContextHelper.switchFromDisplayToMain(
    			csa, newActivity, ViewContext.CONFIRM_PAIR,PresetHelper.man.getLastTurnID());
    	Assert.assertThat(newActivity, new StartedMatcher(i)); 	
    }
    
    @Test
    public void testTransitionBlackSelectToDisplay()
    {
    	CardSlideActivity activity = new CardSlideActivity();
    	PresetHelper.setPreset(new DBProxy(activity), PresetHelper.SELECT_BLACK);
    	
    	Intent i = SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, activity, ViewContext.SELECT_BLACK,PresetHelper.man.getLastTurnID());
        Assert.assertThat(activity, new StartedMatcher(i));
    }
    
    @Test
    public void testTransitionWhiteSelectToDisplay()
    {
    	CardSlideActivity activity = new CardSlideActivity();
    	PresetHelper.setPreset(new DBProxy(activity), PresetHelper.SELECT_WHITE);

    	Intent i = SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, activity, ViewContext.SELECT_WHITE,PresetHelper.man.getLastTurnID());
        Assert.assertThat(activity, new StartedMatcher(i));
    }

}