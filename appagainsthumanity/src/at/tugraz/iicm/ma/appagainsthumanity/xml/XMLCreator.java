package at.tugraz.iicm.ma.appagainsthumanity.xml;
import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardTypeException;


public class XMLCreator {

	public static void createXMLFromString(String fileName, String[] white, String[] black) {
		
    	Serializer serializer = new Persister();
    	CardCollection cc = CardCollection.instance;
    	cc.clearAll();
    	try
    	{
    		if (white != null)
    		{
            	for (String string : white)
            		cc.makeAndAddCardFromText(CardType.WHITE,string);
    		}
        	
        	if (black != null)
        	{
            	for (String string : black)
            		cc.makeAndAddCardFromText(CardType.BLACK,string);
        	}

    	} catch (CardTypeException e)
    	{
    		e.printStackTrace();
    	}
    	
    	File result = new File(fileName);

		try {
			serializer.write(cc, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}

}
