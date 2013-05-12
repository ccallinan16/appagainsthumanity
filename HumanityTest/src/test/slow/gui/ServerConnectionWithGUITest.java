package test.slow.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mocks.IDToCardTranslator;

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
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

@RunWith(PathTestRunner.class)
public class ServerConnectionWithGUITest {

	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);
    	CardCollection.instance.setTranslator(new IDToCardTranslator(csa));

    }
 	/*
    @Test
    public void testTransitionWhiteSelectToDisplay()
    {
    	GameManager preset = new GameManager();
    	CardSlideActivity activity = new CardSlideActivity();
    	DBProxy proxy = new DBProxy(activity);

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
    			ViewContext.CONFIRM_PAIR, preset.getLastTurnID());
    	 
    	
    	// assert that card has been marked as played
    	assertTrue(proxy.getter.checkEntryExistsWhere(DBContract.PlayedWhiteCard.TABLE_NAME, 
    			DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID+"="+preset.getLastTurnID()+" AND "+
    		    DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID+"="+proxy.getUserID() + " AND "+
  				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID+"="+c.getId()));

    }
*/
	
}
