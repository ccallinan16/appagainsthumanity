package test.slow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.GameOptionsActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R.id;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;

@RunWith(SQLTestRunner.class)
public class GameOptionsActivityTest {

	private GameOptionsActivity activity;
	private TextView score;
	private TextView rounds;
	private Button confirm;
	
    @Before
    public void setUp() throws Exception {
    	activity = new GameOptionsActivity();
    	Intent newIntent = new Intent();
    	newIntent.putExtra(CreateGameActivity.EXTRA_INVITES, new long[]{3, 4});
    	activity.setIntent(newIntent);
    	activity.onCreate(null);
    	score = (TextView) activity.findViewById(id.input_scorecap);
    	rounds = (TextView) activity.findViewById(id.input_roundcap);
    	confirm = (Button) activity.findViewById(id.button_confirm);
    }
	
    @Test
	public void testNoEntries()
	{
    	score.setText("");
    	rounds.setText("");
    	confirm.performClick();
    	
    	//check that nothing was started
    	//assert started activity
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    assertEquals(startedIntent, null);
	}
    
    @Test
	public void testInvalidRound()
	{
    	score.setText("1");
    	rounds.setText("-1");
    	confirm.performClick();
    	
    	//check that nothing was started
    	//assert started activity
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    assertEquals(startedIntent, null);
	}
    
    @Test
	public void testInvalidScore()
	{
    	score.setText("-1");
    	rounds.setText("1");
    	confirm.performClick();
    	
    	//check that nothing was started
    	//assert started activity
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    assertEquals(startedIntent, null);
	}
    
    @Test
	public void testTransitionToMainActivity()
	{
    	DBProxy proxy = new DBProxy(new Activity());
    	PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
    	proxy.onStop();
    	
    	score.setText("1");
    	rounds.setText("1");
    	confirm.performClick();
    	
		//assert started activity
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
		assertTrue(MainActivity.class.getName().equals(startedIntent.getComponent().getClassName()));
		
//		proxy = new DBProxy(activity);
//		assertEquals(proxy.readGameList(proxy.getUsername()).getCount(), 2);
		
	}
    
}
