package at.tugraz.iicm.ma.appagainsthumanity.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.TurnlistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class TurnsFragment extends Fragment {

	/*
	 * PRIVATE MEMBERS
	 */
	
	private ListView turnListView;
	private TurnlistAdapter turnListAdapter;
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
		DatabaseUtils.dumpCursor(turnListCursor);
		
		//create adapter
		turnListAdapter = new TurnlistAdapter(getActivity(), turnListCursor, username, chooseBlackCardListener, chooseWhiteCardListener, chooseWinningCardListener, showResultListener);

		//set adapter
		turnListView.setAdapter(turnListAdapter);
		
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
    
    /*
     * CALLBACKS
     */
    
    public OnClickListener chooseBlackCardListener = new OnClickListener() {
    	@Override
		public void onClick(View view) {
    		
        	Intent intent = new Intent(getActivity(), CardSlideActivity.class);
        	intent.putExtras(BundleCreator.createBundle(ViewContext.SELECT_BLACK, 0));
        	startActivity(intent);
		}
    };
    
    public OnClickListener chooseWhiteCardListener = new OnClickListener() {
    	@Override
		public void onClick(View view) {
        	Intent intent = new Intent(getActivity(), CardSlideActivity.class);
        	intent.putExtras(BundleCreator.createBundle(ViewContext.SELECT_WHITE, 0));
        	startActivity(intent);
		}
    };
    
    public OnClickListener chooseWinningCardListener = new OnClickListener() {
    	@Override
		public void onClick(View view) {
        	Intent intent = new Intent(getActivity(), CardSlideActivity.class);
        	intent.putExtras(BundleCreator.createBundle(ViewContext.SELECT_WHITE, 0));
        	startActivity(intent);
		}
    };
    
    public OnClickListener showResultListener = new OnClickListener() {
    	@Override
		public void onClick(View view) {
        	Intent intent = new Intent(getActivity(), CardSlideActivity.class);
        	intent.putExtras(BundleCreator.createBundle(ViewContext.SHOW_RESULT, 0));
        	startActivity(intent);
		}
    };
}
