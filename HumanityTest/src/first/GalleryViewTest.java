package first;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.PathTestRunner;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
 
@RunWith(PathTestRunner.class)
public class GalleryViewTest {
 
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
    public void testG() {
   	
    }
   
 
}