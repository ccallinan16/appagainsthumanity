package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Card {

   @Element
	private String text;
	
   @Attribute
   private int id;
   
   private CardType type;
   
   /**
    * 
    * @param string
    * @param i
    */
	public Card(String string, int i) {
		this.text = string;
		this.id = i;
	}
  
	/**
	 * 
	 * @param text2
	 */
	public Card(String text2) {
		this.text = text2;
		this.id = 0;
	}

	/**
	 * 
	 * @param text
	 * @param id
	 * @param type
	 */
	public Card(String text, Integer id, CardType type) {
		this.text = text;
		this.id = id;
		this.type = type;
	}

	public Card(Integer id, CardType type) {
		this.id = id;
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	
	public int getId() {
		return id;
	}
	
	public CardType getType() {
		return type;
	}

	public int getBGColor() {
		return type.getBGColor();
	}
	
	@Override
		public String toString() {
		return "(" + id + ") " +type + " \"" + text + "\""; 
	}
	
}

