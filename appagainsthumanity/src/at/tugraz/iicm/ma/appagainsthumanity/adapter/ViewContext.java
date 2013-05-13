package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public enum ViewContext {
	SELECT_WHITE, SELECT_BLACK, SELECT_WINNER, CONFIRM_BLACK, CONFIRM_WHITE, CONFIRM_WINNER, SHOW_RESULT, UNKNOWN ;
	
	public static ViewContext getContextFromString(String str)
	{
		if (str.equals(SELECT_WHITE.toString()))
			return SELECT_WHITE;
		if (str.equals(SELECT_BLACK.toString()))
			return SELECT_BLACK;
		if (str.equals(SHOW_RESULT.toString()))
			return SHOW_RESULT;
		if (str.equals(CONFIRM_WHITE.toString()))
			return CONFIRM_WHITE;
		if (str.equals(CONFIRM_BLACK.toString()))
			return CONFIRM_BLACK;
		if (str.equals(SELECT_WINNER.toString()))
			return SELECT_WINNER;
		if (str.equals(CONFIRM_WINNER.toString()))
			return CONFIRM_WINNER;		
		
		return UNKNOWN;
	}

	public CardType getCardType() {

		if (this.equals(SELECT_BLACK) || this.equals(CONFIRM_BLACK))
			return CardType.BLACK;
		else
			return CardType.WHITE;
		
	}
	
}
