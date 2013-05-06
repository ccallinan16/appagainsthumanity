package test.slow;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

@RunWith(SQLTestRunner.class)
public class ServerConnectorTest {
		
	private DBProxy proxy;
	
	@Before
	public void setUp() throws Exception {
        Context c = new Activity();
        proxy = new DBProxy(c);

	}

    @After
    public void teardown() {
    }
    
    @Test
    public void testStartGame()
    {
    	//instead of getting stuff from the server, we just fill it with presets.
    	ServerConnector connector = new ServerConnector(proxy);
    	
    	long game_id = connector.startGame();
    	        
    	assertTrue(proxy.checkEntryExistsWhere(DBContract.Game.TABLE_NAME,
				DBContract.Game._ID+"="+game_id));
    	
    	assertTrue(proxy.checkEntryExistsWhere(DBContract.Participation.TABLE_NAME, 
								DBContract.Participation.COLUMN_NAME_GAME_ID+"="+game_id));
    	
    }

    @Test
    public void testStartRound()
    {
    	ServerConnector connector = new ServerConnector(proxy);
    	
    	long turn_id = connector.startRound();
    	        
    	proxy.printTables();

    	assertTrue(proxy.checkEntryExistsWhere(DBContract.Turn.TABLE_NAME, 
    			DBContract.Turn._ID+"="+turn_id + " AND " + 
    					DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID+" IS NULL"));
    }
    
    @Test
    public void testSelectBlackCard()
    {
    	ServerConnector connector = new ServerConnector(proxy);
    	long turn_id = connector.startRound();
    	
    	connector.selectCardBlack(turn_id, 13);
    	
    	proxy.printTables();

    	assertTrue(proxy.checkEntryExistsWhere(DBContract.Turn.TABLE_NAME, 
    			DBContract.Turn._ID+"="+turn_id + " AND " + 
    					DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID+"=13"));
    }
    
    @Test
    public void testDealWhiteCards()
    {
    	ServerConnector connector = new ServerConnector(proxy);
    	long turn_id = connector.startRound();
    	
    	connector.selectCardBlack(turn_id, 13);
    	
		//TODO: get from mockdealer
		Integer[] cardIds = new Integer[]{5,10,3,51,43}; 

    	connector.dealCards(turn_id,CardType.WHITE,cardIds);
    	
    	proxy.printTables();

    	for (Integer card : cardIds)
    	{
        	assertTrue(proxy.checkEntryExistsWhere(DBContract.DealtWhiteCard.TABLE_NAME, 
    				DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+card));

    	}
    }
    
    @Test
    public void testSelectWhiteCard()
    {
    	ServerConnector connector = new ServerConnector(proxy);
    	long turn_id = connector.startRound();
    	
    	connector.selectCardBlack(turn_id, 13);
    	
		//TODO: get from mockdealer
		Integer[] cardIds = new Integer[]{5,10,3,51,43}; 

    	connector.dealCards(turn_id,CardType.WHITE,cardIds);
    	
    	int card = 51;
    	connector.selectCardWhite(turn_id, card);
    	
    	proxy.printTables();

    	assertTrue(proxy.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+turn_id+" AND "+
    	    	DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+proxy.getUserID()+" AND "+
				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+card));

    	
    }
}
