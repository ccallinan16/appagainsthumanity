package at.tugraz.iicm.ma.appagainsthumanity;

import android.app.Activity;

public class VisibilityAwareActivity extends Activity {
	@Override
	protected void onResume() {
	  super.onResume();
	  AAHApplication.activityResumed();
	}

	@Override
	protected void onPause() {
	  super.onPause();
	  AAHApplication.activityPaused();
	}

}
