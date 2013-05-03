package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import at.tugraz.iicm.ma.appagainsthumanity.R;

@SuppressLint("ValidFragment")
public class FireMissilesDialogFragment extends DialogFragment {
	
	CardFragmentAdapter pageAdapter;
	
    public FireMissilesDialogFragment(CardFragmentAdapter pageAdapter) {
    	this.pageAdapter = pageAdapter;
    }

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmation_dialog)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   pageAdapter.notifyDataSetChanged();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}