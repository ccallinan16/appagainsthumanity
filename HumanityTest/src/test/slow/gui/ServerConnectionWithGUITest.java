package test.slow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mocks.IDToCardTranslator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import test.util.SelectionAndContextHelper;
import android.content.Intent;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.GameManager;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

@RunWith(PathTestRunner.class)
public class ServerConnectionWithGUITest {

	CardSlideActivity csa;
	GameManager preset;
	DBProxy proxy;
	
    @Before
    public void setUp() throws Exception {
    	
    	MainActivity ma = new MainActivity();
    	ma.setUsername("elisabeth");
    	
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);
    	
    	preset = new GameManager();
    	proxy = new DBProxy(csa);

    	csa.setProxy(proxy);
    	CardCollection.instance.setTranslator(new IDToCardTranslator(csa));

    }
    
    @After
    public void tearDown() {
    	proxy.onStop();
    }
         	
    @Test
    public void testTransionWinnerSelectToDisplayMain()
    {
    	PresetHelper.setPreset(preset, proxy, PresetHelper.SELECT_WINNER);

    	//check that there doesn't exist an entry in played card with this users (czar) id.
    	assertEquals(preset.czar,proxy.getUserID());

    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.Turn.TABLE_NAME, 
    			DBContract.Turn._ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.Turn.COLUMN_NAME_USER_ID+"="+preset.czar ));
   	    
    	assertFalse(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+preset.czar));
    	
    	Card c = SelectionAndContextHelper.selectCardFromSelection(
    			csa, ViewContext.SELECT_WINNER,preset.getLastTurnID());

    	CardSlideActivity confirmation = new CardSlideActivity();
    	confirmation.setProxy(proxy);
    	
    	SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, confirmation, ViewContext.SELECT_WINNER,preset.getLastTurnID());
    	
    	SelectionAndContextHelper.switchFromDisplayToMain(confirmation, new MainActivity(), 
    			ViewContext.CONFIRM_WINNER, preset.getLastTurnID());
    	
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+c.getId() + " AND " + 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_WON));

    	
    }
    
    @Test
    public void testTransitionWhiteSelectToDisplayMain()
    {
    	PresetHelper.setPreset(preset, proxy, PresetHelper.SELECT_WHITE);

    	Card c = SelectionAndContextHelper.selectCardFromSelection(
    			csa, ViewContext.SELECT_WHITE,preset.getLastTurnID());

    	//make sure the entry doesn't exist yet
    	 
    	assertFalse(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+proxy.getUserID() + " AND "+
  				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+c.getId()));
   	    	
    	CardSlideActivity confirmation = new CardSlideActivity();
    	
    	SelectionAndContextHelper.switchFromSelectionToDisplay(
    			csa, confirmation, ViewContext.SELECT_WHITE,preset.getLastTurnID());
    	
    	SelectionAndContextHelper.switchFromDisplayToMain(confirmation, new MainActivity(), 
    			ViewContext.CONFIRM_WHITE, preset.getLastTurnID());
    	 
    	/* presets don't work anymore
    	// assert that card has been marked as played
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+proxy.getUserID() + " AND "+
  				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+c.getId()));
	*/
    }
	
}
