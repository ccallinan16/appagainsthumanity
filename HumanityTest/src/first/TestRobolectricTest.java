package first;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.widget.Button;
import android.widget.EditText;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowToast;
 
@RunWith(RobolectricTestRunner.class)
public class TestRobolectricTest {
 
	MainActivity ma;
	
    @Before
    public void setUp() throws Exception {
    	ma = new MainActivity();
    }
 
    @After
    public void tearDown() throws Exception {
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