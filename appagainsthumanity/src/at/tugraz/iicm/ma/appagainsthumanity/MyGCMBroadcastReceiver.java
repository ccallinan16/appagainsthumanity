package at.tugraz.iicm.ma.appagainsthumanity;

import org.gcm.trials.CommonUtilities;
import org.gcm.trials.WakeLocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.connection.NotificationHandler;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;


public class MyGCMBroadcastReceiver extends BroadcastReceiver {
	
	protected static final String KEY_NOTIFICATION_TYPE = "notification_type";
	protected static final String KEY_NOTIFICATION_ID = "notification_id";
	DBProxy proxy;
	OnNotificationListener listener;
	
	public MyGCMBroadcastReceiver(DBProxy proxy, OnNotificationListener listener) {
		this.proxy = proxy;
		this.listener = listener;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		System.err.println("BroadcastReceiver CALLED!!");
		String newMessage = intent.getExtras().getString(CommonUtilities.KEY_MESSAGE);
		//String notificationid = intent.getExtras().getString(KEY_NOTIFICATION_ID);
		//String type = intent.getExtras().getString(KEY_NOTIFICATION_TYPE);

		if (listener != null)
			listener.onResponse(1, 1, "hello!");
		else
			new UpdateTask(proxy).execute();

		
		// Waking up mobile if it is sleeping
		WakeLocker.acquire(context.getApplicationContext());
		
		//TODO: possibly just do a background thread!
		
		// Showing received message
//		lblMessage.append(newMessage + "\n");			
		Toast.makeText(context.getApplicationContext(), "User Action Required", Toast.LENGTH_SHORT).show();
		
		// Releasing wake lock
		WakeLocker.release();
	}
	
	//better to do in main thread!
	private class UpdateTask extends AsyncTask <Void,Void,Void>{

		private NotificationHandler handler;

		public UpdateTask(DBProxy proxy) {
			this.handler = new NotificationHandler(proxy);
		}
		
		@Override
		protected Void doInBackground(Void... params) {

			handler.checkAndHandleUpdates();
			
			//int type = intent.getExtras().getInt(KEY_NOTIFICATION_TYPE);
			//int notificationID = intent.getExtras().getInt(KEY_NOTIFICATION_ID);
			//handler.handleUpdate(type,notificationID);

			
			return null;
		}
	}

}
