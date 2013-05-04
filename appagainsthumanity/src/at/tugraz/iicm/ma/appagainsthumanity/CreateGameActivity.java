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

	/*
	 * CONSTANTS 
	 */
	
	private static final String STATE_INVITES = "STATE_INVITES";
	public static final String EXTRA_INVITES = "EXTRA_INVITES";
	
	/*
	 * PRIVATE MEMBERS
	 */
	
	private ListView inviteListView;
	private ArrayAdapter<String> inviteArrayAdapter ;
	private String username;
	private DBProxy dbProxy;
	private Cursor userCursor;
	private SimpleCursorAdapter userAdapter;
	
	/*
	 * LIFECYCLE METHODS
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_game);
		
		//retrieve username for validation
		username = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getString(getString(R.string.sharedpreferences_key_username), "");
		
		//initialize empty arrayadapter
		inviteArrayAdapter = new ArrayAdapter<String>(this, R.layout.listitem_invitelist);
		
		//initialize listView
		inviteListView = (ListView) findViewById(R.id.players_list_view);
		
		//add adaper to listview
		inviteListView.setAdapter(inviteArrayAdapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getApplicationContext());
				
		//retrieve user list
		userCursor = dbProxy.readKnownOtherUsers(username);
		
		DatabaseUtils.dumpCursor(userCursor);
		
		this.userAdapter = new SimpleCursorAdapter(this, 
		        android.R.layout.simple_list_item_1, 
		        userCursor,
		        new String[] { DBContract.User.COLUMN_NAME_USERNAME }, 
		        new int[] { android.R.id.text1 });
		
		//initialize prompt-onClickListener 
		inviteListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position,	long id) {
				//click on existing entry -> edit
				System.out.println("item selected: " + position);
				
				AutocompletePromptDialog dlg = new AutocompletePromptDialog(CreateGameActivity.this, R.string.prompt_new_invite_title, R.string.prompt_new_invite_comment, inviteArrayAdapter.getItem(position), userAdapter) {
					@Override
					public boolean onOkClicked(String input) {
						if (!input.equals(""))
							editPlayer(position, input);
						else
							deletePlayer(position);
						return true; // true = close dialog
					}
				};
				dlg.show();
		}
		});
		
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	   
	    // Restore state of list
	    for (String item : savedInstanceState.getStringArray(STATE_INVITES))
	    	inviteArrayAdapter.add(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
	    // Save current state of list
	    String[] items = new String[inviteArrayAdapter.getCount()];
	    for (int position = 0; position < inviteArrayAdapter.getCount(); position++)
	    	items[position] = inviteArrayAdapter.getItem(position);
	    
	    savedInstanceState.putStringArray(STATE_INVITES, items);
	    
	    // Always call the superclass so it can save the view hierarchy state
	    super.onSaveInstanceState(savedInstanceState);
	}
	
    @Override
    protected void onStop() {
    	try {
    		super.onStop();
    		if (this.userCursor != null){
    			this.userCursor.close();
    			this.userCursor = null;
    		}

    		if (this.dbProxy != null) {
    			this.dbProxy.onStop();
    			this.dbProxy = null;
    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
    }
	
	/*
	 * LISTUTILITY METHODS
	 */
	
	public void addPlayer(View view) {
		AutocompletePromptDialog dlg = new AutocompletePromptDialog(CreateGameActivity.this, R.string.prompt_new_invite_title, R.string.prompt_new_invite_comment, userAdapter) {
			@Override
			public boolean onOkClicked(String input) {
				if (!input.equals("")) {
					if (isValidEntry(input))
						inviteArrayAdapter.add(input);
					else 
						Toast.makeText(CreateGameActivity.this, getString(R.string.create_game_toast_invalidentry), Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		};
		dlg.show();
	}
	
	public void editPlayer(int position, String id) {
		if (isValidEntry(id)) {
			deletePlayer(position);
			inviteArrayAdapter.insert(id, position);
		} else 
			Toast.makeText(this, getString(R.string.create_game_toast_invalidentry), Toast.LENGTH_SHORT).show();
	}
	
	public void deletePlayer(int position) {
		inviteArrayAdapter.remove(inviteArrayAdapter.getItem(position));
	}
	
	private boolean isValidEntry(String input) {
		//TODO: check if provided name is in database
		//		if not query server and retrieve id
		//		if not on server, return false
		
		//check if entry = username
		if (input.equals(username))
			return false;
		
		//check if entry exists in array
		for (int i = 0; i < inviteArrayAdapter.getCount(); i++)
			if (inviteArrayAdapter.getItem(i).equals(input))
				return false;
		
		return true;
	}
	
	/*
	 * CALLBACK METHODS
	 */
	
	public void confirmEntries(View view) {
		//abort if num invites < 2
		if (inviteArrayAdapter.getCount() < 2) {
			Toast.makeText(this, getString(R.string.create_game_toast_notenoughplayers), Toast.LENGTH_SHORT).show();
			return;
		}

		//create intent
    	Intent intent = new Intent(this, GameOptionsActivity.class);
    	
    	//compile list of invites
	    String[] items = new String[inviteArrayAdapter.getCount()];
	    for (int position = 0; position < inviteArrayAdapter.getCount(); position++)
	    	items[position] = inviteArrayAdapter.getItem(position);
	    
	    //put extra and start activity
    	intent.putExtra(EXTRA_INVITES, items);
    	startActivity(intent);
	}
	
	/*
	 * DEFAULT METHODS
	 */

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
