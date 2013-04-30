package first;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;

import at.tugraz.iicm.ma.appagainsthumanity.xml.CreateCardXML;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class XMLTests {

	String subPath;
	
	@Before
	public void setUp() throws Exception {
		subPath = "testdata/xml/";		
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
	
	@Test
	public void testReadFileIntoStringArray()
	{
		String[] array = CreateCardXML.readArrayFromFile(subPath+"example.xml");
		
		assertEquals(array[0],"<allCards>");
		assertTrue(array[1].contains("<whitecards>"));
	}
	
}
