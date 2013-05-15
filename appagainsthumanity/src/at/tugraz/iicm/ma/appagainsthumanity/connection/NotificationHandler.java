package at.tugraz.iicm.ma.appagainsthumanity.connection;

import java.util.HashMap;

import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

/*
 * Class which handles reactive communication to server after recieving push notifications
 */

public class NotificationHandler {

	private DBProxy dbProxy;

	/**
	 * TODO: use Google Cloud Messaging to send push notifications if there
	 * are any developments in the game. this way, we can implement all 
	 * functions that communicate with the server here.
	 */
	
	public NotificationHandler(DBProxy proxy) {
		// TODO Auto-generated constructor stub
		this.dbProxy = proxy;
	}
	
	/*
	 * TODO: replace with push notification handling
	 * queries the server for status updates in existing games or new games
	 */
	public void checkAndHandleUpdates() {
		
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//retrieve list of known games and update time information
		HashMap<String, String> info = dbProxy.getter.getGameInfoList();

		//query server
		//HashMap<String, String> result = serverProxy.checkUpdates(dbProxy.getUsername(), info);
	}
	
	
}
