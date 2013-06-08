package at.tugraz.iicm.ma.appagainsthumanity.connection;

import java.util.List;
import java.util.Map.Entry;

import mocks.IDToCardTranslator;
import android.os.AsyncTask;
import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

/*
 * Class which handles active communication requests to server
 */

public class ServerConnector {

	private DBProxy proxy;
	private static boolean isRobolectricTestrun = false;

	public static void setRobolectricTestrun() {
		isRobolectricTestrun = true;
	}
	
	public ServerConnector(DBProxy proxy) {
		// TODO Auto-generated constructor stub
		this.proxy = proxy;
	}
	
	/**
	 * user initiated
	 */
	public boolean initializeGame(long[] invites, int roundCap, int scoreCap) {
		if (isRobolectricTestrun) {
			//insert new game
			long gameId = proxy.getDBSetter().addGame(roundCap, scoreCap);
			
			//insert participation of invites
			for(long userId : invites) {
				proxy.getDBSetter().addParticipation(gameId, userId, 0);
			}
			
			//insert participation for player
			long userId = proxy.getUserID();
			proxy.getDBSetter().addParticipation(gameId,  userId, 0);
			
			//add turn
			proxy.getDBSetter().addTurn(gameId, 1, userId, null);
			
			return true;
		} else {
			XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
			
			System.out.println("is connected at this point: " + serverProxy.isConnected());
			
			//check connection
			if (!serverProxy.isConnected())
				return false;
						
			System.out.println("is connected at this point: " + proxy.getUserID() + " " + invites + " " + roundCap + " " + scoreCap);

			//we want to query the server, but in an async task. lets see if we can do this.
			
			
			
			//query server
			return serverProxy.createGame(proxy.getUserID(), invites, roundCap, scoreCap);
		}
	}
	
	public boolean selectCardBlack(long turn_id, int id)
	{		
		//Czar selects a black card, CardType.BLACK
		if (isRobolectricTestrun) {
			CardCollection.instance.setBlackCard(id);
			proxy.getDBSetter().setBlackCardID(turn_id, id);
			return true;
			
		} else {
			XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
			
			//check connection
			if (!serverProxy.isConnected())
				return false;
			
			//query server
			return serverProxy.selectBlackCard(proxy.getUserID(), (int) turn_id, id);
		}
		
	}
	
	public boolean selectCardWhite(long turn_id, int id)
	{
		//Players select a whiteCard
		if (isRobolectricTestrun) {		
			proxy.getDBSetter().setWhiteCardID(turn_id,proxy.getUserID(),id);
			return true;
		} else {
			XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
			
			//check connection
			if (!serverProxy.isConnected())
				return false;
			
			//query server
			return serverProxy.selectWhiteCard(proxy.getUserID(), (int) turn_id, id);
		}
	}
	
	public void selectWinner(long turn_id, int id)
	{
		//Czar selects the winning white card
		
		//tables affected:
		//played_white_cards
		proxy.getDBSetter().updatePlayedWhiteCard(turn_id, id);
		//TODO: send info to server
	}
	
	public void addUsers(GameManager man)
	{
		//tables affected:
		//users
		
		
		long id = 0;
		if (!isRobolectricTestrun) {
			id = XMLRPCServerProxy.getInstance().getUserId(proxy.getUsername());
		}
		
		System.out.println("addUser: " + id);
		
		if (id > 0)
			proxy.getDBSetter().addUser(id, proxy.getUsername());
		else
			id = proxy.getDBSetter().addUser(proxy.getUsername());
		
		System.out.println("addUser: " + id);
		//our own username
		man.setCurrentUser(id, proxy.getUsername());
				
		//entered usernames
		for (String name : man.users)
			man.addUserID(proxy.getDBSetter().addUser(name),name);

	}
	
	/**
	 * server initiated
	 * @return the game_id of the started game.
	 */
	
