package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.os.Bundle;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class BundleCreator {

	public static final String CONTEXT = "KEY_CONTEXT";
	public static final String SELECTABLE = "KEY_SELECT";
	public static final String NUM_BLACK = "KEY_NUM_BLACK";
	public static final String NUM_WHITE = "KEY_NUM_WHITE";
		
	private static Bundle createBundle(ViewContext context) {

		Bundle bundle = new Bundle();
		bundle.putString(CONTEXT, context.toString());
		return bundle;
	}
	

	public static Bundle getSelectBlack()
	{
		//assert(CardCollection.instance.getCardCount(CardType.WHITE) == 0);
		//assert(CardCollection.instance.getCardCount(CardType.BLACK) == 0);

		return createBundle(ViewContext.SELECT_BLACK);
	}
	
	public static Bundle getConfirmBlack()
	{
		//assert(CardCollection.instance.getCardCount(CardType.WHITE) == 0);
		assert(CardCollection.instance.getCardCount(CardType.BLACK) > 0);
		assert(CardCollection.instance.getSelectedCard(CardType.BLACK) != null);
		
		return createBundle(ViewContext.CONFIRM_SINGLE);
	}
	
	public static Bundle getSelectWhite()
	{
/*
		assert(CardCollection.instance.getBlackCard() != null);
		assert(CardCollection.instance.getCardCount(CardType.BLACK) > 0);
*/		//assert(CardCollection.instance.getCardCount(CardType.WHITE) == 0);
		
		return createBundle(ViewContext.SELECT_WHITE);
	}
	
	public static Bundle getConfirmWhite()
	{
		assert(CardCollection.instance.getBlackCard() != null);
		assert(CardCollection.instance.getSelectedCard(CardType.WHITE) != null);
		assert(CardCollection.instance.getCardCount(CardType.BLACK) > 0);
		assert(CardCollection.instance.getCardCount(CardType.WHITE) > 0);
		
		return createBundle(ViewContext.CONFIRM_PAIR);
	}

	public static Bundle getShowResults()
	{
		assert(CardCollection.instance.getBlackCard() != null);
		assert(CardCollection.instance.getCardCount(CardType.WHITE) > 0);
		assert(CardCollection.instance.getCardCount(CardType.BLACK) > 0);
		//TODO: there should also be a "selected" card - the winner.
		
		return createBundle(ViewContext.SHOW_RESULT);
	}


}
