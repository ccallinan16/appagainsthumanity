package test.fast;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mocks.Shuffler;
import mocks.IDToCardTranslator;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
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
		Card card = Card.makeCard(1,"Hello World",CardType.WHITE);
		Card card2 = Card.makeCard(5,"Good Day.",CardType.BLACK);

		assertEquals("(1) white \"Hello World\"",card.toString());
		assertEquals("(5) black \"Good Day.\"",card2.toString());

	}
	
    @Test
    public void testMockDealerTop()
    {
    	IDToCardTranslator dealer = new IDToCardTranslator("testdata/xml/raw/all_cards.xml");
    	int numCards = 7;
    	dealer.setNumWhiteCards(numCards);
    	
    	List<Card> cards = dealer.dealCards(ViewContext.SELECT_WHITE);
    	assertTrue(cards.size()==numCards);
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
   	
    	int numCards = 4;
    	List<Integer> cardNumbers = Shuffler.getRandomListOfInts(numCards);
    	assertEquals(numCards,cardNumbers.size());
    }
    
}
