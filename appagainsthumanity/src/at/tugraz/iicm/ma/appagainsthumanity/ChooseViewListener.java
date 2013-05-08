package at.tugraz.iicm.ma.appagainsthumanity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class ChooseViewListener implements OnClickListener {

	ViewContext context;
	long turn_id;
	Activity guiActivity;
	
	public ChooseViewListener(Activity activity, ViewContext context) {
		this.guiActivity = activity;
		this.context = context;
	}
	
	@Override
	public void onClick(View v) {
		//should make sure that turn_id is set
    	Intent intent = new Intent(guiActivity, CardSlideActivity.class);
    	intent.putExtras(BundleCreator.makeBundle(context,turn_id));
    	guiActivity.startActivity(intent);
	}

	public void setTurnID(long turn)
	{
		this.turn_id = turn;
	}
}
