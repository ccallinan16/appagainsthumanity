package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract.BlackCard;

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

	public float getRelativeTextSize(float max, float min)
	{
		int length = text.length();
		
		float initialSize = max;
		
		while (length > 20 && initialSize > min)
		{
			initialSize--;
			length -= 6;
		}
		
		
		//ok for white
		/*while (length > 20 && initialSize > min)
		{
			initialSize--;
			length -= 6;
		}*/
		
		return initialSize;
	}
	
	public int getTextAppearance() {
		if (type.equals(CardType.BLACK))
			return R.style.text_white_large;
		else
			return R.style.text_black_large;
	}

	
}

