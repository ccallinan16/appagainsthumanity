package test.fast;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mocks.MockDB;
import mocks.MockDealer;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
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
		Card card = CardCollection.instance.makeCard(1,"Hello World",CardType.WHITE);
		Card card2 = CardCollection.instance.makeCard(5,"Good Day.",CardType.BLACK);

		assertEquals("(1) white \"Hello World\"",card.toString());
		assertEquals("(5) black \"Good Day.\"",card2.toString());

	}
	
    @Test
    public void testMockDealerTop()
    {
    	MockDealer dealer = new MockDealer("testdata/xml/raw/all_cards.xml");
    	    	
    	List<Card> cards = dealer.dealCards(CardType.WHITE,5);
    	assertTrue(cards.size()==5);
    	for (Card c : cards)
    	{
        	assertEquals(CardType.WHITE,c.getType());
        	assertTrue(c.getText()!="");
        	assertTrue(c.getText()!="couldn't read from xml");
    	}
    }
    
    @Test
    public void testGettingNumsFromDB()
    {
    	MockDB db = new MockDB();
    	
    	int numCards = 4;
    	List<Integer> cardNumbers = db.assignCards(numCards);
    	assertEquals(numCards,cardNumbers.size());
    }
    
}
