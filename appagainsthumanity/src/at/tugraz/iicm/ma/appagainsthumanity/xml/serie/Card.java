package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Card {

   @Element
	private String text;
	
   @Attribute
   private int id;
   
   private CardType type;

   private boolean highlight = false;
   
   
   public static Card makeCard(Integer id, String text, CardType type)
   {
	   Card card = new Card(id,text,type);
	   return card;
   }
   
	/**
	 * 
	 * @param id
	 * @param text
	 * @param type
	 */
	private Card(Integer id, String text, CardType type) {
		this.text = text;
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

	public void setHighlighted(boolean b) {
		highlight = b;
	}
	
	public boolean isHighlighted() {
		return highlight;
	}

	public void setText(String string) {
		this.text = string;
	}

	
}

