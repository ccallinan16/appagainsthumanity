package test.slow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.connection.xmlrpc.XMLRPCServerProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

@RunWith(SQLTestRunner.class)
public class CopyOfDBProxyTest {
	
	DBProxy proxy;
	
	@Before
	public void setUp() throws Exception {
		
		
        Context c = new MainActivity();       
        proxy = new DBProxy(c);
        
        if (proxy == null)
        	System.err.println("dbh not created");		
        
        PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
	}

    @After
    public void teardown() {
		proxy.onStop();
    }

    @Test
    public void canGetWriteableDB() {   
        assertTrue(proxy != null);        
    }
	
	@Test
	public void checkIfTableExists()
	{	
		assertEquals(DBContract.Turn.TABLE_NAME,
				proxy.getter.checkIfTableExists(DBContract.Turn.TABLE_NAME));
	}
	
}
