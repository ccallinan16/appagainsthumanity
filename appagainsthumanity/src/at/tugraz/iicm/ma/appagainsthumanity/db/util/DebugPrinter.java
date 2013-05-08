package at.tugraz.iicm.ma.appagainsthumanity.db.util;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract;

public class DebugPrinter {
	SQLiteDatabase db;
	
	public DebugPrinter(SQLiteDatabase readable) {
		 db = readable;	
	}
	
	/**
	 * using the cursor dump
	 */

	
	
	public void dumpTables() {
		String[] projection = new String[]{};
		Cursor c;
		
		System.out.println("BEGIN DATABASE DUMP");
		//table game
		System.out.println("table " + DBContract.Game.TABLE_NAME);
		c = db.query(DBContract.Game.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table participation
		System.out.println("table " + DBContract.Participation.TABLE_NAME);
		c = db.query(DBContract.Participation.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table user
		System.out.println("table " + DBContract.User.TABLE_NAME);
		c = db.query(DBContract.User.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table turn
		System.out.println("table " + DBContract.Turn.TABLE_NAME);
		c = db.query(DBContract.Turn.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table played_white_card
		System.out.println("table " + DBContract.PlayedWhiteCard.TABLE_NAME);
		c = db.query(DBContract.PlayedWhiteCard.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table dealt_white_card
		System.out.println("table " + DBContract.DealtWhiteCard.TABLE_NAME);
		c = db.query(DBContract.DealtWhiteCard.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		System.out.println("END DATABASE DUMP");
	}
	
	
	/**
	 * using stdout
	 */
	
	
	
	public void printUsers()
	{
		System.out.println("table " + DBContract.User.TABLE_NAME);
		
	    String selectQuery = "SELECT  * FROM " + DBContract.User.TABLE_NAME;
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    for (int row = -1; row < cursor.getCount(); row++)
	    {
	    	String line = row +"\t| ";
	    	if (row == -1)
	    	{
	    		line += cursor.getColumnName(0) + "\t| ";
    			line += cursor.getColumnName(1) + "\t| ";
	    	}
	    	else
	    	{
	    		line += cursor.getInt(0) + "\t| ";
    			line += cursor.getString(1) + "\t| ";

	    	}
		    System.out.println(line);
		    cursor.moveToNext();
	    }
	}
	
	public void printPlayedWhiteCards()
	{
		System.out.println("table " + DBContract.PlayedWhiteCard.TABLE_NAME);
		
	    String selectQuery = "SELECT  * FROM " + DBContract.PlayedWhiteCard.TABLE_NAME;
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    for (int row = -1; row < cursor.getCount(); row++)
	    {
	    	String line = row +"\t| ";
		    for (int index = 0; index < cursor.getColumnCount()-1; index++)
		    {
		    	if (row == -1)
		    		line += cursor.getColumnName(index) + "\t| ";
		    	else
		    	{
		    		if (cursor.isNull(index))
		    			line += "null\t| ";
		    		else
		    		{
		    			if (index > 0)
		    				line += cursor.getInt(index) + "\t\t| ";
		    			else
		    				line += cursor.getInt(index) + "\t| ";
			    		
		    		}
		    	}
		    }
		    System.out.println(line);
		    cursor.moveToNext();
	    }
	
	}
	
	public void printTableWithOnlyInts(String tblName)
	{
		System.out.println("table " + tblName);
		
	    String selectQuery = "SELECT  * FROM " + tblName;
	    Cursor cursor = db.rawQuery(selectQuery, null);

	    for (int row = -1; row < cursor.getCount(); row++)
	    {
	    	String line = row +"\t| ";
		    for (int index = 0; index < cursor.getColumnCount(); index++)
		    {
		    	if (row == -1)
		    		line += cursor.getColumnName(index) + "\t| ";
		    	else
		    	{
		    		if (cursor.isNull(index))
		    			line += "null\t| ";
		    		else
		    		{
		    			if (index > 0)
		    				line += cursor.getInt(index) + "\t\t| ";
		    			else
		    				line += cursor.getInt(index) + "\t| ";
			    		
		    		}
		    	}
		    }
		    System.out.println(line);
		    cursor.moveToNext();
	    }
	    
	}

	
}
