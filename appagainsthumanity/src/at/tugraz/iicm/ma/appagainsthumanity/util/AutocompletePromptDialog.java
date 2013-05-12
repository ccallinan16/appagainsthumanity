package at.tugraz.iicm.ma.appagainsthumanity.util;

/*
 * refer to: http://www.jjoe64.com/2011/06/prompt-dialog-for-android.html
 */

	import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleCursorAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
      
    /** 
     * helper for Prompt-Dialog creation 
     */  
    public abstract class AutocompletePromptDialog extends AlertDialog.Builder implements OnClickListener {  
     private final AutoCompleteTextView input;  
      
     /** 
      * @param context 
      * @param title resource id 
      * @param message resource id 
      */  
     public AutocompletePromptDialog(Context context, int title, int message, SimpleCursorAdapter adapter) {  
      super(context);  
      setTitle(title);  
      setMessage(message);  
      
      input = new AutoCompleteTextView(context);  
      input.setId(R.id.text1);
      input.setAdapter(adapter);
      input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 @Override
	    	 public void onItemClick(AdapterView<?> listView, View view,
	    			 				 int position, long id) {
	            // Get the cursor, positioned to the corresponding row in the result set
             Cursor cursor = (Cursor) listView.getItemAtPosition(position);
	 
             // Get the state's capital from this row in the database.
             String capital =	cursor.getString(cursor.getColumnIndexOrThrow(DBContract.User.COLUMN_NAME_USERNAME));
	 
             // Update the parent class's TextView
	            input.setText(capital);
	    	}
      });
      setView(input);  
      
      setPositiveButton(R.string.ok, this);  
      setNegativeButton(R.string.cancel, this);  
     }  
     
     /** 
      * @param context 
      * @param title resource id 
      * @param message resource id 
      * @param text default entry
      */  
     public AutocompletePromptDialog(Context context, int title, int message, String entry, SimpleCursorAdapter adapter) {  
		 super(context);  
	     setTitle(title);  
	     setMessage(message);  
	     
	     input = new AutoCompleteTextView(context);  
	     input.setText(entry);
	     input.setAdapter(adapter);
	     input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 @Override
	    	 public void onItemClick(AdapterView<?> listView, View view,
	    			 				 int position, long id) {
	            // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
	 
                // Get the state's capital from this row in the database.
                String capital =	cursor.getString(cursor.getColumnIndexOrThrow(DBContract.User.COLUMN_NAME_USERNAME));
	 
                // Update the parent class's TextView
	            input.setText(capital);
	    	}
	     });
	     
	     setView(input);  
	     setPositiveButton(R.string.ok, this);  
	     setNegativeButton(R.string.cancel, this);  
      
     }  
      
     /** 
      * will be called when "cancel" pressed. 
      * closes the dialog. 
      * can be overridden. 
      * @param dialog 
      */  
     public void onCancelClicked(DialogInterface dialog) {  
      dialog.dismiss();  
     }  
      
     @Override  
     public void onClick(DialogInterface dialog, int which) {  
      if (which == DialogInterface.BUTTON_POSITIVE) {  
       if (onOkClicked(input.getText().toString())) {  
        dialog.dismiss();  
       }  
      } else {  
       onCancelClicked(dialog);  
      }  
     }  
      
     /** 
      * called when "ok" pressed. 
      * @param input 
      * @return true, if the dialog should be closed. false, if not. 
      */  
     abstract public boolean onOkClicked(String input);  
    }  