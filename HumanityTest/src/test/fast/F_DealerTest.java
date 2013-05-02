package test.fast;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import mocks.MockDB;
import mocks.MockDealer;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.xml.CreateCardXML;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class F_DealerTest {

	String subPath;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCardToString()
	{
		Card card = new Card("Hello World",1,CardType.WHITE);
		Card card2 = new Card("Good Day.",5,CardType.BLACK);

		assertEquals("(1) white \"Hello World\"",card.toString());
		assertEquals("(5) black \"Good Day.\"",card2.toString());

	}
	
    @Test
    public void testMockDealerTop()
    {
    	MockDealer dealer = new MockDealer(5, "testdata/xml/allCards.xml");
    	    	
    	List<Card> cards = dealer.dealCards(CardType.WHITE,5);
    	assertTrue(cards.size()==5);
    	for (Card c : cards)
    	{
    		System.out.println(c);
        	assertEquals(CardType.WHITE,c.getType());
        	assertTrue(c.getText()!="");
        	assertTrue(c.getText()!="couldn't read from xml");
    	}
    }
    
    @Test
    public void testGettingNumsFromDB()
    {
    	String[] cards = {"black card text 01 ____.",
    			"some other text. ____ .", 
    			"and a third one, just to be safe.",
    			"some 02 text. ____ .", 
    			"some 03 text. ____ .", 
    			"some 04 text. ____ .", };

    	MockDB db = new MockDB(cards.length);
    	
    	int numCards = 4;
    	List<Integer> cardNumbers = db.assignCards(numCards);
    	assertEquals(numCards,cardNumbers.size());
    	for (Integer num : cardNumbers)
    	{
    		assert(num < cards.length);
    	}
    }
    
}
