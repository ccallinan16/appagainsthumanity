package at.tugraz.iicm.ma.appagainsthumanity.db;

import java.util.ArrayList;
import java.util.Random;

public class PresetHelper {

	ArrayList<Long> userIDs;
	ArrayList<Long> gameIDs;
	ArrayList<Long> turnIDs;
	private DBProxy proxy;

	
	public PresetHelper(DBProxy proxy) {
		// TODO Auto-generated constructor stub
		userIDs = new ArrayList<Long>();
		gameIDs = new ArrayList<Long>();
		turnIDs = new ArrayList<Long>();

		this.proxy = proxy;

	}
		
	public void addUsers(String[] usernames)
	{
		for (String str : usernames)
		{
			userIDs.add(proxy.addUser(str));
		}
	}
	
	public void addGame()
	{
		//TODO: replace values by real values
		gameIDs.add(proxy.addGame(true, 5, false, 0));
	}
	
	public void addTurn(long firstGame) {
		Random rand = new Random();
		
		turnIDs.add(proxy.addTurn(firstGame, turnIDs.size()+1, 
				rand.nextInt(userIDs.size()+1)+1, 
				null));
	}
	
	public void addParticipationAllPlayersToAllGames()
	{
		for (long game_id : gameIDs)
		{
			for (long player_id : userIDs)
			{
				proxy.addParticipation(game_id, player_id);
			}
		}
	}

	public void addDealtWhiteCards(long turn_id, Integer[] cardIDs) {
		for (Integer num : cardIDs)
			proxy.addDealtWhiteCards(turn_id, num);
	}

	
	public void updateScores(long turn_id) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPlayedCards(long turn_id) {
		//TODO: this function
		//proxy.addPlayedWhiteCard(turn_id, user_id, white_card_id, won);
	}

	public void updatePlayedWhiteCards(long turn_id, int chosen_card) {
		proxy.updatePlayedWhiteCard(turn_id, chosen_card);
	}
	
	public long getFirstGame() {
		if (gameIDs.size() > 0)
			return gameIDs.get(0);
		return 0;
	}

	public long getLastTurn() {
		if (turnIDs.size() > 0)
			return turnIDs.get(0);
		return 0;
	}


}
