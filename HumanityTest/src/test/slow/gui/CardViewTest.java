package test.slow.gui;

import static org.junit.Assert.assertEquals;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
 
@RunWith(PathTestRunner.class)
public class CardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	Intent i = new Intent();
    	DBProxy proxy = new DBProxy(csa);
    	PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
    	csa.setProxy(proxy);
    	i.putExtras(BundleCreator.createBundle(ViewContext.SELECT_BLACK,
    			PresetHelper.man.getLastTurnID()));

    	csa.setIntent(i);

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
    
    @Test
    public void testTextViewExists()
    {    	
    	csa.onCreate(null);
    	
    	TextView label = (TextView) csa.findViewById(R.id.cs_label);
    	assertEquals(View.VISIBLE,label.getVisibility());
    	assertEquals(csa.getResources().getString(R.string.cs_label_select_black),label.getText());
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	CardFragmentAdapter cfa = (CardFragmentAdapter) pager.getAdapter();
    	assertEquals(cfa.getItem(0).getClass(),SingleCardFragment.class);
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());

    	
    }

/*    @Test //can be tested again if we put the dealt black cards in db
 * 
    public void testBundleForCzarView()
    {
    	int numBlackCards = 10;
    	System.out.println("----------");

    	DBProxy proxy = new DBProxy(csa);

    	System.out.println("----------");
    	
    	GameManager man = new GameManager();
    	man.numCards = numBlackCards;
    	
    	PresetHelper.setPreset(man, proxy, PresetHelper.SELECT_BLACK);
    	
    	csa.setProxy(proxy);
    	csa.getIntent().putExtras(
    			BundleCreator.createBundle(
    					ViewContext.SELECT_BLACK, 
    					man.getLastTurnID()));
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	System.out.println("----------");

    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numBlackCards,pager.getAdapter().getCount());
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(0,frame.getChildCount());
    }*/
    
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