package at.tugraz.iicm.ma.appagainsthumanity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mocks.Shuffler;

public class GameManager {

	/**
	 * info set by user OR simulated by user-input
	 */
	public String[] users;
	public int roundcap;
	public int scorecap;
	public int numCards = 5;
	
	/**
	 * info set by database
	 */
	HashMap<String,Long> userIDs;
	ArrayList<Long> turnIDs;
	public long gameID;
	public long czar;
	private Integer[] dealtCards;
	private HashMap<Integer,Long> playedCards;
	
	private int winnerCard;
	private long currentUserID;
	
	
	private void init()
	{
		userIDs = new HashMap<String,Long>();
		turnIDs = new ArrayList<Long>();
	}
	
	public GameManager()
	{
		users = new String[]{"user1", "user2", "user3"};
		roundcap = 1;
		scorecap = 5;

		init();			
	}
	
	public List<Long> getUserIDs()
	{
		return new ArrayList<Long>(userIDs.values());
	}
	
	public void addUserID(long addUser, String name) {
		userIDs.put(name,addUser);
		czar = addUser;
	}

	public void setGameID(long addGame) {
		gameID = addGame;
	}

	public int getRoundNum() {
		return turnIDs.size()+1;
	}

	public void addTurnID(long addTurn) {
		turnIDs.add(addTurn);
	}

	public long getLastTurnID() {
		return turnIDs.get(turnIDs.size()-1);
	}

	public int getSelectedBlack() {
		return 13;
	}

	public int getSelectedWhite() {
		return 24;
	}
	
	public Integer[] getDealtCardIDs() {
		
		if (dealtCards == null || dealtCards.length == 0)
		{
			List<Integer> dealt = Shuffler.getRandomListOfInts(numCards);
						
			dealtCards = new Integer[dealt.size()];
			dealtCards = dealt.toArray(dealtCards);
		}
		
		return dealtCards;
	}

	public HashMap<Integer,Long> getPlayedCards() {
	
		
		if (playedCards == null)
		{
			List<Integer> dealt = Shuffler.getRandomListOfInts(userIDs.size());
			winnerCard = dealt.get(0);
			
			playedCards = new HashMap<Integer,Long>();
			int index = 0;
			for(long user : userIDs.values())
			{
				playedCards.put(dealt.get(index++),user);
			}
		}
		
		return playedCards;
	}

	public int getWinnerCard() {
				
		return winnerCard;
	}
	
	public long getUserIDForCard(int card)
	{
		return playedCards.get(card);
	}

	public void setCurrentUser(long addUser, String name)
	{
		addUserID(addUser, name);
		currentUserID = addUser;
	}
	
	public long getCurrentUserID() {
		return currentUserID;
	}

	
}
