package test.slow.gui;

import static org.junit.Assert.assertEquals;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import test.util.TestBundleCreator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class CardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	PresetHelper.setPreset(new DBProxy(csa), PresetHelper.SELECT_BLACK);
    	
    	i.putExtras(BundleCreator.createBundle(ViewContext.SELECT_BLACK,
    			PresetHelper.man.getLastTurnID()));

    	csa.setIntent(i);

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }

    @Test
    public void testBundleForCzarView()
    {
    	int numBlackCards = 10;
    	
    	Bundle b = TestBundleCreator.getSelectBlackTESTING(numBlackCards);
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
    	//Preset SELECT_BLACK already selected
    	csa.onCreate(null);

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	CardFragmentAdapter cfa = (CardFragmentAdapter) pager.getAdapter();
    	assertEquals(cfa.getItem(0).getClass(),SingleCardFragment.class);
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }
    
    
    
    @Test
    public void testBundleForCzarViewWithPresets()
    {
    	int numBlackCards = 10;
    	
    	Bundle b = TestBundleCreator.getSelectBlackTESTING(numBlackCards);
    	
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);

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
    	dealer.setNumWhiteCards(numCards);
            	
    	csa.onCreate(null);
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	((CardFragmentAdapter) pager.getAdapter()).setCards(dealer.dealCards(ViewContext.SELECT_WHITE));

    	
    	assertEquals(numCards,pager.getAdapter().getCount());
    }


}