package at.tugraz.iicm.ma.appagainsthumanity;


import java.util.List;

import mocks.MockDealer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.util.PromptDialog;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardSlideActivity extends FragmentActivity {
	        
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_screen_slide);

      //TODO
	  MockDealer dealer = new MockDealer(this);
	  
	  int numBlackCards = 1;
	  int numWhiteCards = 5;
	  boolean selectable = true;
	  
	  if (getIntent() != null && getIntent().getExtras() != null)
	  {		  
	      numBlackCards = getIntent().getExtras().getInt(BundleCreator.NUM_BLACK);
	      numWhiteCards = getIntent().getExtras().getInt(BundleCreator.NUM_WHITE);
	      selectable =    getIntent().getExtras().getBoolean(BundleCreator.SELECTABLE);
	  }
	  
	  List<Card> cards = null;
	  
	  if (numBlackCards == 1 && numWhiteCards > 0)
		  cards = (List<Card>) CardsInPlay.instance.getCards(
				  CardType.WHITE, numWhiteCards, dealer);
	  
	  else if (numBlackCards > 1 && numWhiteCards == 0)
		  cards = (List<Card>) CardsInPlay.instance.getCards(
				  CardType.BLACK, numBlackCards, dealer);

	  
	  if (cards == null)
		  cards = (List<Card>) CardsInPlay.instance.getCards(
				  CardType.WHITE, numWhiteCards, dealer);  
	  
      CardFragmentAdapter pageAdapter = new CardFragmentAdapter(
    		  getSupportFragmentManager(), 
    		  cards,
    		  selectable
    		  );  
      
      ViewPager pager = (ViewPager) findViewById(R.id.cs_card_slider);
	
     
      //TODO: exception in Unittests, comment back in for nice effects
      //pager.setPageTransformer(true, new ZoomOutPageTransformer());

      pager.setAdapter(pageAdapter);
          
      if (numBlackCards == 1)
      {
    	  cards = (List<Card>) CardsInPlay.instance.getCards(
				  CardType.BLACK, numBlackCards, dealer);

    		SingleCardFragment scv = SingleCardFragment.newInstance(
      				cards.get(0),35f,false);
      		
    		
      		getSupportFragmentManager()
      			.beginTransaction()
      			.add(R.id.cs_display_frame, scv)
      			.commit();
      		
      } 
      
    }      
}


	
class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.85f;
    private static float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}

