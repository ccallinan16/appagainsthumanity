package at.tugraz.iicm.ma.appagainsthumanity.xml.serie;

import android.graphics.Color;

public enum CardType {
	WHITE, BLACK;

	@Override
	public String toString() {
		
		if (this.equals(WHITE))
			return "white";
		if (this.equals(BLACK))
			return "black";
		return super.toString();
	}
	
	public int getTextColor()
	{
		if (this.equals(WHITE))
			return Color.BLACK;
		if (this.equals(BLACK))
			return Color.WHITE;
		return Color.BLACK;
	}
	
	public int getBGColor()
	{
		if (this.equals(WHITE))
			return Color.WHITE;
		if (this.equals(BLACK))
			return Color.BLACK;
		return Color.WHITE;
	}

}
