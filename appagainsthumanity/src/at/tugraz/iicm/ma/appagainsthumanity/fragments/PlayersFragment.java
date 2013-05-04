package at.tugraz.iicm.ma.appagainsthumanity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class PlayersFragment extends Fragment {
	/*
	 * PRIVATE MEMBERS
	 */
	
	private ListView playersListView;
	private SimpleCursorAdapter playersListAdapter;
	private long game_id;
	private DBProxy dbProxy;
	private Cursor playersListCursor;
	
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
        return (LinearLayout)inflater.inflate(R.layout.tab_players_layout, container, false);
    }
    
    @SuppressWarnings("deprecation")
	@Override
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getActivity());
				
		//retrieve user_id from intent
		game_id = getActivity().getIntent().getExtras().getLong(MainActivity.EXTRA_GAMEID);
		
		//retrieve listview
		playersListView = (ListView) getActivity().findViewById(R.id.listview_players);
		
		//retrieve game list
		playersListCursor = dbProxy.readPlayerList(game_id);
		
		//create adapter
		playersListAdapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem_playerlist, playersListCursor,
		        new String[] { DBContract.User.COLUMN_NAME_USERNAME, DBContract.Participation.COLUMN_NAME_SCORE }, 
		        new int[] { R.id.tv_username, R.id.tv_score });

		//set adapter
		playersListView.setAdapter(playersListAdapter);
	}
    
    @Override
	public void onStop() {
    	try {
    		super.onStop();
    		if (this.playersListCursor != null){
    			this.playersListCursor.close();
    			this.playersListCursor = null;
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
