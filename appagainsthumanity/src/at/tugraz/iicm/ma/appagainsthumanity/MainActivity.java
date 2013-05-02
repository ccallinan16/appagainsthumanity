package at.tugraz.iicm.ma.appagainsthumanity;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

public class MainActivity extends Activity {

	private ListView gameListView;
	private ArrayAdapter<String> gameArrayAdapter;
	private DBProxy dbProxy;
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//get username
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();
		username = list[0].name;
		
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getApplicationContext());
		
		//retrieve game list
		Cursor c = dbProxy.readGameList(username);
		displayListView(c);
		
		//populate game list
		String[] stringarray = {"test1", "test2", "test3"};
		gameListView = (ListView) findViewById(R.id.game_list_view);
		gameArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray);
		gameListView.setAdapter(gameArrayAdapter);
		
		//populate database presets
		Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DBProxy.PRESETS);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void displayListView(Cursor c) {
		// The desired columns to be bound
		dbProxy.dumpTables();
		System.out.println("row count: " + c.getCount());
		
		System.out.println(DatabaseUtils.dumpCursorToString(c));
		/*
		  String[] columns = new String[] {
		    DBContract.Game._ID,
		    DBContract.Game.COLUMN_NAME_UPDATED,
		  };
		 
		  // the XML defined views which the data will be bound to
		  int[] to = new int[] {
		    R.id.code,
		    R.id.name,
		    R.id.continent,
		    R.id.region,
		  };
		 
		  // create the adapter using the cursor pointing to the desired data
		  //as well as the layout information
		  dataAdapter = new SimpleCursorAdapter(
		    this, R.layout.country_info,
		    cursor,
		    columns,
		    to,
		    0);
		 
		  ListView listView = (ListView) findViewById(R.id.listView1);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		  */
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public void createGame(View view) {
    	Intent intent = new Intent(this, CreateGameActivity.class);
//    	EditText editText = (EditText) findViewById(R.id.edit_message);
//    	String message = editText.getText().toString();
//    	intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);
    }

    public void showGallery(View view) {
    	
    	Bundle bundle = new Bundle();
    	bundle.putBoolean("SELECTABLE", true);
    	bundle.putBoolean("TOP_SINGLE", true);
    	bundle.putBoolean("BOTTOM_SINGLE",false);

    	Intent intent = new Intent(this, CardSlideActivity.class);
    	
    	intent.putExtras(bundle);
    	
    	startActivity(intent);
    }
    
    public void setPreset(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.presets_spinner);
    	dbProxy.setPreset(spinner.getSelectedItemPosition());
    	
    	Toast toast = Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT);
    	toast.show();
    	
    	finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
