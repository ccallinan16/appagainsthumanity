package at.tugraz.iicm.ma.appagainsthumanity;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.util.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.util.DBHelper;

public class MainActivity extends Activity {

	private ListView gameListView;
	private ArrayAdapter<String> gameArrayAdapter;
	private DBHelper dbHelper;
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//get username
		AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();
		
		System.out.println( list[0].name + " " + list[0].type + " " + list[0].toString());
		
		
		username = "testuser";
		
		
		// Instanciate database helper
		dbHelper = new DBHelper(this.getApplicationContext());
		
		// Gets the data repository in write mode
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID,
		    DBContract.Game.TABLE_NAME + "." + DBContract.Game.COLUMN_NAME_UPDATED
		};

		// How you want the results sorted in the resulting Cursor
		String sortOrder =
		    DBContract.Game.COLUMN_NAME_UPDATED + " DESC";

		Cursor c = db.query(
			DBContract.Game.TABLE_NAME +
			" INNER JOIN " + DBContract.Participation.TABLE_NAME + 
			" ON " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID +        
			" INNER JOIN " + DBContract.User.TABLE_NAME + " ON " + 
			DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " = " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_USER_ID,
			// The table to query
		    projection,                               // The columns to return
		    DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME + " = ?",  // The columns for the WHERE clause
		    new String[]{username},                   // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		
		String[] stringarray = {"test1", "test2", "test3"};
		gameListView = (ListView) findViewById(R.id.game_list_view);
		
		
		gameArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray);
		gameListView.setAdapter(gameArrayAdapter);
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
}
