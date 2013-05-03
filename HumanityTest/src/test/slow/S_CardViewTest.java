package test.slow;

import static org.junit.Assert.assertEquals;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SelectableCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_CardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	csa.setIntent(i);

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
     
    @Test
    public void testBundleForCzarView()
    {
    	int numBlackCards = 10;
    	int numWhiteCards = 0;
    	
    	Bundle b = BundleCreator.createBundle(true,numBlackCards,numWhiteCards);

    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numBlackCards,pager.getAdapter().getCount());
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }
    
    @Test
    public void testCreateSwipableBlackList()
    {
   	
    	Bundle b = BundleCreator.getCzarView();

    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	CardFragmentAdapter cfa = (CardFragmentAdapter) pager.getAdapter();
    	assertEquals(cfa.getItem(0).getClass(),SelectableCardFragment.class);
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }
    
    @Test
    public void testBundleForCzarViewWithPresets()
    {
    	int numBlackCards = 10;
    	int numWhiteCards = 0;
    	
    	Bundle b = BundleCreator.getCzarView();
    	
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);

    	//we need a view for the cardSlider, so ViewPage? 
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numBlackCards,pager.getAdapter().getCount());
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }
    
    @Test
    public void testBundleForPlayerChooser()
    {
    	int numBlackCards = 1;
    	int numWhiteCards = 7;
    	
    	Bundle b = BundleCreator.createBundle(true,numBlackCards,numWhiteCards);
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(1,frame.getChildCount());

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numWhiteCards,pager.getAdapter().getCount());
    }
    
    @Test
    public void testBundleForPlayerDisplay()
    {
    	int numBlackCards = 1;
    	int numWhiteCards = 1;
    	
    	Bundle b = BundleCreator.createBundle(false,numBlackCards,numWhiteCards);
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(1,frame.getChildCount());

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numWhiteCards,pager.getAdapter().getCount());
    }
    
    @Test
    public void testBundleForShowResults()
    {
    	int numBlackCards = 1;
    	int numWhiteCards = 10;
    	
    	Bundle b = BundleCreator.createBundle(false,numBlackCards,numWhiteCards);
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(1,frame.getChildCount());

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numWhiteCards,pager.getAdapter().getCount());
    }
    
    
    @Test
    public void testChooseRandomCards()
    {

    	int numCards = 9;
    	MockDealer dealer = new MockDealer(csa);
            	
    	csa.onCreate(null);
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	((CardFragmentAdapter) pager.getAdapter()).setCards(dealer.dealCards(CardType.WHITE, numCards));

    	
    	assertEquals(numCards,pager.getAdapter().getCount());
    }


}