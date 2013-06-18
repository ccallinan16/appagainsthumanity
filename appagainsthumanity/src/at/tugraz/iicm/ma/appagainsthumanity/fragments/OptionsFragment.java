package at.tugraz.iicm.ma.appagainsthumanity.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.util.MessageDialog;
import at.tugraz.iicm.ma.appagainsthumanity.util.PromptDialog;

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
		
		Game g = conn.getGame(game_id);
		
		if (g != null)
		{
			delete.setEnabled(g.isFinished);
			
			TextView view = (TextView) getActivity().findViewById(R.id.textMaxScore);
			view.setText(String.valueOf(g.maxScore));
			
			view = (TextView) getActivity().findViewById(R.id.textMaxRound);
			view.setText(String.valueOf(g.maxRounds));
		}
					
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				MessageDialog msg = new MessageDialog(v.getContext(),
						R.string.delete_title, R.string.delete_text) {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						conn.deleteGame(game_id);
						changeActivity();						
					}

				};
				
				msg.setNeutralButton(R.string.menu_cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					      dialog.dismiss();  						
					}
				});
				
				msg.show();
			}
		});
		//get other infos about game.
		
	}
	
	private void changeActivity() {
		Intent intent = new Intent(this.getActivity(),MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	this.getActivity().startActivity(intent);	

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
