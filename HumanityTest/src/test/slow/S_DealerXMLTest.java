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
public class S_DealerXMLTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	csa.onCreate(null);
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
         
	@Test
	public void testMockDBResourceXML() {
			
    	int numCards = 10;
    	MockDealer dealer = new MockDealer(csa);
        
    	List<Card> cards = dealer.dealCards(CardType.WHITE,numCards);
    	assertTrue(cards.size()==numCards);
    	for (Card c : cards)
    	{
    		System.out.println(c);
        	assertEquals(CardType.WHITE,c.getType());
        	assertTrue(c.getText()!="");
        	assertTrue(c.getText()!="couldn't read from xml");
    	}
    	
    	csa.pageAdapter.setFragments(SingleCardView.getFragmentFromCards(cards,50f));
    	
    	assertEquals(numCards,csa.pageAdapter.getCount());
	}

    
	@Test
	public void testQueryOnResourceXML() {
		
		int id = 34;
		
		XMLReader reader = new XMLReader(csa);
		try {
			System.out.println(reader.getText(CardType.WHITE,id));
		} catch (Exception e)
		{
			fail();
		}
	}


}