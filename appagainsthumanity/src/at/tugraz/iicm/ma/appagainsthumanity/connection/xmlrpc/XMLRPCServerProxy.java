package at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.os.AsyncTask;
import android.widget.Toast;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerProxy;

public class XMLRPCServerProxy extends ServerProxy{

	//TODO: read these informations from a config file / common prefs
	private static final String NAMESPACE_RPC 					= "aah.";
	private static final String NAMESPACE_NOTIFICATION 			= "notification.";
	private static final String FUNCTIONNAME_CHECKCONNECTION 	= "checkConnection";
	private static final String FUNCTIONNAME_REGISTERUSER 		= "registerUser";
	private static final String FUNCTIONNAME_GETUSERID 			= "getUserId";
	private static final String FUNCTIONNAME_CREATEGAME			= "createGame";
	private static final String FUNCTIONNAME_GETNOTIFICATIONS	= "getNotifications";
	private static final String FUNCTIONNAME_GETUPDATE 			= "getUpdate";
	private static final String FUNCTIONNAME_CHOOSEBLACKCARD	= "chooseBlackCard";
	private static final String FUNCTIONNAME_CHOOSEWHITECARD	= "chooseWhiteCard";
	private static final String FUNCTIONNAME_ISREGISTERED 		= "isRegistedIdSet";
	
	private XMLRPCClient client;
    private URI uri;
	
	public static XMLRPCServerProxy createInstance(String hostname) {
		instance = new XMLRPCServerProxy(hostname);
		return (XMLRPCServerProxy) instance;
	}
    
	public static XMLRPCServerProxy getInstance() {
		return (XMLRPCServerProxy) instance;
	}
	
	private XMLRPCServerProxy(String hostname) {
		 uri = URI.create(hostname);
	     client = new XMLRPCClient(uri);
	}
	
	@Override
	public boolean checkConnection() {
		try{
			Boolean b = (Boolean) client.call(NAMESPACE_RPC + FUNCTIONNAME_CHECKCONNECTION);
			setConnected();
			return b;
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}
	
	@Override
	public int signupUser(String username, String gcmid) {
		System.out.println("XMLRPCServerProxy::signupUser called " + username + " size: " + gcmid.length() + "\n" + gcmid);
		try{ 
			Object o = client.call(NAMESPACE_RPC + FUNCTIONNAME_REGISTERUSER, username,gcmid);
			int id = Integer.parseInt(o.toString());
			return id;
		} catch (XMLRPCException e) {
			setDisconnected();
			return 0;
		}
	}

	public boolean isRegIDSet(String username) {
		try{
			Object o = client.call(NAMESPACE_RPC + FUNCTIONNAME_ISREGISTERED, username);
			return Boolean.parseBoolean(o.toString());
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}

	
	@Override
	public int getUserId(String username) {
		try{
			Object o = client.call(NAMESPACE_RPC + FUNCTIONNAME_GETUSERID, username);
			int id = Integer.parseInt(o.toString());
			return id;
		} catch (XMLRPCException e) {
			setDisconnected();
			return 0;
		}
	}

	@Override
	public boolean createGame(int userId, long[] invites, int roundcap, int scorecap) {
		//prepare data
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("roundcap", roundcap);
		data.put("scorecap", scorecap);
		HashMap<String, Object> inviteData = new HashMap<String, Object>();
		for(long id : invites) 
			inviteData.put(String.valueOf(id), id);
		data.put("invites", inviteData);
		
		data.put("userid", userId);
		data.put("action", 1);
		
		AsyncTask<HashMap<String, Object>, Void, Boolean> task = new ServerActionTask();
		
		task.execute(data);
		
		//TODO: always returns true, as its an async task, we don't wait for it.. 
		return true;
		
		/*
		//query
		try{
			Boolean b = (Boolean) client.call(NAMESPACE_RPC + FUNCTIONNAME_CREATEGAME, userId, data);
			return b;
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}*/
	}
	
	
	class ServerActionTask extends AsyncTask<HashMap<String,Object>, Void, Boolean> {

        private Exception exception;

        protected Boolean doInBackground(HashMap<String,Object>... data) {
            try {
            	
            	//what to do
            	HashMap<String,Object> payload = data[0];
            	int i = (Integer) payload.get("action");
            	
            	switch(i)
            	{
            	case 1:
            		try{
            			int uid = (Integer) payload.get("userid");
            			
            			payload.remove("action");
            			payload.remove("userid");
            			
            			Boolean b = (Boolean) client.call(NAMESPACE_RPC + FUNCTIONNAME_CREATEGAME, uid, payload);
            			return b;
            		} catch (XMLRPCException e) {
            			setDisconnected();
            			return false;
            		}
            	}
            	     	           	
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Boolean b) {

        	if (exception != null)
        	{
        		exception.printStackTrace();
        	}
        }
     }
	

	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getNotifications(int userId) {
		try{
			Object o = client.call(NAMESPACE_NOTIFICATION + FUNCTIONNAME_GETNOTIFICATIONS, userId);
			if (HashMap.class.isInstance(o))
				return (HashMap<String, String>) o;
			return null;
		} catch (XMLRPCException e) {
			setDisconnected();
			return null;
		}
	}

	@Override
	public Object getUpdate(int notificationId) {
		try{
			return client.call(NAMESPACE_NOTIFICATION + FUNCTIONNAME_GETUPDATE, notificationId);
		} catch (XMLRPCException e) {
			setDisconnected();
			return null;
		}
	}
	
	@Override
	public boolean selectBlackCard(int userId, int turnId, int cardId) {
		try{
			return (Boolean) client.call(NAMESPACE_RPC + FUNCTIONNAME_CHOOSEBLACKCARD, userId, turnId, cardId);
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}
	
	@Override
	public boolean selectWhiteCard(int userId, int turnId, int cardId) {
		try{
			return (Boolean) client.call(NAMESPACE_RPC + FUNCTIONNAME_CHOOSEWHITECARD, userId, turnId, cardId);
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}

}
