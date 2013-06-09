package at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import android.os.AsyncTask;
import at.tugraz.iicm.ma.appagainsthumanity.connection.OnResponseListener;
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
	private static final String FUNCTIONNAME_CHOOSEWINNERCARD	= "chooseWinnerCard";
	private static final String FUNCTIONNAME_ISREGISTERED 		= "isRegistedIdSet";
	
	private static final String KEY_ACTION = "action";
	private static final String KEY_UID    = "userid";
	private static final String KEY_TURNID = "turnid";
	private static final String KEY_CARDID = "cardid";

	
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

	/**
	 * as this function is called directly in an ui-thread, we need to start an async task to connect to the server
	 */
	@Override
	public boolean createGame(int userId, long[] invites, int roundcap, int scorecap, OnResponseListener responseListener) {
		//prepare data
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("roundcap", roundcap);
		data.put("scorecap", scorecap);
		HashMap<String, Object> inviteData = new HashMap<String, Object>();
		for(long id : invites) 
			inviteData.put(String.valueOf(id), id);
		data.put("invites", inviteData);
		
		data.put(KEY_UID, userId);
		data.put(KEY_ACTION, FUNCTIONNAME_CREATEGAME);
		
		System.out.println(data);
		
		ServerActionTask task = new ServerActionTask();
		
		task.setOnResponseListener(responseListener);
		task.execute(data);
		
		//TODO: always returns true, as its an async task, we don't wait for it.. 
		return true;
		
	}
	
	
	class ServerActionTask extends AsyncTask<HashMap<String,Object>, Void, Boolean> {

        private Exception exception;
        private OnResponseListener listener = null;
        private Object response = null;
        
        public void setOnResponseListener(OnResponseListener onResponseListener) {
        	this.listener = onResponseListener;
        }

        protected Boolean doInBackground(HashMap<String,Object>... data) {
            try {
            	
            	HashMap<String,Object> payload = data[0];
        		System.out.println(data);
        		System.out.println(payload);


            	//what to do
            	String function = (String) payload.get(KEY_ACTION);
    			Integer uid = (Integer) payload.get(KEY_UID);
    			
        		System.out.println(payload);

    			
    			payload.remove(KEY_ACTION);
    			payload.remove(KEY_UID);
    			
        		System.out.println(payload);

          		try{
            		System.out.println("in try " + payload);

        			Boolean b = null;
        			
                	if (function.equals(FUNCTIONNAME_CREATEGAME))
                	{
            			b = (Boolean) client.call(NAMESPACE_RPC + function, uid, payload);
                	}
                	else if (  function.equals(FUNCTIONNAME_CHOOSEBLACKCARD) 
                			|| function.equals(FUNCTIONNAME_CHOOSEWHITECARD)
                			|| function.equals(FUNCTIONNAME_CHOOSEWINNERCARD))
                	{
                		int cardID = (Integer) payload.get(KEY_CARDID);
                		int turnID = (Integer) payload.get(KEY_TURNID);
                		
                		b = (Boolean) client.call(NAMESPACE_RPC + function, uid, turnID, cardID); 
                	}
        			
        			response = b;
        			return b;

                	
        		} catch (XMLRPCException e) {
        			setDisconnected();
        			e.printStackTrace();
        			response = false;
        			return false;
        		}
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
        	if (listener != null) {
        		listener.onResponse(response);
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
	
	/**
	 * this function is called directly in the ui thread, so we need to start an async task
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean selectBlackCard(int userId, int turnId, int cardId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(KEY_UID, userId);
		data.put(KEY_TURNID, turnId);
		data.put(KEY_CARDID, cardId);
		data.put(KEY_ACTION, FUNCTIONNAME_CHOOSEBLACKCARD);
		ServerActionTask task = new ServerActionTask();		
		task.execute(data);
		return true;
	}
	
	/**
	 * this function is called directly in the ui thread, so we need to start an async task
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean selectWhiteCard(int userId, int turnId, int cardId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(KEY_UID, userId);
		data.put(KEY_TURNID, turnId);
		data.put(KEY_CARDID, cardId);
		data.put(KEY_ACTION, FUNCTIONNAME_CHOOSEWHITECARD);
		ServerActionTask task = new ServerActionTask();		
		task.execute(data);
		return true;
	}
	
	/**
	 * this function is called directly in the ui thread, so we need to start an async task
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean selectWinnerCard(int userId, int turnId, int cardId) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(KEY_UID, userId);
		data.put(KEY_TURNID, turnId);
		data.put(KEY_CARDID, cardId);
		data.put(KEY_ACTION, FUNCTIONNAME_CHOOSEWINNERCARD);
		ServerActionTask task = new ServerActionTask();		
		task.execute(data);
		System.out.println("task executing");
		return true;
	}

}
