package test.slow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import mocks.IDToCardTranslator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class DealerXMLTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
         
       
    @Test
    public void testWithCardsInPlaySingle()
    {
    	String message = "black card text____.";

    	Card c = Card.makeCard(1,message,CardType.BLACK);
    	    	
    	CardCollection.instance.addCard(c);
    	assertEquals(c.getText(),CardCollection.instance.getCardSafe(1,CardType.BLACK).getText());
    	assertEquals(1,CardCollection.instance.getCardSafe(1,CardType.BLACK).getId());
    }

    
	@Test
	public void testMockDBResourceXML() {
			
    	Intent i = new Intent();
    	i.putExtras(BundleCreator.createBundle(
    			ViewContext.SELECT_BLACK, 0));
    	csa.setIntent(i);
    	csa.onCreate(null);
		
    	int numCards = 10;
    	IDToCardTranslator dealer = new IDToCardTranslator(csa);
    	dealer.setNumWhiteCards(numCards);
        
    	List<Card> cards = dealer.dealCards(ViewContext.SELECT_WHITE);
    	assertTrue(cards.size()==numCards);
    	for (Card c : cards)
    	{
        	assertEquals(CardType.WHITE,c.getType());
        	assertTrue(c.getText()!="");
        	assertTrue(c.getText()!="couldn't read from xml");
    	}
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	
    	((CardFragmentAdapter) pager.getAdapter()).setCards(cards);
    	
    	assertEquals(numCards,pager.getAdapter().getCount());
	}

    
	@Test
	public void testQueryOnResourceXML() {
		
		int id = 34;
		
		XMLReader reader = new XMLReader(csa);
		try {
			String str = reader.getText(CardType.WHITE,id);
			assertTrue(str != null);
		} catch (Exception e)
		{
			fail();
		}
	}


}