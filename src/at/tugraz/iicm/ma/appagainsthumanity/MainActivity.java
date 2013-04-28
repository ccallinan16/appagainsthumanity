package at.tugraz.iicm.ma.appagainsthumanity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView gameListView;
	private ArrayAdapter<String> gameArrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

}
