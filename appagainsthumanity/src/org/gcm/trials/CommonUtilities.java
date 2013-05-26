package org.gcm.trials;
 
import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    // give your server registration url here
    public static final String SERVER_URL = 
    		"http://192.168.1.120/serverAgainstHumanity/module/Application/src/Application/Model/gcm/register.php";
    //"http://10.0.2.2/gcm_server_php/register.php";
 
    // Google project id
    public static final String SENDER_ID = "803533367860";
 
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "AndroidHive GCM";
 
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
 
    public static final String KEY_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessageByBroadcast(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(KEY_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}