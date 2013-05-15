package test.slow;
import static org.junit.Assert.assertEquals;

import mocks.IDToCardTranslator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Context;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.connection.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;

@RunWith(SQLTestRunner.class)
public class DatabaseTest {
		
    DBProxy proxy;
    ServerConnector connector;
    
	@Before
	public void setUp() throws Exception {
		MainActivity ma = new MainActivity();
		ma.setUsername("elisabeth");
        Context c = new Activity();       
        proxy = new DBProxy(c);
        connector = new ServerConnector(proxy);
    	CardCollection.instance.setTranslator(new IDToCardTranslator(c));
	}
		
	@After
	public void tearDown()
	{
		proxy.onStop();
	}
	
	@Test
	public void setBlackIDAndSendToDB()
	{
		PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
		
		//while GUI knows nothing about turnids
		long turnid = PresetHelper.man.getLastTurnID();

		GUIEmulator emulator = new GUIEmulator();
		
		Card origin = emulator.createSelectionTransitionReturnSelected(
													ViewContext.SELECT_BLACK,
													turnid);
		assertEquals(origin.getId(),proxy.getter.getBlackCard(turnid));
	}
	
	@Test
	public void selectWhiteAndSendToDB()
	{	
		PresetHelper.setPreset(proxy, PresetHelper.SELECT_WHITE);

		//while GUI knows nothing about turnids
		long turnid = PresetHelper.man.getLastTurnID();
		
		GUIEmulator emulator = new GUIEmulator();
		
		Card origin = emulator.createSelectionTransitionReturnSelected(
													ViewContext.SELECT_WHITE,turnid);
		
		System.out.println(PresetHelper.man.getCurrentUserID());
		assertEquals(PresetHelper.man.getCurrentUserID(),proxy.getUserID());		
		assertEquals(origin.getId(),proxy.getter.getPlayedWhiteCard(turnid, proxy.getUserID()));
	}	
}
