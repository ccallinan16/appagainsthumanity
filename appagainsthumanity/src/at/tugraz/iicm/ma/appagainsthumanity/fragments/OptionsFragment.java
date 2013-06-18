package at.tugraz.iicm.ma.appagainsthumanity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

public class OptionsFragment extends Fragment {
	
	private long game_id;
	private DBProxy dbProxy;
	private ServerConnector conn;

	
    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
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
        return (LinearLayout)inflater.inflate(R.layout.tab_options_layout, container, false);
    }
    
	@Override
	public void onStart() {
		super.onStart();
		// Instanciate database proxy
		dbProxy = new DBProxy(this.getActivity());
				
		conn =  new ServerConnector(dbProxy);
		//retrieve user_id from intent
		game_id = getActivity().getIntent().getExtras().getLong(MainActivity.EXTRA_GAMEID);

		ImageButton delete =  (ImageButton) getActivity().findViewById(R.id.BtnDeleteGame);
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				conn.deleteGame(game_id);
				
				//TODO: dialog
				//TODO: check if game currently running, then don't delete. 
				
				//go back to main, nothing here to see. //TODO: possible dialog
				Intent intent = new Intent(v.getContext(),MainActivity.class);
		    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			  	v.getContext().startActivity(intent);									
			}
		});
		
		
		//get other infos about game.
		
	}
	
    @Override
	public void onStop() {
    	try {
    		super.onStop();

    		if (this.dbProxy != null) {
    			this.dbProxy.onStop();
    			this.dbProxy = null;
    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
    }

}
