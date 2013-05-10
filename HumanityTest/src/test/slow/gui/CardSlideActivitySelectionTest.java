package test.slow.gui;

import static org.junit.Assert.assertEquals;
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
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class CardSlideActivitySelectionTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);

    	CardCollection.instance.setTranslator(new IDToCardTranslator(csa));
    	
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
   
    
    @Test
    public void testSelectionRememberedBlack()
    {
    	PresetHelper.setPreset(new DBProxy(csa), PresetHelper.SELECT_BLACK);
    	    	
    	csa.getIntent().putExtras(BundleCreator.createBundle(ViewContext.SELECT_BLACK,
    			PresetHelper.man.getLastTurnID()));
    	csa.onCreate(null);
    	
    	CardType type = CardType.BLACK;
    	
    	Card c = SelectionAndContextHelper.getFirstCard(csa, type);
    	SelectionAndContextHelper.selectCardAndPerformClick(csa,c);

    	CardSlideActivity newActivity = SelectionAndContextHelper.createNewCSActivity(ViewContext.CONFIRM_SINGLE);
    	Card selectedCard = SelectionAndContextHelper.getFirstCard(newActivity, type);

    	assertTrue(c != null);
    	assertTrue(selectedCard != null);
    	
    	assertEquals(c.getId(),selectedCard.getId());
    }
    
    @Test
    public void testSelectionRememberedWhite()
    {
    	DBProxy proxy = new DBProxy(csa);
    	PresetHelper.setPreset(proxy, PresetHelper.SELECT_WHITE);
    	
    	csa.getIntent().putExtras(BundleCreator.createBundle(
    			ViewContext.SELECT_WHITE, 
    			PresetHelper.man.getLastTurnID()));
    	    	
    	csa.setProxy(proxy);
    	csa.onCreate(null);
    	    	
    	CardType type = CardType.WHITE;
    	
    	Card c = SelectionAndContextHelper.getFirstCard(csa, type);
    	SelectionAndContextHelper.selectCardAndPerformClick(csa,c);
    	
    	CardSlideActivity activity = SelectionAndContextHelper.createNewCSActivity(
    			ViewContext.CONFIRM_PAIR);
    	Card selected = SelectionAndContextHelper.getFirstCard(activity, CardType.WHITE);
    	
    	assertTrue(c != null);
    	assertTrue(selected != null);
    	
    	assertEquals(c.getId(),selected.getId());
    }
    
}