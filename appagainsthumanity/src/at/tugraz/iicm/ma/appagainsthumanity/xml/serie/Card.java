package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Card {

   @Element
	private String text;
	
   @Attribute
   private int id;
   
	public Card(String string, int i) {
		this.text = string;
		this.id = i;
	}
  
}

