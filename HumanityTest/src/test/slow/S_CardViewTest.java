package test.slow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mocks.MockDB;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_CardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
     
    @Test
    public void testBundleForCardSlider()
    {
    	Bundle bundle = new Bundle();
    	bundle.putBoolean("SELECTABLE", true);
    	bundle.putBoolean("TOP_SINGLE", true);
    	bundle.putBoolean("BOTTOM_SINGLE",false);
    	
    	csa.onCreate(bundle);
    	    	
    }
    
    
    @Test
    public void testChooseRandomCards()
    {

    	int numCards = 9;
    	MockDealer dealer = new MockDealer(numCards,csa);
            	
    	csa.onCreate(null);
    	csa.pageAdapter.setFragments(
    			SingleCardView.getFragmentFromCards(
    					dealer.dealCards(CardType.WHITE, numCards), 30f
    					)
    			);
    	
    	assertEquals(numCards,csa.pageAdapter.getCount());
    }


}