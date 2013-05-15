package at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc;

import java.net.URI;
import java.util.HashMap;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;

import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerProxy;

public class XMLRPCServerProxy extends ServerProxy{

	private XMLRPCClient client;
    private URI uri;
	
	public static XMLRPCServerProxy getInstance() {
		if (instance == null)
			instance = new XMLRPCServerProxy();
		return (XMLRPCServerProxy) instance;
	}
	
	private XMLRPCServerProxy() {
		 uri = URI.create("http://10.0.0.3/");
	     client = new XMLRPCClient(uri);
	}
	
	@Override
	public boolean checkConnection() {
		try{
			Boolean b = (Boolean) client.call("aah.checkConnection");
			setConnected();
			return b;
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}
	
	@Override
	public int signupUser(String username) {
		try{
			Object o = client.call("aah.signupUser", username);
			int id = Integer.parseInt(o.toString());
			return id;
		} catch (XMLRPCException e) {
			setDisconnected();
			return 0;
		}
	}

	@Override
	public int getUserId(String username) {
		try{
			Object o = client.call("aah.getUserId", username);
			int id = Integer.parseInt(o.toString());
			return id;
		} catch (XMLRPCException e) {
			setDisconnected();
			return 0;
		}
	}

	@Override
	public boolean createGame(String username, long[] invites, int roundcap, int scorecap) {
		//prepare data
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("roundcap", roundcap);
		data.put("scorecap", scorecap);
		HashMap<String, Object> inviteData = new HashMap<String, Object>();
		for(long id : invites) 
			inviteData.put(String.valueOf(id), id);
		data.put("invites", inviteData);
		
		//query
		try{
			Boolean b = (Boolean) client.call("aah.createGame", username, data);
			return b;
		} catch (XMLRPCException e) {
			setDisconnected();
			return false;
		}
	}

	@Override
	public HashMap<String, String> checkUpdates(String username, HashMap<String, String> info) {
		try{
			Object o = client.call("aah.checkUpdates", username, info);
			return (HashMap<String, String>) o;
		} catch (XMLRPCException e) {
			setDisconnected();
			return null;
		}
	}

}
