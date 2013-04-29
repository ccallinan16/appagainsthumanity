package at.tugraz.iicm.ma.appagainsthumanity;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import at.tugraz.iicm.ma.appagainsthumanity.util.PromptDialog;

public class CreateGameActivity extends Activity {

	private ListView inviteListView;
	private ArrayAdapter<String> inviteArrayAdapter ;
	private ArrayList<String> invitedPlayers;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		
		invitedPlayers = new ArrayList<String>();
		invitedPlayers.add("add Player");
		
		inviteListView = (ListView) findViewById(R.id.invites_list_view);
		inviteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position,	long id) {
				if ((position + 1) == invitedPlayers.size()) {
					//click on entry "add Player" -> add
					PromptDialog dlg = new PromptDialog(CreateGameActivity.this, R.string.prompt_new_invite_title, R.string.prompt_new_invite_comment) {
						@Override
						public boolean onOkClicked(String input) {
							if (!input.equals(""))
								addPlayer(input);
							return true; // true = close dialog
						}
					};
					dlg.show();
				} else {
					//click on existing entry -> edit
					
					System.out.println("item selected: " + position);
					
					PromptDialog dlg = new PromptDialog(CreateGameActivity.this, R.string.prompt_new_invite_title, R.string.prompt_new_invite_comment, invitedPlayers.get(position) ) {
						@Override
						public boolean onOkClicked(String input) {
							if (!input.equals(""))
								editPlayer(position, input);
							return true; // true = close dialog
						}
					};
					dlg.show();
				}
			}
		});
		
		
		String[] stringarray = invitedPlayers.toArray(new String[0]);
		inviteArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray);
		inviteListView.setAdapter(inviteArrayAdapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void addPlayer(String id) {
		this.invitedPlayers.add(invitedPlayers.size() - 1, id);
		String[] stringarray = invitedPlayers.toArray(new String[0]);
		inviteArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray);
		inviteListView.setAdapter(inviteArrayAdapter);
	}
	
	public void editPlayer(int position, String id) {
		this.invitedPlayers.set(position, id);
		String[] stringarray = invitedPlayers.toArray(new String[0]);
		inviteArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray);
		inviteListView.setAdapter(inviteArrayAdapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
