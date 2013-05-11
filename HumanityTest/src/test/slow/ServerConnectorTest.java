package test.slow;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

@RunWith(SQLTestRunner.class)
public class ServerConnectorTest {
		
	private DBProxy proxy;
	ServerConnector connector;
	GameManager preset;
	
	@Before
	public void setUp() throws Exception {
        Context c = new Activity();
        proxy = new DBProxy(c);
        connector = new ServerConnector(proxy);
    	preset = new GameManager();

	}

    @After
    public void teardown() {
		proxy.onStop();
    }
    
    /**
     * userside + serverside are equal
     */
    
    @Test
    public void testStartGame()
    {
       	connector.startGame(preset);
    	        
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.Game.TABLE_NAME,
				DBContract.Game._ID+"="+preset.gameID));
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.Participation.TABLE_NAME, 
								DBContract.Participation.COLUMN_NAME_GAME_ID+"="+preset.gameID));
    	
    }
    
    /**
     * userside + serverside are equal
     */
    @Test
    public void testStartRound()
    {
    	connector.startGame(preset);
    	connector.startRound(preset);
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.Turn.TABLE_NAME, 
    			DBContract.Turn._ID+"="+preset.getLastTurnID() + " AND " + 
    					DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID+"=0"));

    }
    
    @Test
    public void testSelectBlack()
    {
    	/**
    	 * setup
    	 */
    	connector.startGame(preset);
    	connector.startRound(preset);
    	    	
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.Turn.TABLE_NAME, 
    			DBContract.Turn._ID+"="+preset.getLastTurnID() + " AND " + 
    					DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID+"="+preset.getSelectedBlack()));

    }
    
    @Test
    public void testDealWhiteCards()
    {
    	/**
    	 * setup
    	 */
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());

    	connector.dealCards(preset.getLastTurnID(), CardType.WHITE, preset.getDealtCardIDs());
    	
    	for (Integer card : preset.getDealtCardIDs())
    	{
        	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.DealtWhiteCard.TABLE_NAME, 
    				DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+card));

    	}
    }

    @Test
    public void testSelectWhiteCard()
    {
    	/**
    	 * setup
    	 */
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	connector.dealCards(preset.getLastTurnID(), CardType.WHITE, preset.getDealtCardIDs());
    	
    	connector.selectCardWhite(preset.getLastTurnID(), preset.getSelectedWhite());
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    	    	DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+proxy.getUserID()+" AND "+
				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+preset.getSelectedWhite()));
    }

    
    @Test
    public void testGetPlayedCards()
    {
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	
    	connector.getPlayedCards(preset);
    	   	
		for (Entry<Integer,Long> entry : preset.getPlayedCards().entrySet())
    	{
        	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
        			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
        	    	DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+entry.getValue()+" AND "+
    				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+entry.getKey()));
    	}

    }
    
    
    @Test
    public void testSelectWinner()
    {
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	connector.getPlayedCards(preset);

    	connector.selectWinner(preset.getLastTurnID(), preset.getWinnerCard());
    	    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()
    			+" AND "+
				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+preset.getWinnerCard() 
				+ " AND " +
				DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+preset.getUserIDForCard(preset.getWinnerCard())
				+ " AND " +
    			DBContract.PlayedWhiteCard.COLUMN_NAME_WON
				));
    }
    
    @Test
    public void getGameIDFromTurnID()
    {
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	connector.getPlayedCards(preset);
    	connector.selectWinner(preset.getLastTurnID(), preset.getWinnerCard());
    	connector.updateScore(preset.getLastTurnID(), preset.getWinnerCard());

    	assertEquals(preset.gameID,proxy.getter.getGameIDFromTurn(preset.getLastTurnID()));
    }
    
    
    @Test
    public void updateScore()
    {
    	connector.startGame(preset);
    	connector.startRound(preset);
    	connector.selectCardBlack(preset.getLastTurnID(), preset.getSelectedBlack());
    	connector.getPlayedCards(preset);
    	connector.selectWinner(preset.getLastTurnID(), preset.getWinnerCard());
    	
    	connector.updateScore(preset.getLastTurnID(), preset.getWinnerCard());
    	
    	assertEquals(preset.getUserIDForCard(preset.getWinnerCard()),
    			proxy.getter.getUserOfWonCard(preset.getLastTurnID()));
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(
    			DBContract.Participation.TABLE_NAME, 
    			DBContract.Participation.COLUMN_NAME_GAME_ID+"="+preset.gameID
    			+" AND "+
				DBContract.Participation.COLUMN_NAME_USER_ID+"="+
    				preset.getUserIDForCard(preset.getWinnerCard())
				+ " AND " +
    			DBContract.Participation.COLUMN_NAME_SCORE+"=1"
				));

    }
    
}
