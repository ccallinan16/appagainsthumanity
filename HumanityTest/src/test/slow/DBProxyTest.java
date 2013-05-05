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
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;

@RunWith(SQLTestRunner.class)
public class DBProxyTest {
	
	DBProxy proxy;
	
	@Before
	public void setUp() throws Exception {
        Context c = new Activity();       
        proxy = new DBProxy(c);
        
        if (proxy == null)
        	System.err.println("dbh not created");		
        
        proxy.setPreset(1);
	}

    @After
    public void teardown() {
        proxy.closeReadableDatabase();
        proxy.closeWritableDatabase();
    }

    @Test
    public void canGetWriteableDB() {   
        assertTrue(proxy != null);        
    }
	
	@Test
	public void checkIfTableExists()
	{	
		assertEquals(DBContract.Turn.TABLE_NAME,
				proxy.checkIfTableExists(DBContract.Turn.TABLE_NAME));
	}
	
	@Test
	public void getTurnIDForBlackCard()
	{
		int turnid = proxy.getLastTurnID();
		
		assertEquals(2,turnid);
	}	
}
