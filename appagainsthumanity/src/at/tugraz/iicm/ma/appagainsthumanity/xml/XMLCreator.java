package at.tugraz.iicm.ma.appagainsthumanity.xml;
import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.AllCards;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardTypeException;


public class XMLCreator {

	public static void createXMLFromString(String fileName, String[] white, String[] black) {
		
    	Serializer serializer = new Persister();
    	AllCards wc = new AllCards();
    	try
    	{
    		if (white != null)
    		{
            	for (String string : white)
            		wc.addCard(CardType.WHITE,string);
    		}
        	
        	if (black != null)
        	{
            	for (String string : black)
            		wc.addCard(CardType.BLACK,string);
        	}

    	} catch (CardTypeException e)
    	{
    		e.printStackTrace();
    	}
    	
    	File result = new File(fileName);

		try {
			serializer.write(wc, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

}
