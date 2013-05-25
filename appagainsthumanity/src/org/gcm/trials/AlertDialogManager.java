package org.gcm.trials;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import at.tugraz.iicm.ma.appagainsthumanity.R;
 
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - success/failure (used to set icon)
     *               - pass null if you don't want icon
     * */
    public static void showAlertDialog(Context context, String title, String message,
            Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        if(status != null)
            // Setting alert dialog icon
            //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }

	public static void showErrorAlertDialog(Context context, String errorId, boolean status) {
		
		String title = "An Error occured";
		String message = "";
		
		if (errorId.equals("ACCOUNT_MISSING"))
		{			
			title = context.getString(R.string.gcm_no_account_title);
			message = context.getString(R.string.gcm_no_account_body);	
		}
		
		showAlertDialog(context, title, message, status);
		
	}
}