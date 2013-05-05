package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import at.tugraz.iicm.ma.appagainsthumanity.R;

public abstract class MessageDialog extends AlertDialog.Builder implements OnClickListener {

	public MessageDialog(Context arg0, int title,int message) {
		super(arg0);
		setTitle(title);
		setMessage(message);
		
      setPositiveButton(R.string.menu_ok, this);  

	}

}
