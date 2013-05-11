package test.slow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.GameOptionsActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowAlertDialog;

@RunWith(SQLTestRunner.class)
public class CreateGameActivityTest {

	private CreateGameActivity activity;
	
    @Before
    public void setUp() throws Exception {
    	activity = new CreateGameActivity();
    }
	
    @Test
	public void testInputUser()
	{
		activity.onCreate(null);
		activity.onStart();
		
		//open prompt
		Button btn = (Button) activity.findViewById(R.id.button_addplayer);
		btn.performClick();

		//insert first user
		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		TextView view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("person1");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		//get userlist
		ListView list = (ListView) activity.findViewById(R.id.players_list_view);
		
		assertEquals(1, list.getCount());
	}
    
    @Test
	public void testInputEmptyUser()
	{
		activity.onCreate(null);
		activity.onStart();
		
		//open prompt
		Button btn = (Button) activity.findViewById(R.id.button_addplayer);
		btn.performClick();

		//insert first user
		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		TextView view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		//get userlist
		ListView list = (ListView) activity.findViewById(R.id.players_list_view);
		
		assertEquals(0, list.getCount());
	}
    
    @Test
	public void testInputUserAlreadyInList()
	{
		activity.onCreate(null);
		activity.onStart();
		
		//open prompt
		Button btn = (Button) activity.findViewById(R.id.button_addplayer);
		btn.performClick();

		//insert first user
		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		TextView view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("user");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		//open prompt
		btn.performClick();

		//insert second user
		dialog = ShadowAlertDialog.getLatestAlertDialog();
		view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("user");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		//get userlist
		ListView list = (ListView) activity.findViewById(R.id.players_list_view);
		assertEquals(1, list.getCount());
	}
    
    @Test
	public void testTransitionToGameOptionsActivity()
	{
		activity.onCreate(null);
		activity.onStart();
		
		//open prompt
		Button btn = (Button) activity.findViewById(R.id.button_addplayer);
		btn.performClick();

		//insert first user
		AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
		TextView view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("user");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		//open prompt
		btn.performClick();

		//insert second user
		dialog = ShadowAlertDialog.getLatestAlertDialog();
		view = (TextView) dialog.findViewById(android.R.id.text1);
		view.setText("user2");
		dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		
		
		//click confirm button
		Button btnConfirm = (Button) activity.findViewById(R.id.button_confirm);
		btnConfirm.performClick();
		
		//assert started activity
		ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
	    Intent startedIntent = shadowActivity.getNextStartedActivity();
	    System.out.println(startedIntent.getComponent().getClassName());
		assertTrue(GameOptionsActivity.class.getName().equals(startedIntent.getComponent().getClassName()));
	}
}
