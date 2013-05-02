package test.slow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import mocks.MockDealer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardView;
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
		csa.onCreate(null);

		
    	int numCards = 5;
    	MockDealer dealer = new MockDealer(numCards,csa);
        
    	csa.pageAdapter.setFragments(
    			SingleCardView.getFragmentFromCards(dealer.dealCards(CardType.WHITE, numCards), 30f)
    			);
    	
    	assertEquals(numCards,csa.pageAdapter.getCount());
	}

    
	@Test
	public void testQueryOnResourceXML() {
		
		int id = 34;
		
		CardSlideActivity csa = new CardSlideActivity();
		csa.onCreate(null);
		
		XMLReader reader = new XMLReader(csa);
		try {
			System.out.println(reader.getText(CardType.WHITE,id));
		} catch (Exception e)
		{
			fail();
		}
	}

    
    
    @Test
    public void testGameListDefaultElemTest() {
   	
    	ListView list = (ListView) ma.findViewById(R.id.invites_list_view);
    	assertEquals(1,list.getChildCount());
    }
   
    
    @Test
    public void testGameListAdded() {
    	ma.addPlayer("Gerald");
    	
    	ListView list = (ListView) ma.findViewById(R.id.invites_list_view);
    	
    	assertEquals(2,list.getChildCount());
    	ArrayAdapter<String> elems = (ArrayAdapter<String>) list.getAdapter();
    	assertEquals(elems.getItem(0),"Gerald");
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