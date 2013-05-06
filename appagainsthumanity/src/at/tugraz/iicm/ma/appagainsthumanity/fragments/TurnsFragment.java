package at.tugraz.iicm.ma.appagainsthumanity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class TurnsFragment extends Fragment {

	/*
	 * PRIVATE MEMBERS
	 */
	
	private ListView turnListView;
	private SimpleCursorAdapter turnListAdapter;
	private String username;
	private long game_id;
	private DBProxy dbProxy;
	private Cursor turnListCursor;
	
	/*
	 * LIFECYCLE METHODS
	 */
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        return (LinearLayout)inflater.inflate(R.layout.tab_turns_layout, container, false);
    }
    
    @SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getActivity());
				
		//retrieve username for validation
		username = getActivity().getSharedPreferences(getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getString(getString(R.string.sharedpreferences_key_username), "");
		
		//retrieve user_id from intent
		game_id = getActivity().getIntent().getExtras().getLong(MainActivity.EXTRA_GAMEID);
		
		//retrieve listview
		turnListView = (ListView) getActivity().findViewById(R.id.listview_turns);
		
		//retrieve game list
		turnListCursor = dbProxy.readTurnlist(game_id);
		
		//create adapter
		turnListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem_turnlist, turnListCursor,
		        new String[] { DBContract.Turn.COLUMN_NAME_ROUNDNUMBER }, 
		        new int[] { android.R.id.text1 });

		//set adapter
		turnListView.setAdapter(turnListAdapter);
		
		//add onclick listener
		turnListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//set cursor to current position
				turnListCursor.moveToPosition(position);
				
				//react depending on situation
				if (turnListCursor.getString(3).equals(username) && turnListCursor.getLong(4) == 0) {
					//choose black card
					Intent intent = new Intent(getActivity(), CardSlideActivity.class);
		        	intent.putExtras(BundleCreator.getSelectBlack());
		        	startActivity(intent);
				} else if (!turnListCursor.getString(3).equals(username) && turnListCursor.getInt(5) < (turnListCursor.getInt(2) - 1)) {
					//choose white card
					Intent intent = new Intent(getActivity(), CardSlideActivity.class);
		        	intent.putExtras(BundleCreator.getSelectWhite());
		        	startActivity(intent);
				} else if (turnListCursor.getString(3).equals(username) && turnListCursor.getInt(5) == (turnListCursor.getInt(2) - 1)) {
					//choose winning card
					Intent intent = new Intent(getActivity(), CardSlideActivity.class);
		        	intent.putExtras(BundleCreator.getSelectWhite());
		        	startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), CardSlideActivity.class);
		        	intent.putExtras(BundleCreator.getShowResults());
		        	startActivity(intent);
				}
				
			}
		});
	}
    
    @Override
	public void onStop() {
    	try {
    		super.onStop();
    		if (this.turnListCursor != null){
    			this.turnListCursor.close();
    			this.turnListCursor = null;
    		}

    		if (this.dbProxy != null) {
    			this.dbProxy.onStop();
    			this.dbProxy = null;
    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
    }
}
