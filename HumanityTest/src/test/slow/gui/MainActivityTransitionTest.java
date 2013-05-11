package test.slow.gui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

import com.xtremelabs.robolectric.matchers.StartedMatcher;

@RunWith(SQLTestRunner.class)
public class MainActivityTransitionTest {

    @Before
    public void setUp() throws Exception {
    	DBProxy proxy = new DBProxy(new Activity());
    	PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
    }
	
	/**
	 * creates a test for the Activities: MainActivity (with ListClick), and
	 * CardSlideActivity, running through the selection process.
	 * 
	 * @param context: must be either SELECT_BLACK or SELECT_WHITE
	 * @return the intent of the new/started activity
	 */
	public Intent createListTransition(ViewContext context)
	{
		MainActivity activity = new MainActivity();
		activity.onCreate(null);
		activity.onStart();
		
		ListView list = (ListView) activity.findViewById(R.id.game_list_view);

		/* tried to access imagebutton, but didn't manage to.
		
        LinearLayout firstItemLayout = (LinearLayout) list.getChildAt(0);
        
		System.out.println("children: " + firstItemLayout.getChildCount());
        
        list.findViewById(R.layout.listitem_gamelist);
        
		ImageButton thumbnail = (ImageButton)firstItemLayout.findViewById(R.id.thumbnail);

		thumbnail.performClick();
		*/
		GamelistAdapter adapter = (GamelistAdapter) list.getAdapter();
		adapter.simulateClick(context);

		CardSlideActivity csa = new CardSlideActivity();
				
    	Intent i = new Intent(csa, CardSlideActivity.class);
    	i.putExtras(BundleCreator.createBundle(context,PresetHelper.man.getLastTurnID()));

    	csa.setIntent(i);
		
		return i;//SelectionAndContextHelper.createAndGetIntent(csa, context);
	}
	
	@Test
	public void testTransitionFromListToCardSlideActivity()
	{
    	//Intent i = SelectionAndContextHelper.switchFromDisplayToMain(
    	//		activity, newActivity, ViewContext.CONFIRM_SINGLE,true);
		
		CardSlideActivity newActivity = new CardSlideActivity();
		
		Intent i = createListTransition(ViewContext.SELECT_BLACK);
		
    	Assert.assertThat(newActivity, new StartedMatcher(i));
    	
		newActivity = new CardSlideActivity();
		i = createListTransition(ViewContext.SELECT_WHITE);
		
    	Assert.assertThat(newActivity, new StartedMatcher(i));

	}
	
	@Test
	public void testTransitionFromListToCreateGameActivity()
	{
    	//Intent i = SelectionAndContextHelper.switchFromDisplayToMain(
    	//		activity, newActivity, ViewContext.CONFIRM_SINGLE,true);
		
		MainActivity activity = new MainActivity();
		activity.onCreate(null);
		activity.onStart();
		
		Button btn = (Button) activity.findViewById(R.id.button_creategame);
		btn.performClick();
    	Assert.assertThat(activity, new StartedMatcher(CreateGameActivity.class));

	}
	
}
