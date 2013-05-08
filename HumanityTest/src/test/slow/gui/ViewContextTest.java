package test.slow.gui;

import static org.junit.Assert.assertEquals;

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
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class ViewContextTest {
 
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
     
    private void testContextView(ViewContext context, int overwrite)
    {
    	Bundle b = TestBundleCreator.createBundleOverwrite(context,overwrite);

    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);
    	
    	//we need a view for the cardSlider, so ViewPage? 
    	
    	assertEquals(context,csa.getViewContext());
    	
    	int numsInFrame = 1;
    	int numsInSwitcher = overwrite;

    	switch (context)
    	{
    	case SELECT_BLACK:
    	case CONFIRM_SINGLE:
    		numsInFrame = 0;
    		default:
    			
    	}
    	
    	assertNumsInPager(csa, numsInSwitcher);
    	assertNumsinTopFrame(csa, numsInFrame);

    }
       
    public static void assertNumsInPager(CardSlideActivity activity, int numsInSwitcher)
    {
    	ViewPager pager = (ViewPager) activity.findViewById(R.id.cs_card_slider);
    	assertEquals(numsInSwitcher,pager.getAdapter().getCount());
    }
    
    public static void assertNumsinTopFrame(CardSlideActivity activity, int numsInFrame)
    {
    	FrameLayout frame = (FrameLayout) activity.findViewById(R.id.cs_display_frame);
    	assertEquals(numsInFrame,frame.getChildCount());

    }
    
    
    @Test
    public void testBundleForCzarView()
    {
    	int numBlackCards = 11;
    	
    	testContextView(ViewContext.SELECT_BLACK, numBlackCards);
    }
    
    
    @Test
    public void testBundleForCzarViewWithPresets()
    {
    	int numBlackCards = 10;
    	int numWhiteCards = 0;
    	
    	Bundle b = TestBundleCreator.getSelectBlackTESTING(numBlackCards);
    	
    	csa.getIntent().putExtras(b);
    	csa.onCreate(null);

    	//we need a view for the cardSlider, so ViewPage? 
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	assertEquals(numBlackCards,pager.getAdapter().getCount());
    	
    	FrameLayout frame = (FrameLayout) csa.findViewById(R.id.cs_display_frame);
    	assertEquals(numWhiteCards,frame.getChildCount());
    }
    
    @Test
    public void testBundleForPlayerChooser()
    {
    	int numWhiteCards = 7;
    	   	
    	Card c = CardCollection.instance.makeCard(10, "hello", CardType.BLACK);
    	CardCollection.instance.setBlackCard(c.getId());
    	testContextView(ViewContext.SELECT_WHITE, numWhiteCards);
    }
    
    @Test
    public void testBundleForPlayerDisplay()
    {
    	Card white = CardCollection.instance.makeCard(1,"Hello",CardType.WHITE);
    	Card black = CardCollection.instance.makeCard(5,"black",CardType.BLACK);

    	CardCollection.instance.setBlackCard(black.getId());
    	CardCollection.instance.setSelected(white);
    	testContextView(ViewContext.CONFIRM_PAIR, 1);
    }
    
    @Test
    public void testBundleForShowResults()
    {
    	int numWhiteCards = 10;
    	
    	testContextView(ViewContext.SHOW_RESULT, numWhiteCards);
    }
    
}