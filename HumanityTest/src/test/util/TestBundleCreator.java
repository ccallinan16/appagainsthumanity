package test.util;

import mocks.MockDealer;
import android.os.Bundle;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class TestBundleCreator {

	static MockDealer dealer = new MockDealer("testdata/xml/raw/all_cards.xml");

	public static Bundle getSelectBlackTESTING(int numBlackCards) {
		
		//do what needs to be done to make assertions not fail
	/*	AllCards.instance.clearAll();
		AllCards.instance.fillWithCards(CardType.BLACK,numBlackCards,dealer);
		*/
		
		dealer.setNumBlack(numBlackCards);
		CardCollection.instance.setupContextTESTING(ViewContext.SELECT_BLACK, dealer);

		return BundleCreator.getSelectBlack();
	}

	public static Bundle getSelectWhiteTESTING(int numWhiteCards)
	{
		dealer.setNumWhiteCards(numWhiteCards);
		CardCollection.instance.setupContextTESTING(ViewContext.SELECT_WHITE, dealer);

		return BundleCreator.getSelectWhite();
	}
	
	public static Bundle getSelectBlackBundle()
	{
		return BundleCreator.getSelectBlack();
	}
	
	public static Bundle getSelectWhiteBundle()
	{
		CardCollection.instance.setupContextTESTING(ViewContext.SELECT_WHITE, dealer);
		return BundleCreator.getSelectWhite();
	}

	public static Bundle createBundleOverwrite(ViewContext context, int overwriteNum) {
	
		CardCollection.instance.setupContextTESTING(context, dealer);

		switch(context)
		{
		case SELECT_BLACK:
			dealer.setNumBlack(overwriteNum);
		case SELECT_WHITE:
			dealer.setNumWhiteCards(overwriteNum);
		case SHOW_RESULT:
			dealer.setNumPlayers(overwriteNum);
		default:
			//do nothing
		}
		
		Bundle bundle = new Bundle();
		bundle.putString(BundleCreator.CONTEXT, context.toString());

		return bundle;
	}

	public static Bundle createBundle(ViewContext context, boolean createTestingCards) {

		if (createTestingCards)
			CardCollection.instance.setupContextTESTING(context, dealer);

		Bundle bundle = new Bundle();
		bundle.putString(BundleCreator.CONTEXT, context.toString());
		
		return bundle;
	}

	

	
}
