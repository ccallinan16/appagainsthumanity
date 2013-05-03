package test.slow;

import static org.junit.Assert.assertEquals;

import java.util.List;

import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.CardsInPlay;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_SingleCardFragTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {
    	csa = new CardSlideActivity();
    	csa.onCreate(null);
    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
        

    
    @Test
    public void testCreateFragmentWithID()
    {
    	ViewPager pager = (ViewPager)csa.findViewById(R.id.cs_card_slider);
    	
    	CardFragmentAdapter adapter = (CardFragmentAdapter) pager.getAdapter();

    	Card c = CardsInPlay.instance.getCard(adapter.getCardID(2),CardType.WHITE);

    	c.setText("sometext");   	
    }
    

}