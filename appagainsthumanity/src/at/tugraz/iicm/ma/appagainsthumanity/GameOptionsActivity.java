package at.tugraz.iicm.ma.appagainsthumanity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.connection.OnResponseListener;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

public class GameOptionsActivity extends VisibilityAwareActivity implements OnResponseListener {

	private long[] invites;
	private TextView textViewRoundcap;
	private TextView textViewScorecap;
	private DBProxy proxy;
	private ProgressBar bar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_options);
		
		//receive invite array from intent
		Intent intent = getIntent();
	    invites = intent.getLongArrayExtra(CreateGameActivity.EXTRA_INVITES);
	    
	    //define views
	    textViewRoundcap = (TextView) findViewById(R.id.input_roundcap);
	    textViewScorecap = (TextView) findViewById(R.id.input_scorecap);
	    bar = (ProgressBar) findViewById(R.id.progressBar);
	}

	/*
	 * CALLBACK METHODS
	 */
	
	public void confirmEntries(View view) {

		//option values
		int scorecap = -1;
		int roundcap = -1;
		
		//validate option values
		boolean valid = true;
		
		if (textViewScorecap.getText().length() == 0)
			scorecap = 0;
		else 
			scorecap = Integer.parseInt(String.valueOf(textViewScorecap.getText()));
		
		if (textViewRoundcap.getText().length() == 0)
			roundcap = 0;
		else
			roundcap = Integer.parseInt(String.valueOf(textViewRoundcap.getText()));
		
		if (scorecap < 0)
			valid = false;
		if (roundcap < 0)
			valid = false;
		if (scorecap == 0 && roundcap == 0)
			valid = false;
		
		//cancel if invalid
		if (!valid) {
			Toast.makeText(this, getString(R.string.game_options_toast_invalidselection), Toast.LENGTH_SHORT).show();
			return;
		}
		
		//update server information
		proxy = new DBProxy(this);
		ServerConnector connector = new ServerConnector(proxy);
		bar.setVisibility(View.VISIBLE);
		connector.initializeGame(invites, roundcap, scorecap, this);
	}
	
	public void onResponse(Object response) {
		bar.setVisibility(View.GONE);
		Boolean success = false;
		if (response != null)
			success = (Boolean) response;
		if (!success) {
			//connection error
			Toast.makeText(this, getString(R.string.game_options_toast_connectionerror), Toast.LENGTH_SHORT).show();
		} else {
			//create intent
			Intent intent = new Intent(this, MainActivity.class);
			//add flag to get back to main activity and clean intermediate activities
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//launch intent
			startActivity(intent);
		}
	}
	
	 @Override
    protected void onStop() {
    	try {
    		if (this.proxy != null) {
    			this.proxy.onStop();
    			this.proxy = null;
    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
		super.onStop();
    }
	
	/*
	 * DEFAULT METHODS
	 */
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_options, menu);
		return true;
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
