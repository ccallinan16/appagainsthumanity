package at.tugraz.iicm.ma.appagainsthumanity.db;

import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class PresetHelper {

	public static final int NO_GAMES = 0; 		//1 user, no games
	public static final int SELECT_BLACK = 1; 	//3 users, 1 game, 2 rounds user has to choose black card
	public static final int SELECT_WHITE = 2; 	//3 users, 1 game, 2 rounds user has to choose white card
	public static final int SELECT_WINNER = 3; 	//3 users, 1 game, 2 rounds user has to choose white card

	
	public static void setPreset(DBProxy proxy, int type) {
		
		proxy.reinitializeDB();
		ServerConnector connector = new ServerConnector(proxy);
		GameManager man = new GameManager();
		
		switch(type)
		{
		case SELECT_BLACK:
			connector.startGame(man);
			connector.startRound(man);
			connector.selectCardBlack(man.getLastTurnID(), man.getSelectedBlack());
			connector.getPlayedCards(man);
			//the following two should be one function:
			connector.selectWinner(man.getLastTurnID(), man.getWinnerCard());
			connector.updateScore(man.getLastTurnID(), man.getWinnerCard());
			man.czar = man.getCurrentUserID();
			connector.startRound(man);
			break;
		case SELECT_WHITE:
			connector.startGame(man);
			connector.startRound(man);
			connector.selectCardBlack(man.getLastTurnID(), man.getSelectedBlack());
			connector.getPlayedCards(man);
			connector.selectWinner(man.getLastTurnID(), man.getWinnerCard());
			connector.updateScore(man.getLastTurnID(), man.getWinnerCard());
			connector.startRound(man);
			connector.dealCards(man.getLastTurnID(), CardType.WHITE, man.getDealtCardIDs());
			break;
			
		case NO_GAMES:
			connector.startGame(man); //adds users
			break;
		default:
			break;
		}
	}
	
	
		
}
