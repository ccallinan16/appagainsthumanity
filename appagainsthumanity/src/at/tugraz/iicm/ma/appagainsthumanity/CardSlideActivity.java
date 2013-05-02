package at.tugraz.iicm.ma.appagainsthumanity;


import mocks.MockDealer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardSlideActivity extends FragmentActivity {
	
    public CardFragmentAdapter pageAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
	  MockDealer dealer = new MockDealer(5,this);

      setContentView(R.layout.activity_screen_slide);
      pageAdapter = new CardFragmentAdapter(getSupportFragmentManager(), 
    		  SingleCardView.getFragmentFromCards(
    				  dealer.dealCards(CardType.WHITE, 5), 50f)
    		  );  
      
      ViewPager pager = (ViewPager)findViewById(R.id.viewpager_white_cards);
      
      //TODO: exception in Unittests, comment back in for nice effects
      //pager.setPageTransformer(true, new ZoomOutPageTransformer());

      pager.setAdapter(pageAdapter);
          
  		SingleCardView scv = SingleCardView.newInstance(
  				new Card("This is ________ our black card.",0,CardType.BLACK),30f);
  		
  		getSupportFragmentManager()
  			.beginTransaction()
  			.add(R.id.frame_black_card, scv)
  			.commit();
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

