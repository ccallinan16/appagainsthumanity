package at.tugraz.iicm.ma.appagainsthumanity;

import mocks.IDToCardTranslator;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.connection.NotificationHandler;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

public class MainActivity extends Activity {

	/*
	 * CONSTANTS
	 */
	public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
	public static final String EXTRA_GAMEID = "EXTRA_GAMEID";
	
	/*
	 * PRIVATE MEMBER VARIABLES
	 */
	private ListView gameListView;
	private GamelistAdapter gamelistAdapter;
	public DBProxy dbProxy;
	private String username;
	private ProgressBar bar;
	
	//database
	private Cursor gamelistCursor;
	
	/**
	 * gcm
	 */
	AsyncTask<Void, Void, Void>	mRegisterTask;
	
	/*
	 * LIFECYCLE METHODS
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**
		 * 
		 */
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			if (regId.equals("")) {
			  GCMRegistrar.register(this, SENDER_ID);
			} else {
			  System.out.println("Already registered");
			}
		} catch (Exception e)
		{
			//for the testcases, this does not matter right now.
			System.err.println(e.getMessage());
		}

		
		
		//retrieve username flag
		boolean flagUsernameExists = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getBoolean(getString(R.string.sharedpreferences_key_username_defined), false);
		
		if (!flagUsernameExists) {
			//get username
			AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
			if (manager == null)
				username="emulatedUser@gmail.com";
			else
			{
				Account[] list = manager.getAccounts();
				if (list.length == 0) {
					//TODO: handle non-existing google account
					username="emulatedUser@gmail.com";
				} else
					username = list[0].name;
			}
			
			//supply username to shared preferences for other activities
			SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).edit();
			editor.putString(getString(R.string.sharedpreferences_key_username), username);
			//set flag
			editor.putBoolean(getString(R.string.sharedpreferences_key_username_defined), true);
			editor.commit();
		} else {
			username = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getString(getString(R.string.sharedpreferences_key_username), "");
		}
		
		//populate database presets
		Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DBProxy.PRESETS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		//bind gameListView
		gameListView = (ListView) findViewById(R.id.game_list_view);
	}
	
	public void setUsername(String name)
	{
		//supply username to shared preferences for other activities
		SharedPreferences.Editor editor = getApplicationContext()
				.getSharedPreferences(
						getString(R.string.sharedpreferences_filename), 
						Context.MODE_PRIVATE).edit();
		editor.putString(getString(R.string.sharedpreferences_key_username), name);
		editor.commit();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getApplicationContext());
		
		//set the translator in the Singleton
		CardCollection.instance.setTranslator(
				new IDToCardTranslator(this.getApplicationContext()));
		
		//check connection
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		System.out.println(serverProxy.isConnected());
		
		if (!checkConnection())
			return;
		handleRegistrationWithGCM();

		//prepare xmlrpc connection
		XMLRPCServerProxy.createInstance(getString(R.string.xmlrpc_hostname));
		
		//check for updates
        bar = (ProgressBar) findViewById(R.id.progressBar);
        new ProgressTask().execute();
	}
	
    @Override
    protected void onStop() {
    	try {
    		super.onStop();
    		if (this.gamelistCursor != null){
    			this.gamelistCursor.close();
    			this.gamelistCursor = null;
    		}

    		if (this.dbProxy != null) {
    			this.dbProxy.onStop();
    			this.dbProxy = null;
    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
    }

   @Override
	protected void onDestroy() {
		
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();

	} 
    
	/*
	 * UTILITY METHODS
	 */
    
	private void displayListView(Cursor c) {
		// The desired columns to be bound
//		dbProxy.dumpTables();
//		System.out.println("row count: " + c.getCount());
//		System.out.println(DatabaseUtils.dumpCursorToString(c));
		if (gamelistAdapter == null)
			gamelistAdapter = new GamelistAdapter(this,c); //TODO
		else
			gamelistAdapter.changeCursor(c);
		gameListView.setAdapter(gamelistAdapter);
		//add onClick listener 
		gameListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//click on existing entry -> edit
				Intent intent = new Intent(MainActivity.this, GameOverviewActivity.class);
				intent.putExtra(EXTRA_GAMEID, id);
				startActivity(intent);
			}
		});
	}
	
    public void createGame(View view) {
    	Intent intent = new Intent(this, CreateGameActivity.class);
//    	EditText editText = (EditText) findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }
    
    public void setPreset(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
    	
    	PresetHelper.setPreset(dbProxy, spinner.getSelectedItemPosition());
    	
    	//dbProxy.setPreset(spinner.getSelectedItemPosition());

    	Toast toast = Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
    	toast.show();
    	
    	finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
        
    /*
     * DEFAULT METHODS
     */
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * checks if mobile device has internet switched on.
	 */
	private boolean checkConnection()
	{
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			AlertDialogManager alert = new AlertDialogManager();
			// Internet Connection is not present
			alert.showAlertDialog(MainActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return false;
		}
		return true;
	}
	
	/**
	 * GCM Handling 
	 * 
	 * 2. get username
	 * 3. check if already registered
	 * 4. if not -> register.
	 */
	private void handleRegistrationWithGCM()
	{	
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		
		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			// Registration is not present, register now with GCM			
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
				ServerUtilities.unregister(this,regId);
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				// Asyntask
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						XMLRPCServerProxy.getInstance().signupUser(username, regId);

						ServerUtilities.register(context, username, null, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}


	}
	
	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
			
			// Showing received message
//			lblMessage.append(newMessage + "\n");			
			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	

	private class ProgressTask extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	    	gameListView.setVisibility(View.GONE);
	        bar.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected Void doInBackground(Void... arg0) {   
	    	 XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		        if (serverProxy.isConnected()) {
					//register user
					//TODO: in production, check in sharedPref-entry whether registration has already happened
					//      in the meantime register all over in case the database was dropped 
					ServerConnector connector = new ServerConnector(dbProxy);
					connector.registerUser(username);
					
					//check and process notifications
					NotificationHandler handler = new NotificationHandler(dbProxy);
					handler.checkAndHandleUpdates();
				} else {
					//show toast and notify user about connection problems
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, getString(R.string.main_toast_connectionerror), Toast.LENGTH_SHORT).show();
						    }
						});
				}
		        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	        //hide progress bar
	    	bar.setVisibility(View.GONE);
	    	gameListView.setVisibility(View.VISIBLE);
			
	        //retrieve and show game list
			gamelistCursor = dbProxy.readGameList(username);
			displayListView(gamelistCursor);
	    }
	}

}
