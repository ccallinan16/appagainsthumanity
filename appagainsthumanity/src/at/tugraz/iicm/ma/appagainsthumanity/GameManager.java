package at.tugraz.iicm.ma.appagainsthumanity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mocks.MockDB;

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
	private HashMap<Long, Integer> playedCards;
	
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
			MockDB db = new MockDB();
			List<Integer> dealt = db.assignCards(numCards);
			
			
			dealtCards = new Integer[dealt.size()];
			dealtCards = dealt.toArray(dealtCards);
		}
		
		return dealtCards;
	}

	public HashMap<Long,Integer> getPlayedCards() {
	
		
		if (playedCards == null)
		{
			MockDB db = new MockDB();
			List<Integer> dealt = db.assignCards(userIDs.size());
			winnerCard = dealt.get(0);
			
			playedCards = new HashMap<Long,Integer>();
			int index = 0;
			for(long user : userIDs.values())
			{
				playedCards.put(user, dealt.get(index++));
			}
		}
		
		return playedCards;
	}

	public int getWinnerCard() {
				
		return winnerCard;
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
