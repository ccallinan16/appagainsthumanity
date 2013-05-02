package test.slow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mocks.MockDB;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_SingleCardViewTest {
 
	CardSlideActivity csa;
	
    @Before
    public void setUp() throws Exception {

    }
 
    @After
    public void tearDown() throws Exception {
    	
    }
    
    @Test
    public void testCreateSCVFromString()
    {
    	String message = "black card text____.";
    	float textSize = 50f;
    	SingleCardView scv = SingleCardView.newInstance(new Card(message,0,CardType.BLACK),textSize);	

    	assertEquals(message,scv.getText());
    }
        
    
    @Test
    public void testGetSCVFragmentFunction()
    {
    	String[] cards = {"black card text 01 ____.",
    			"some other text. ____ .", 
    			"and a third one, just to be safe."};
    	
    	List<Fragment> fragments = SingleCardView.getSCVFragments(CardType.BLACK,cards);
    	assertEquals(cards[1],((SingleCardView) fragments.get(1)).getText());
    }
   

}