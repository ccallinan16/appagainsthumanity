package at.tugraz.iicm.ma.appagainsthumanity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.util.AutocompletePromptDialog;

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
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getApplicationContext());
				
		//retrieve user list
		userCursor = dbProxy.readKnownOtherUsers(username);
		
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
		dbProxy.dumpTables();
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
					if (validateUser(input)){
						inviteArrayAdapter.add(input);
						inviteArrayAdapter.notifyDataSetChanged();
					}
					else 
						Toast.makeText(CreateGameActivity.this, getString(R.string.create_game_toast_invalidentry), Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		};
		dlg.show();
	}
	

	
	public void editPlayer(int position, String id) {
		if (validateUser(id)) {
			deletePlayer(position);
			inviteArrayAdapter.insert(id, position);
			inviteArrayAdapter.notifyDataSetChanged();
		} else 
			Toast.makeText(this, getString(R.string.create_game_toast_invalidentry), Toast.LENGTH_SHORT).show();
	}
	
	public void deletePlayer(int position) {
		inviteArrayAdapter.remove(inviteArrayAdapter.getItem(position));
		inviteArrayAdapter.notifyDataSetChanged();
	}
	
	@SuppressWarnings("deprecation")
	private boolean validateUser(String input) {
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
		

		//check if entry exists in userlist
		boolean found = false;
		
		if (userCursor.getCount() > 0)
		{
			userCursor.moveToFirst();

			do {
				if(input.equals(userCursor.getString(1)))
					found = true;
			} while (userCursor.moveToNext());

		}
		
		//if user was found in local list, return true
		if (found)
			return found;
		
		//else: check server information
		ServerConnector connector = new ServerConnector(dbProxy);
		if (connector.retrieveUserId(input) >= 1) {
			
			//update local cursor information
			
			userCursor = dbProxy.readKnownOtherUsers(username);
		
			//userAdapter.changeCursor(userCursor);

			return true;
		}
		//otherwise, user was not found
		return false;
	}
	
	private long retrieveUserId(String input) {
		if (input.equals(username))
			return -1;
		
		//check if entry exists in userlist
		long found = -1;
		userCursor.moveToFirst();
		do { 
			if(input.equals(userCursor.getString(1)))
				found = userCursor.getLong(0);
		} while (userCursor.moveToNext());
		
		return found;
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
	    long[] items = new long[inviteArrayAdapter.getCount()];
	    for (int position = 0; position < inviteArrayAdapter.getCount(); position++) {
	    	items[position] = retrieveUserId(inviteArrayAdapter.getItem(position));
	    	if (! (items[position] >= 1)) {
	    		//something went wrong here
	    		Toast.makeText(CreateGameActivity.this, getString(R.string.create_game_toast_invalidentry), Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    }
	    
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
