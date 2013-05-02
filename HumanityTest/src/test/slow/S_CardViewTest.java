package test.slow;

import static org.junit.Assert.assertEquals;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_CardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
     
    @Test
    public void testBundleForCzar()
    {
    	int numBlackCards = 10;
    	int numWhiteCards = 0;
    	
    	Bundle bundle = new Bundle();
    	bundle.putBoolean(
    			csa.getResources().getString(R.string.key_selectable), true);
    	bundle.putInt(
    			csa.getResources().getString(R.string.key_num_black), numBlackCards);
    	bundle.putInt(
    			csa.getResources().getString(R.string.key_num_white), numWhiteCards);
    	
    	csa.onCreate(bundle);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numBlackCards,pager.getAdapter().getCount());
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }
    
    
    @Test
    public void testChooseRandomCards()
    {

    	int numCards = 9;
    	MockDealer dealer = new MockDealer(csa);
            	
    	csa.onCreate(null);
    	csa.pageAdapter.setFragments(
    			SingleCardView.getFragmentFromCards(
    					dealer.dealCards(CardType.WHITE, numCards), 30f
    					)
    			);
    	
    	assertEquals(numCards,csa.pageAdapter.getCount());
    }


}