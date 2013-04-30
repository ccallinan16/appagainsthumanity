package at.tugraz.iicm.ma.appagainsthumanity.xml;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;


public class XMLReader {

	 
	  private String xmlFile;
	  private Document xmlDocument;
	  private XPath xPath;
	 
	  public XMLReader(String xmlFile) {
	    this.xmlFile = xmlFile;
	    initObjects();
	  }
	 
	  private void initObjects(){
	      try {
	        xmlDocument = DocumentBuilderFactory.
	        newInstance().newDocumentBuilder().
	        parse(xmlFile);
	        xPath =  XPathFactory.newInstance().
	        newXPath();
	      } catch (IOException ex) {
	        ex.printStackTrace();
	      } catch (SAXException ex) {
	        ex.printStackTrace();
	      } catch (ParserConfigurationException ex) {
	        ex.printStackTrace();
	      }
	  }

	
	public String getText(CardType type, int id) {
				
		try {
		      XPathExpression xPathExpression = xPath.compile("/allCards/"+type.toString()+"cards/card["+id+"]/text");
		     String str = (String) xPathExpression.evaluate(xmlDocument, XPathConstants.STRING);
		     if ((str.trim().equals("")))
		    	 return null;
		     return str;
		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