	public void startGame(GameManager man)
	{
		//load information from server into db
		
		//tables affected:
		addUsers(man);
		
		//game
		//table game: add game
		man.setGameID(proxy.getDBSetter().addGame(man.roundcap, man.scorecap));
		
		for (Long uid : man.getUserIDs())
			proxy.getDBSetter().addParticipation(man.gameID, uid, 0);				
	}
	
	public void endGame()
	{
		//delete stuff from server. 
		
	}
	
	public void startRound(GameManager man)
	{
		man.addTurnID(proxy.getDBSetter().addTurn(man.gameID, man.getRoundNum(), man.czar, 0));
	}
	
	
	public void dealCards(long turn_id, CardType type, Integer[] cardIds)
	{

		//deal BlackCards to person whose turn it is (to be Czar)
	
		//tables affected:
		//turn
		//TODO: right now, this is in startRound... what should we do here?
		//server action to select cards that have not been played before. 
		
		//---------------------------------------------------------------------
		
		
		//deal WhiteCards AFTER the selectCard Action on the Black Card
		//has happened to deal all white Cards
				
		//tables affected:
		//dealt_white_cards (player_id is useless locally, as we only store our own)
		for (int card : cardIds)
			proxy.getDBSetter().addDealtWhiteCards(turn_id, card);

		//get a list of cardNums from Server
	}
	
	public List<Integer> getDealtCards(IDToCardTranslator dealer, CardType cardType, long turnID) {

		//the user getting the cards the server dealt 
		
		//1. from server
		
		List<Integer> listIDs;
		
		//2. then from the database
		if (cardType.equals(CardType.WHITE))
			listIDs = proxy.getter.getDealtWhiteCards(turnID);
		else
			listIDs = proxy.getter.getDealtBlackCards(turnID);
		//	listIDs = dealer.getRandomBlackCardIDs(cardType); old method to deal randomized cards
						
		//3. then sets it to the card collection
		CardCollection.instance.setCards(listIDs, cardType);
		return listIDs;

	}

	public int getBlackCardForTurn(long turnID) {
		return proxy.getter.getBlackCard(turnID);
	}

		
	public void getPlayedCards(GameManager preset) {
		
		//TODO: don't add a card for czar!
		
		for (Entry<Integer,Long> entry : preset.getPlayedCards().entrySet())
		{
			proxy.getDBSetter().addPlayedWhiteCard(preset.getLastTurnID(), entry.getValue(), entry.getKey(), null);
		}
	}
	
	public List<Integer> getPlayedCards(long turnID) {
		return proxy.getter.getPlayedWhiteCards(turnID);
	}
	
	public void updateScore(long turn_id, int chosen_card)
	{
		//tables affected:
		//played_white_cards (to mark WON)
		//participation / (scores)
		proxy.getDBSetter().updatePlayedWhiteCard(turn_id,chosen_card);
		proxy.getDBSetter().updateScores(turn_id);
	}
	
	public long retrieveUserId(String username) {
		if (isRobolectricTestrun) {
			return proxy.getDBSetter().addUser(username);
		} else {
			XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
	
			//retrieve id of user from server
			int id = serverProxy.getUserId(username);
			
			System.out.println("retrieveUserID: " + id);
			
			//check connection
			if (!serverProxy.isConnected())
				return 0;
			
			//if id is valid, insert entry in local database
			if (id > 0)
				proxy.getDBSetter().addUser(id, username);
			
			//return success
			return id;
		}
	}

	public boolean registerUser(String username,int id) {
		if (isRobolectricTestrun) {
			return (proxy.getDBSetter().addUser(username) > 0);
		} else {
			/*XMLRPCServerProxy serverProxy = XMLRPCServerProxy.getInstance();
			
			//check connection
			if (!serverProxy.isConnected())
				return false;
			
			//retrieve user id from server
			int id = serverProxy.signupUser(username,regid);
			*/
			long retid = 0;
			//if id is valid, insert entry in local database
			if (id > 0)
				retid = proxy.getDBSetter().addUser(id, username);
			
			//return success
			return (retid > 0);
		}
	}
	
}
