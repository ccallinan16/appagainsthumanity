package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.content.Context;
import android.os.Bundle;

public class BundleCreator {

	public static String SELECTABLE = "KEY_SELECT";
	public static String NUM_BLACK = "KEY_NUM_BLACK";
	public static String NUM_WHITE = "KEY_NUM_WHITE";
	
	public static Bundle createBundle(
			boolean selectable, 
			int numBlack, 
			int numWhite)
	{
		Bundle bundle = new Bundle();
		bundle.putBoolean(SELECTABLE, selectable);
		bundle.putInt(NUM_BLACK, numBlack);
		bundle.putInt(NUM_WHITE, numWhite);
		return bundle;
	}
	
	public static Bundle getCzarView()
	{
    	int numBlackCards = 10;
    	int numWhiteCards = 0;

		return createBundle(true,numBlackCards,numWhiteCards);
	}
	
	public static Bundle getPlayerSelectionView()
	{
    	int numBlackCards = 1;
    	int numWhiteCards = 5;

		return createBundle(true,numBlackCards,numWhiteCards);
	}
	
	public static Bundle getPlayerDisplayView()
	{
    	int numBlackCards = 1;
    	int numWhiteCards = 1;

		return createBundle(false,numBlackCards,numWhiteCards);
	}

	public static Bundle getShowResultsView()
	{
    	int numBlackCards = 1;
    	int numWhiteCards = 9;

		return createBundle(false,numBlackCards,numWhiteCards);
	}
}
