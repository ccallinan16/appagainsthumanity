package at.tugraz.iicm.ma.appagainsthumanity.adapter;

public enum ViewContext {
	SELECT_WHITE, SELECT_BLACK, CONFIRM_SINGLE, CONFIRM_PAIR, SHOW_RESULT, UNKNOWN;
	
	public static ViewContext getContextFromString(String str)
	{
		if (str.equals(SELECT_WHITE.toString()))
			return SELECT_WHITE;
		if (str.equals(SELECT_BLACK.toString()))
			return SELECT_BLACK;
		if (str.equals(SHOW_RESULT.toString()))
			return SHOW_RESULT;
		if (str.equals(CONFIRM_PAIR.toString()))
			return CONFIRM_PAIR;
		if (str.equals(CONFIRM_SINGLE.toString()))
			return CONFIRM_SINGLE;
		
		
		return UNKNOWN;
	}
	
}
