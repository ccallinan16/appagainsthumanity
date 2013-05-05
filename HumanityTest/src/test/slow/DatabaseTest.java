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
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.DataBase;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

@RunWith(SQLTestRunner.class)
public class DatabaseTest {
	
    private static final int CURRENT_DB_VERSION = 1;
	
    DBProxy db;
    Context c;
    
	@Before
	public void setUp() throws Exception {
        Context c = new Activity();       
        db = new DBProxy(c);
	}
		
	@After
	public void tearDown()
	{

	}
	
	@Test
	public void setBlackIDAndSendToDB()
	{
		PresetHelper preset = new PresetHelper(db);
		
		preset.addGame();
		preset.addTurn(preset.getFirstGame());
		long turnid = preset.getLastTurn();
		
		//TODO: while we're not able to pass turnid
		turnid = 1;

		db.printTables();

		GUIEmulator emulator = new GUIEmulator();
		
		Card origin = emulator.createSelectionTransitionReturnSelected(
													ViewContext.SELECT_BLACK);
		db.printTables();
		assertEquals(origin.getId(),db.getBlackCard(turnid));
	}
	
	@Test
	public void selectWhiteAndSendToDB()
	{
		PresetHelper preset = new PresetHelper(db);
		
		preset.addGame();
		preset.addTurn(preset.getFirstGame());
		long turnid = preset.getLastTurn();

		turnid = 2;
		
		GUIEmulator emulator = new GUIEmulator();
		
		Card origin = emulator.createSelectionTransitionReturnSelected(
													ViewContext.SELECT_WHITE);
		
		assertEquals(origin.getId(),db.getPlayedWhiteCard(turnid));
	}	
}
