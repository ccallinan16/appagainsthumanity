package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.app.Activity;
import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class ServerConnector {

	private DBProxy proxy;
	private PresetHelper preset;

	/**
	 * TODO: use Google Cloud Messaging to send push notifications if there
	 * are any developments in the game. this way, we can implement all 
	 * functions that communicate with the server here.
	 */
	
	public ServerConnector(DBProxy proxy) {
		// TODO Auto-generated constructor stub
		this.proxy = proxy;
		this.preset = new PresetHelper(proxy);
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
		
		//tables affected: (locally)
		//turn
			proxy.setBlackCardID(turn_id, id);
		//TODO: send info to server!
		
	}
	
	public void selectCardWhite(long turn_id, int id)
	{
		//Players select a whiteCard
		
		//tables affected:
		//played_white_cards
			proxy.setWhiteCardID(turn_id,proxy.getUserID(),id);
		//TODO: send info to server
	}
	
	public void selectWinner(long turn_id, int id)
	{
		//Czar selects the winning white card
		
		//tables affected:
		//played_white_cards
		proxy.updatePlayedWhiteCard(turn_id, id);
		//TODO: send info to server
	}
	
	/**
	 * server initiated
	 * @return the game_id of the started game.
	 */
	
	public long startGame()
	{
		//load information from server into db
		//TODO: replace presets with real data.	
		
		//tables affected:
		//users
		preset.addUsers(new String[] {proxy.getUsername(),
				"user2@dummy.com","user3@dummy.com"});
		
		//game
		//table game: add game
		preset.addGame();

		//participation (possibly confirmation?)
		preset.addParticipationAllPlayersToAllGames();
		
				
		return preset.getFirstGame();
	}
	
	public void endGame()
	{
		//delete stuff from server. 
		
	}
	
	public long startRound()
	{
		long game_id = preset.getFirstGame();
		if (game_id == 0)
			game_id = startGame();
		
		//tables affected:
		//turn
		preset.addTurn(game_id);
		
		//TODO: put presets here
		return preset.getLastTurn();
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
		
		//get a list of cardNums from Server
		preset.addDealtWhiteCards(turn_id, cardIds);
	}
	
	public void getPlayedCards(long turn_id)
	{
		preset.addPlayedCards(turn_id);
	}
	
	public void updateScore(long turn_id, int chosen_card)
	{
		//tables affected:
		//played_white_cards (to mark WON)
		//participation / (scores)
		preset.updatePlayedWhiteCards(turn_id,chosen_card);
		preset.updateScores(turn_id);
	}
}
