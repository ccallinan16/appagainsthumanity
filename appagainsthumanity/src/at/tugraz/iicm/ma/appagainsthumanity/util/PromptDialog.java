package at.tugraz.iicm.ma.appagainsthumanity.util;

/*
 * refer to: http://www.jjoe64.com/2011/06/prompt-dialog-for-android.html
 */

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import at.tugraz.iicm.ma.appagainsthumanity.R;
      
    /** 
     * helper for Prompt-Dialog creation 
     */  
    public abstract class PromptDialog extends MessageDialog {  
     private final EditText input;  
      
     /** 
      * @param context 
      * @param title resource id 
      * @param message resource id 
      */  
     public PromptDialog(Context context, int title, int message) {  
      super(context,title,message);  
      
      input = new EditText(context);  
      setView(input);  
      
     }  
     
     /** 
      * @param context 
      * @param title resource id 
      * @param message resource id 
      * @param text default entry
      */  
     public PromptDialog(Context context, int title, int message, String entry) {  
		 super(context,title,message);  
	     
	     input = new EditText(context);  
	     input.setText(entry);
	     setView(input);        
	     setNegativeButton(R.string.cancel, this);  

	           
     }  
      

     /** 
      * will be called when "cancel" pressed. 
      * closes the dialog. 
      * can be overridden. 
      * @param dialog 
      */  
     void onCancelClicked(DialogInterface dialog) {  
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