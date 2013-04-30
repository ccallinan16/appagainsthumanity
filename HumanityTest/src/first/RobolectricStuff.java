package first;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;

import com.xtremelabs.robolectric.RobolectricTestRunner;
 
@RunWith(PathTestRunner.class)
public class RobolectricStuff {
 
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