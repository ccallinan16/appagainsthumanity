package at.tugraz.iicm.ma.appagainsthumanity.xml;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;


public class XMLReader {

	 
	  private Document xmlDocument;
	  private XPath xPath;
	 
	  public XMLReader(String xmlFile) {
	    initObjects(null,xmlFile);
	  }
	 
	  public XMLReader(Context context)   {
		  initObjects(context, null);
	  }

	private void initObjects(Context context, String xmlFile){
	      try {
	    	  
	    	  if (context == null && xmlFile != null)
	    	  {
	    		  this.xmlDocument = DocumentBuilderFactory.
		        	        newInstance().newDocumentBuilder().
		        	        parse(xmlFile);
	    	  }
	    	  else if (context != null && xmlFile == null)
	    	  {
	    		  InputStream is = context.getResources().openRawResource(R.raw.all_cards);
	    		  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    		  DocumentBuilder builder = factory.newDocumentBuilder();
	    		  this.xmlDocument = builder.parse(is);
	    	  }
	    	  else
	    		  throw new Exception("not enough information!");

	        xPath =  XPathFactory.newInstance().newXPath();
	      } catch (IOException ex) {
	        ex.printStackTrace();
	      } catch (SAXException ex) {
	        ex.printStackTrace();
	      } catch (ParserConfigurationException ex) {
	        ex.printStackTrace();
	      } catch (Exception e) {
			e.printStackTrace();
		}
	  }

	/**
	 * 
	 * @param type
	 * @param id: starts with 0
	 * @return
	 */
	public String getText(CardType type, int id) {
				
		try {
			//as indizes here start with 1, well add 1
			String expr = "/allCards/"+type.toString()+"cards/card["+(id+1)+"]/text";
			
		      XPathExpression xPathExpression = xPath.compile(expr);
		      if (xPathExpression == null)
		    	  return null;
		     String str = (String) xPathExpression.evaluate(xmlDocument, XPathConstants.STRING);
		     if (("").equals(str.trim()))
		    	 return null;
		     return str;
		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	

}
