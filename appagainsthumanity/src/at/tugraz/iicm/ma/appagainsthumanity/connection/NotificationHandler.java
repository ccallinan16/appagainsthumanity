package at.tugraz.iicm.ma.appagainsthumanity.connection;

import java.util.HashMap;
import java.util.TreeSet;

import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

/*
 * Class which handles reactive communication to server after recieving push notifications
 */

public class NotificationHandler {

    public static final int NOTIFICATION_NEW_GAME       = 0;
    public static final int NOTIFICATION_NEW_ROUND      = 1;
    public static final int NOTIFICATION_NEW_ROUND_CZAR = 2;
    public static final int NOTIFICATION_CHOSEN_BLACK   = 3;
    public static final int NOTIFICATION_CHOSEN_WHITE   = 4;
    public static final int NOTIFICATION_CHOSEN_WINNER  = 5; 
	
	private DBProxy dbProxy;

	/**
	 * TODO: use Google Cloud Messaging to send push notifications if there
	 * are any developments in the game. this way, we can implement all 
	 * functions that communicate with the server here.
	 */
	
	public NotificationHandler(DBProxy proxy) {
		this.dbProxy = proxy;
	}
	
	/*
	 * TODO: replace with push notification handling
	 * queries the server for status updates in existing games or new games
	 */
	public synchronized void checkAndHandleUpdates() {
		
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//retrieve notifications
		HashMap<String, String> result = serverProxy.getNotifications(dbProxy.getUserID());
		
		//if result == null, nothing to do here
		if (result == null)
			return;
		
		//otherwise iterate through notifications and invoke necessary callbacks
		for(String notificationIdString: new TreeSet<String>(result.keySet())) {
			int notificationId = Integer.parseInt(notificationIdString);
			int type = Integer.parseInt(result.get(notificationIdString));
			
			System.out.println("got notification of type: " + type + " with id: " + notificationId);
			
			handleUpdate(type,notificationId);
		}
	}

	public void handleUpdate(int type, int notificationId) {
		switch (type) {
		case NOTIFICATION_NEW_GAME:
			callbackNewGame(notificationId);
			break;
		case NOTIFICATION_NEW_ROUND:
			callbackNewRound(notificationId);
			break;
		case NOTIFICATION_NEW_ROUND_CZAR:
			callbackNewRoundCzar(notificationId);
			break;	
		case NOTIFICATION_CHOSEN_BLACK:
			callbackChosenBlack(notificationId);
			break;
		case NOTIFICATION_CHOSEN_WHITE:
			callbackChosenWhite(notificationId);
			break;
		case NOTIFICATION_CHOSEN_WINNER:
			callbackChosenWinner(notificationId);
			break;
		}
		
	}

	@SuppressWarnings("unchecked")
	private void callbackChosenWinner(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		HashMap<String, Object> result = (HashMap<String, Object>) serverProxy.getUpdate(notificationId);
		
		//update participation entries
		Object[] participationArray = (Object[]) result.get("participation");
		for (Object o : participationArray) {
			HashMap<String, String> participation = (HashMap<String, String>) o;
			dbProxy.getDBSetter().updateParticipation(Integer.parseInt(participation.get("id")), Integer.parseInt(participation.get("game_id")), 
												   	  Integer.parseInt(participation.get("user_id")), Integer.parseInt(participation.get("score")));
		}
		
		//update card entries
		Object[] cardsArray = (Object[]) result.get("cards");
		for(Object o : cardsArray) {
			HashMap<String, String> playedWhiteCard = (HashMap<String, String>) o;
			//update turn entry
			dbProxy.getDBSetter().updatePlayedWhiteCard(Integer.parseInt(playedWhiteCard.get("id")),
													    Integer.parseInt(playedWhiteCard.get("turn_id")), Integer.parseInt(playedWhiteCard.get("user_id")), 
													    Integer.parseInt(playedWhiteCard.get("white_card_id")), Boolean.parseBoolean(playedWhiteCard.get("won")));
		}
	}

	@SuppressWarnings("unchecked")
	private void callbackChosenWhite(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		Object[] result = (Object[]) serverProxy.getUpdate(notificationId);
		
		for(Object o : result) {
			HashMap<String, String> playedWhiteCard = (HashMap<String, String>) o;
			//update turn entry
			dbProxy.getDBSetter().addPlayedWhiteCard(Integer.parseInt(playedWhiteCard.get("id")),
													 Integer.parseInt(playedWhiteCard.get("turn_id")), Integer.parseInt(playedWhiteCard.get("user_id")), 
													 Integer.parseInt(playedWhiteCard.get("white_card_id")), Boolean.parseBoolean(playedWhiteCard.get("won")));
		}
	}

