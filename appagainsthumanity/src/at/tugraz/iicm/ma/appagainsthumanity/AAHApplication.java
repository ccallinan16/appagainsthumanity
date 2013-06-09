package at.tugraz.iicm.ma.appagainsthumanity;

import android.app.Application;

/**
 * this class is needed to track whether we are currently interacting with the user or not (notifications)
 * @author egetzner
 *
 */
public class AAHApplication extends Application {
	  public static boolean isActivityVisible() {
	    return activityVisible;
	  }  

	  public static void activityResumed() {
	    activityVisible = true;
	  }

	  public static void activityPaused() {
	    activityVisible = false;
	  }

	  private static boolean activityVisible;

}
