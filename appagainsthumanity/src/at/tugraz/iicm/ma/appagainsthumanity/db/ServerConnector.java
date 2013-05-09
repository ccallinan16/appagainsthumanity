package at.tugraz.iicm.ma.appagainsthumanity.db;

import java.util.List;
import java.util.Map.Entry;

import mocks.MockDealer;
import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class ServerConnector {

	private DBProxy proxy;

	/**
	 * TODO: use Google Cloud Messaging to send push notifications if there
	 * are any developments in the game. this way, we can implement all 
	 * functions that communicate with the server here.
	 */
	
	public ServerConnector(DBProxy proxy) {
		// TODO Auto-generated constructor stub
		this.proxy = proxy;
	}
	
	
	/**
	 * user initiated
	 */
	public void initializeGame() {
		
		//tables affected:
		
		//users
		//game 
		
		//--> send to server
	}
	
	public void selectCardBlack(long turn_id, int id)
	{		
		//Czar selects a black card, CardType.BLACK
		CardCollection.instance.setBlackCard(id);
		
		//tables affected: (locally)
		//turn
		proxy.getDBSetter().setBlackCardID(turn_id, id);
		//TODO: send info to server!
		
	}
	
	public void selectCardWhite(long turn_id, int id)
	{
		//Players select a whiteCard
		
		//tables affected:
		//played_white_cards
			proxy.getDBSetter().setWhiteCardID(turn_id,proxy.getUserID(),id);
		//TODO: send info to server
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
		
		//our own username
		man.setCurrentUser(proxy.getDBSetter().addUser(proxy.getUsername()), proxy.getUsername());
				
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
	
	public List<Integer> getDealtCards(MockDealer dealer, CardType cardType, long turnID) {

		//the user getting the cards the server dealt 
		
		//1. from server
		
		List<Integer> listIDs;
		
		//2. then from the database
		if (cardType.equals(CardType.WHITE))
			listIDs = proxy.getter.getDealtWhiteCards(turnID);
		else
			listIDs = dealer.getRandomBlackCardIDs(cardType); //TODO, tmp only
						
		//3. then sets it to the card collection
		CardCollection.instance.setCardsForPager(dealer, listIDs, cardType);
		return listIDs;

	}

	public int getBlackCardForTurn(long turnID) {
		return proxy.getter.getBlackCard(turnID);
	}

		
	public void getPlayedCards(GameManager preset) {
		
		for (Entry<Long, Integer> entry : preset.getPlayedCards().entrySet())
		{
			proxy.getDBSetter().addPlayedWhiteCard(preset.getLastTurnID(), entry.getKey(), entry.getValue(), null);
		}
	}

	
	public void updateScore(long turn_id, int chosen_card)
	{
		//tables affected:
		//played_white_cards (to mark WON)
		//participation / (scores)
		proxy.getDBSetter().updatePlayedWhiteCard(turn_id,chosen_card);
		proxy.getDBSetter().updateScores(turn_id);
	}






}
