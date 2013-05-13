package at.tugraz.iicm.ma.appagainsthumanity.db;

import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class PresetHelper {

	public static final int NO_GAMES = 0; 		//1 user, no games
	public static final int SELECT_BLACK = 1; 	//3 users, 1 game, 2 rounds user has to choose black card
	public static final int SELECT_WHITE = 2; 	//3 users, 1 game, 2 rounds user has to choose white card
	public static final int SELECT_WINNER = 3; 	//3 users, 1 game, 1 round user has to choose white card

	public static final GameManager man = new GameManager();
	
	public static void setPreset(GameManager manager, DBProxy proxy, int type)
	{
		proxy.reinitializeDB();
		ServerConnector connector = new ServerConnector(proxy);
		
		switch(type)
		{
		case SELECT_BLACK:
			connector.startGame(manager);
			connector.startRound(manager);
			connector.selectCardBlack(manager.getLastTurnID(), manager.getSelectedBlack());
			connector.getPlayedCards(manager);
			//the following two should be one function:
			connector.selectWinner(manager.getLastTurnID(), manager.getWinnerCard());
			connector.updateScore(manager.getLastTurnID(), manager.getWinnerCard());
			
			manager.czar = manager.getCurrentUserID();
			connector.startRound(manager);
			break;
		case SELECT_WHITE:
			connector.startGame(manager);
			connector.startRound(manager);
			connector.selectCardBlack(manager.getLastTurnID(), manager.getSelectedBlack());
			connector.getPlayedCards(manager);
			connector.selectWinner(manager.getLastTurnID(), manager.getWinnerCard());
			connector.updateScore(manager.getLastTurnID(), manager.getWinnerCard());
			connector.startRound(manager);
			connector.dealCards(manager.getLastTurnID(), CardType.WHITE, manager.getDealtCardIDs());
			break;
		case SELECT_WINNER:
			connector.startGame(manager);
			manager.czar = manager.getCurrentUserID();
			connector.startRound(manager);
			connector.selectCardBlack(manager.getLastTurnID(), manager.getSelectedBlack());
			connector.getPlayedCards(manager);

			//the following two should be one function:
			//connector.selectWinner(manager.getLastTurnID(), manager.getWinnerCard());
			//connector.updateScore(manager.getLastTurnID(), manager.getWinnerCard());
			break;
		case NO_GAMES:
			connector.startGame(manager); //adds users
			break;
		default:
			break;
		}

	}
	
	public static void setPreset(DBProxy proxy, int type) {
		setPreset(man,proxy,type);
	}
	
	
		
}