	@SuppressWarnings("unchecked")
	private void callbackChosenBlack(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		HashMap<String, String> turn = (HashMap<String, String>) serverProxy.getUpdate(notificationId);
		
		//update turn entry
		dbProxy.getDBSetter().updateTurn(Integer.parseInt(turn.get("id")), Integer.parseInt(turn.get("game_id")), Integer.parseInt(turn.get("roundnumber")),
				  Integer.parseInt(turn.get("user_id")), Integer.parseInt(turn.get("black_card_id")));
	}

	@SuppressWarnings("unchecked")
	private void callbackNewRoundCzar(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		HashMap<String, Object> result = (HashMap<String, Object>) serverProxy.getUpdate(notificationId);
		
		if (result == null)
			return;
		
		//add turn entry
		HashMap<String, String> turn = (HashMap<String, String>) result.get("turn");
		dbProxy.getDBSetter().addTurn(Integer.parseInt(turn.get("id")), Integer.parseInt(turn.get("game_id")), Integer.parseInt(turn.get("roundnumber")),
				  Integer.parseInt(turn.get("user_id")), Integer.parseInt(turn.get("black_card_id")));
		
		//update black cards
			//remove remaining cards
		dbProxy.getDBSetter().dropDealtBlackCards(Integer.parseInt(turn.get("game_id")));
			//add new black cards
		Object[] cardArray = (Object[]) result.get("cards");
		for(Object o : cardArray) {
			HashMap<String, String> blackCard = (HashMap<String, String>) o;
			//add card
			dbProxy.getDBSetter().addDealtBlackCard(Integer.parseInt(blackCard.get("id")), Integer.parseInt(blackCard.get("game_id")), 
													Integer.parseInt(blackCard.get("user_id")), Integer.parseInt(blackCard.get("black_card_id")));
		}
	}

	@SuppressWarnings("unchecked")
	private void callbackNewRound(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		HashMap<String, Object> result = (HashMap<String, Object>) serverProxy.getUpdate(notificationId);
		
		//add turn entry
		HashMap<String, String> turn = (HashMap<String, String>) result.get("turn");
		dbProxy.getDBSetter().addTurn(Integer.parseInt(turn.get("id")), Integer.parseInt(turn.get("game_id")), Integer.parseInt(turn.get("roundnumber")),
				  Integer.parseInt(turn.get("user_id")), Integer.parseInt(turn.get("black_card_id")));
		
		//update white cards
			//remove remaining cards
		dbProxy.getDBSetter().dropDealtWhiteCards(Integer.parseInt(turn.get("game_id")));
			//add new white cards
		Object[] cardArray = (Object[]) result.get("cards");
		for(Object o : cardArray) {
			HashMap<String, String> whiteCard = (HashMap<String, String>) o;
			//add card
			dbProxy.getDBSetter().addDealtWhiteCard(Integer.parseInt(whiteCard.get("id")), Integer.parseInt(whiteCard.get("game_id")), 
													Integer.parseInt(whiteCard.get("user_id")), Integer.parseInt(whiteCard.get("white_card_id")));
		}
	}

	@SuppressWarnings("unchecked")
	private void callbackNewGame(int notificationId) {
		//check if server connection is established, otherwise abort
		XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
		if (!serverProxy.isConnected())
			return;
		
		//query server
		HashMap<String, Object> result = (HashMap<String, Object>) serverProxy.getUpdate(notificationId);
		
		System.out.println("-----------------callbackNewGame:");
		for(String key : result.keySet())
			System.out.println(key);
		
		//add game entry
		HashMap<String, String> game = (HashMap<String, String>) result.get("game");
		dbProxy.getDBSetter().addGame(Integer.parseInt(game.get("id")), Integer.parseInt(game.get("roundcap")), Integer.parseInt(game.get("scorecap")));
		
		//add user entries
		Object[] userArray = (Object[]) result.get("user");
		for (Object o : userArray) {
			HashMap<String, String> user = (HashMap<String, String>) o;
			dbProxy.getDBSetter().addUser(Integer.parseInt(user.get("id")), user.get("username"));
		}
		
		//add participation entries
		Object[] participationArray = (Object[]) result.get("participation");
		for (Object o : participationArray) {
			HashMap<String, String> participation = (HashMap<String, String>) o;
			dbProxy.getDBSetter().addParticipation(Integer.parseInt(participation.get("id")), Integer.parseInt(participation.get("game_id")), 
												   Integer.parseInt(participation.get("user_id")), Integer.parseInt(participation.get("score")));
		}
	}

}
