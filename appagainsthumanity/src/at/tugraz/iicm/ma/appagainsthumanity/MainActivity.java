package at.tugraz.iicm.ma.appagainsthumanity;

import static org.gcm.trials.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static org.gcm.trials.CommonUtilities.SENDER_ID;
import mocks.IDToCardTranslator;

import org.gcm.trials.AlertDialogManager;
import org.gcm.trials.ConnectionDetector;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.connection.NotificationHandler;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends VisibilityAwareActivity {

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
	public NotificationHandler notificationHandler;
	public static String username;
	private ProgressBar bar;
	private static boolean isFirstStart = true; //indicates first start
	
	
	/**
	 * Receiving push messages
	 * */
	private BroadcastReceiver mHandleMessageReceiver;

	
	/**
	 * member because it needs to be cancelled on destroy
	 */
	private ProgressTask asyncTask;
	private Context context = this;
	
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
		
		//bind views
		Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
		gameListView = (ListView) findViewById(R.id.game_list_view);
		bar = (ProgressBar) findViewById(R.id.progressBar);
		
		//get username
		getUsernameFromShared();
		
		//populate database presets
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DBProxy.PRESETS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		//set the translator in the Singleton
		CardCollection.instance.setTranslator(
				new IDToCardTranslator(this.getApplicationContext()));
		
		//prepare xmlrpc connection
		if (!ServerConnector.isRobolectricTestrun())
			XMLRPCServerProxy.createInstance(getString(R.string.xmlrpc_hostname));
		
		//indicate first start for notification polling
		if (savedInstanceState == null)
			isFirstStart = true;
	}
		
	public void getUsernameFromShared()
	{
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
				
				username = "emulatedUser@gmail.com";
				for (Account acc : list)
				{
					if (acc.type.equals("com.google"))
						username = acc.name;
				}
				System.err.println("username: " + username);
			}
			setUsername(username);
		} else {
			username = getApplicationContext().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getString(getString(R.string.sharedpreferences_key_username), "");
		}
	}
	
	public void setUsername(String name)
	{
		//supply username to shared preferences for other activities
		SharedPreferences.Editor editor = getApplicationContext()
				.getSharedPreferences(
						getString(R.string.sharedpreferences_filename), 
						Context.MODE_PRIVATE).edit();
		editor.putString(getString(R.string.sharedpreferences_key_username), name);
		//set flag
		editor.putBoolean(getString(R.string.sharedpreferences_key_username_defined), true);
		editor.commit();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getApplicationContext());
		notificationHandler = new NotificationHandler(dbProxy);
		
		//check if device is capable of gcm
		if (!ServerConnector.isRobolectricTestrun())
			checkGCMRequirements();
		
		//register the receiver for GCM events
		OnNotificationListener list = new OnNotificationListener() {
			@Override
			public void onResponse(int type, int game, String msg) {
				System.out.println("onResponse called, msg: " + msg);
				// TODO Auto-generated method stub
		        new NotificationUpdateTask().execute();
			}
		};
		
		mHandleMessageReceiver = new MyGCMBroadcastReceiver(dbProxy, list);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		//check for updates
		//if (!ServerConnector.isRobolectricTestrun())
		{
	        asyncTask = new ProgressTask();
	        asyncTask.execute();
		}
	}
	
    @Override
    protected void onStop() {
		if (asyncTask != null) {
			asyncTask.cancel(true);
		}
    	try {
    		if (this.gamelistCursor != null){
    			this.gamelistCursor.close();
    			this.gamelistCursor = null;
    		}
    		if (this.dbProxy != null) {
    			this.dbProxy.onStop();
    			this.dbProxy = null;
    		}
			unregisterReceiver(mHandleMessageReceiver);
			this.mHandleMessageReceiver = null;
			notificationHandler = null;
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
		super.onStop();
    }

   @Override
	protected void onDestroy() {
		try {
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			//bug in gcm, not our fault.
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
    	startActivity(intent);
    }
    
    public void refresh(View view) {
    	NotificationUpdateTask task = new NotificationUpdateTask();
    	task.execute();
    }
    
    public void setPreset(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
    	int pos = spinner.getSelectedItemPosition();
    	new PresetTask(pos).execute();
    	//dbProxy.setPreset(spinner.getSelectedItemPosition());
/*
    	Toast toast = Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
    	toast.show();
    	
    	finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);*/
    }
   			
	private class PresetTask extends AsyncTask <Void,Void,Void>{
		int preset;
		
		public PresetTask(int preset) {
			this.preset = preset;
		}
		
		@Override
		protected Void doInBackground(Void... params) {

			while (dbProxy == null);
			PresetHelper.setPreset(dbProxy, preset);
		
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
			Toast toast = Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
	    	toast.show();
	    	
	    	finish();
	        Intent intent = new Intent(MainActivity.this, MainActivity.class);
	        startActivity(intent);
		}
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
			// Internet Connection is not present
			AlertDialogManager.showAlertDialog(MainActivity.this,
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
	private boolean checkGCMRequirements()
	{	
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		return true;
	}
	
	
	public void gcmRegistrationProcess()
	{
		boolean flagRegistered = getApplicationContext()
									.getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE)
									.getBoolean(getString(R.string.sharedpref_key_registered), false);
		
		XMLRPCServerProxy server = XMLRPCServerProxy.getInstance();
		ServerConnector connector = new ServerConnector(dbProxy);

		if (flagRegistered && server.isRegIDSet(username) && connector.retrieveUserId(username) >= 0)
		{
			
			/*
			//TODO: for debugging, we want to continue to register.
			GCMRegistrar.unregister(context);
			
			SharedPreferences.Editor editor = getApplicationContext()
					.getSharedPreferences(
							getString(R.string.sharedpreferences_filename), 
							Context.MODE_PRIVATE).edit();
			
			editor.putBoolean(getString(R.string.sharedpref_key_registered), false);
			editor.commit();

			
			
			setRegistered(false);*/
			return; //no registration necessary anymore
		}

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(context);

		System.err.println("reg id: " + regId);

		// Check if regid already presents
		if (regId.equals("")) {
			
			// Registration is not present, register now with GCM, 
			//this will call the XMLRPCServer register function, but not the local one.
			GCMRegistrar.register(context, SENDER_ID);
			
			int id = XMLRPCServerProxy.getInstance().getUserId(username);
			connector.registerUser(username, id);
			
			
		} else {
			// Device is already registered on GCM

			//TODO: check for this, but while we're still resetting our db, this will return true even if
			//regid is not in our database... 
			if (GCMRegistrar.isRegisteredOnServer(context) && server.isRegIDSet(username)) {
				// Skips registration.

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
				    }
				});

				//make sure user is registered in local database as well
				int id = XMLRPCServerProxy.getInstance().getUserId(username);
				connector.registerUser(username, id);

			} else  {
				
				int id = server.signupUser(username, regId);
				
				if (id > 0)
					connector.registerUser(username, id);
				else
				{
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, getString(R.string.main_toast_connectionerror), Toast.LENGTH_SHORT).show();
						    }
						});
					//TODO: retry!
					return;
				}

				GCMRegistrar.setRegisteredOnServer(context, true);
			}
    	
		}
		
		setRegistered(true);
		
	}
	
	private void setRegistered(boolean bool)
	{
		SharedPreferences.Editor editor = getApplicationContext()
				.getSharedPreferences(
						getString(R.string.sharedpreferences_filename), 
						Context.MODE_PRIVATE).edit();
		
		editor.putBoolean(getString(R.string.sharedpref_key_registered), bool);
		editor.commit();

	}

	

	/**
	 * handles the registration process: username, gcmid
	 * 
	 *
	 */
	private class ProgressTask extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	    	gameListView.setVisibility(View.GONE);
	        bar.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected Void doInBackground(Void... arg0) {   
	    	
	    	if (ServerConnector.isRobolectricTestrun())
	    		return null;
	    	
	    	/**
	    	 * check if device is connected to the internet
	    	 */
	    	if (!checkConnection())
	    		return null;

	    	if (!XMLRPCServerProxy.getInstance().isConnected()) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainActivity.this, getString(R.string.main_toast_connectionerror), Toast.LENGTH_SHORT).show();
					    }
					});
				return null;
		     }
		     		 		     
	    	//check and process notifications only on first start
		    if (isFirstStart) {
		    	isFirstStart = false;

		    	//register gcm
			    gcmRegistrationProcess();
		    	notificationHandler.checkAndHandleUpdates();
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
			DatabaseUtils.dumpCursor(gamelistCursor);
			displayListView(gamelistCursor);
	    }
	    
		/**
		 * checks if mobile device has internet switched on.
		 */
		private boolean checkConnection()
		{
			ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

			// Check if Internet present
			if (!cd.isConnectingToInternet()) {
				// Internet Connection is not present
				runOnUiThread(new Runnable() {
					public void run() {
						AlertDialogManager.showAlertDialog(MainActivity.this,
								"Internet Connection Error",
								"Please connect to working Internet connection", false);
					    }
				});
				// stop executing code by return
				return false;
			}
			return true;
		}
	}
		
	/**
	 * handles GCM update notifications
	 */
	private class NotificationUpdateTask extends AsyncTask <Void,Void,Void>{
	    @Override
	    protected void onPreExecute(){
	    	if (gameListView != null)
	    		gameListView.setVisibility(View.GONE);
	        if (bar != null)
	        	bar.setVisibility(View.VISIBLE);
	    }

	    @Override
	    protected Void doInBackground(Void... arg0) {   
			//check and process notifications only on first start
	    	notificationHandler.checkAndHandleUpdates();
		    return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	    	
	        //hide progress bar
	    	if (bar != null)
	    		bar.setVisibility(View.GONE);
	    	if (gameListView != null)
	    	gameListView.setVisibility(View.VISIBLE);
			
	        //retrieve and show game list
	    	if (gamelistCursor != null && dbProxy != null)
	    	{
	    		gamelistCursor = dbProxy.readGameList(username);
	    		displayListView(gamelistCursor);
	    	}
	    }
	}

}
