package test.slow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.XMLReader;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;
 
@RunWith(PathTestRunner.class)
public class S_RobolectricStuff {
 
	CreateGameActivity ma;
	
    @Before
    public void setUp() throws Exception {
    	ma = new CreateGameActivity();
    	ma.onCreate(null);
    }
 
    @After
    public void tearDown() throws Exception {
    }
 
	@Test
	public void testMockDBResourceXML() {
		
		CardSlideActivity csa = new CardSlideActivity();
		DBProxy proxy = new DBProxy(csa);
		PresetHelper.setPreset(proxy, PresetHelper.SELECT_WHITE);
		
		Intent i = new Intent();
		i.putExtras(BundleCreator.createBundle(ViewContext.SELECT_WHITE, 
				PresetHelper.man.getLastTurnID()));
		csa.setIntent(i);
		csa.setProxy(proxy);
		csa.onCreate(null);

    	int numCards = 5;
    	MockDealer dealer = new MockDealer(csa);
    	dealer.setNumWhiteCards(numCards);
    	
    	ViewPager pager = (ViewPager) csa.findViewById(R.id.cs_card_slider);
    	((CardFragmentAdapter) pager.getAdapter()).setCards(dealer.dealCards(ViewContext.SELECT_WHITE));
   	
    	assertEquals(numCards,pager.getAdapter().getCount());
	}

    
	@Test
	public void testQueryOnResourceXML() {
		
		int id = 34;
		
		CardSlideActivity csa = new CardSlideActivity();
		csa.onCreate(null);
		
		XMLReader reader = new XMLReader(csa);
		try {
			reader.getText(CardType.WHITE,id);
		} catch (Exception e)
		{
			fail();
		}
	}
    
    @Test
    public void testButtonPopup() {
  /*  	Button btn = (Button) ma.findViewById(R.id.hi_button);
    	
    	EditText txt = (EditText) ma.findViewById(R.id.name_field);
    	txt.setText("Clara Oswald");
    	
    	ma.onClick(btn);
    	System.out.println(ShadowToast.getTextOfLatestToast());
    	
    	assertTrue( ShadowToast.getTextOfLatestToast().equals("Hi Clara Oswald!") ); 
    	
    	assert(btn != null);*/
    }
 
}