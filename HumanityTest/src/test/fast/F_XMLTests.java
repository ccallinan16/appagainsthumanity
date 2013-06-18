package test.fast;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import at.tugraz.iicm.ma.appagainsthumanity.xml.CreateCardXML;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class F_XMLTests {

	String subPath;
	
	@Before
	public void setUp() throws Exception {
		subPath = "testdata/xml/";		
	}
	
	@Test
	public void createXMLDocument() {
		String[] black = CreateCardXML.readArrayFromFile("testdata/xml/cards_black.txt");
		String[] white = CreateCardXML.readArrayFromFile("testdata/xml/cards_white.txt");
		
		XMLCreator.createXMLFromString("testdata/xml/raw/all_cards.xml", white, black);	
	}

	@Test
	public void testQueryOnAllCards() {
		
		int id = 34;
		XMLReader reader = new XMLReader(subPath+"raw/all_cards.xml");
		try {
			reader.getText(CardType.WHITE,id);
		} catch (Exception e)
		{
			fail();
		}
	}

	
	@Test
	public void testQuery() {
		
		int id = 1;
		XMLReader reader = new XMLReader(subPath+"example.xml");
		assertEquals("Hello",reader.getText(CardType.WHITE,id));
	}
		
	
	@Test
	public void testQueryOutOfBounds() {
		
		int id = 4;
		XMLReader reader = new XMLReader(subPath+"example.xml");
		assertEquals(null,reader.getText(CardType.WHITE,id));		
	}
	
	@Test
	public void testReadFileIntoStringArray()
	{
		String[] array = CreateCardXML.readArrayFromFile(subPath+"example.xml");
		
		assertEquals(array[0],"<cardCollection>");
		assertTrue(array[1].contains("<white>"));
	}
	
	
	@Test
	public void testCreateFromString() {
		
		String FILENAME = subPath+"example2.xml";
		String[] array = {"Text01","Text02","Text03"};

        XMLCreator.createXMLFromString(FILENAME,array,null);
		
		XMLReader reader = new XMLReader(FILENAME);
		assertEquals(array[2],reader.getText(CardType.WHITE,3));
	}
	
	@Test
	public void testCreateBlackAndWhitesFromString() {
		
		String FILENAME = subPath+"example3.xml";
		String[] white = {"Text01","Text02","Text03"};
		String[] black = {"BlackText","BlackText2","BT03"};

        XMLCreator.createXMLFromString(FILENAME,white,black);
		
		XMLReader reader = new XMLReader(FILENAME);
		assertEquals(white[2],reader.getText(CardType.WHITE,3));
		assertEquals(black[1],reader.getText(CardType.BLACK,2));		
	}
	

	
}
