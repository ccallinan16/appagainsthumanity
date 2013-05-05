package test.slow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import test.util.SelectionAndContextHelper;
import test.util.TestBundleCreator;
import android.content.Intent;
import android.os.Bundle;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
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

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
   
    
    @Test
    public void testSelectionRememberedBlack()
    {
    	Bundle b = TestBundleCreator.getSelectBlackBundle();
    	
    	csa.getIntent().putExtras(b);
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
    	Bundle b = TestBundleCreator.getSelectWhiteBundle();

    	Card black = CardCollection.instance.makeCard(1, "hello", CardType.BLACK);
    	CardCollection.instance.setBlackCard(black);

    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	    	
    	assertEquals(black.getId(),SelectionAndContextHelper.getTopCard(csa).getId());
    	
    	CardType type = CardType.WHITE;
    	
    	Card c = SelectionAndContextHelper.getFirstCard(csa, type);
    	SelectionAndContextHelper.selectCardAndPerformClick(csa,c);
    	
    	CardSlideActivity activity = SelectionAndContextHelper.createNewCSActivity(ViewContext.CONFIRM_PAIR);
    	Card selected = SelectionAndContextHelper.getFirstCard(activity, CardType.WHITE);
    	
    	assertTrue(c != null);
    	assertTrue(selected != null);
    	
    	assertEquals(c.getId(),selected.getId());
    }
    
}