package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class DBProxy {

	/*
	 * CONSTANTS
	 */
	
	public static final int PRESET_NO_GAMES = 0; 		//1 user, no games
	public static final int PRESET_CHOOSE_BLACK = 1; 	//1 user, 1 game, user has to choose black card
	
	public static final String[] PRESETS = {
		"PRESET_NO_GAMES",
		"PRESET_CHOOSE_BLACK",
	};
	
	/*
	 * UTILITY
	 */
	
	private DBHelper dbHelper;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writableDatabase;
	
	
	public DBProxy(Context context) {
		this.dbHelper = new DBHelper(context);
		this.readableDatabase = null;
		this.writableDatabase = null;
	}

	public SQLiteDatabase getReadableDatabase() {
		if (readableDatabase == null)
			readableDatabase = dbHelper.getReadableDatabase();
		return readableDatabase;
	}
	
	public SQLiteDatabase getWritableDatabase() {
		if (writableDatabase == null)
			writableDatabase = dbHelper.getWritableDatabase();
		return writableDatabase;
	}
	
	public void dumpTables() {
		String[] projection = new String[]{};
		Cursor c;
		
		System.out.println("BEGIN DATABASE DUMP");
		//table game
		System.out.println("table " + DBContract.Game.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.Game.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table participation
		System.out.println("table " + DBContract.Participation.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.Participation.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table user
		System.out.println("table " + DBContract.User.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.User.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table turn
		System.out.println("table " + DBContract.Turn.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.Turn.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table played_white_card
		System.out.println("table " + DBContract.PlayedWhiteCard.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.PlayedWhiteCard.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		//table dealt_white_card
		System.out.println("table " + DBContract.DealtWhiteCard.TABLE_NAME);
		c = this.getReadableDatabase().query(DBContract.DealtWhiteCard.TABLE_NAME,
			projection,null,new String[]{},null,null,null);
		DatabaseUtils.dumpCursor(c);
		
		System.out.println("END DATABASE DUMP");
	}

	
	/*
	 * READ QUERIES
	 */
	public Cursor readGameList(String username) {
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID,
		    DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_SCORE,
		    "COUNT(participation2." + DBContract.Participation.COLUMN_NAME_USER_ID + " )",
		    DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_ROUNDNUMBER
		};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = DBContract.Game.COLUMN_NAME_UPDATED + " DESC";

		return getReadableDatabase().query(
			DBContract.Game.TABLE_NAME +
			" INNER JOIN " + DBContract.Participation.TABLE_NAME + 
			" ON " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID +        
			" INNER JOIN " + DBContract.User.TABLE_NAME + " ON " + 
			DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " = " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_USER_ID +
			" INNER JOIN " + DBContract.Participation.TABLE_NAME + " AS participation2 ON " + 
			DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID +
			" INNER JOIN " + DBContract.Turn.TABLE_NAME + " ON " + 
			DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID + " = " + DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_GAME_ID,
			// The table to query
		    projection,                               // The columns to return
		    DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME + " = ?",
		    new String[]{username},                   // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
	}
	
	
	/*
	 * UPDATE QUERIES
	 * 
	 */
	
	
	
	/*
	 * DEBUG QUERIES
	 */
	public void setPreset(int preset) {
		dbHelper.reinitialize(getWritableDatabase());
		ContentValues values = new ContentValues();
		
		switch(preset) {
		case PRESET_NO_GAMES:
			//1 user, no games
			values.put(DBContract.User.COLUMN_NAME_USERNAME, "pkoch37@gmail.com");
			getWritableDatabase().insert(DBContract.User.TABLE_NAME, null, values);
			break;
			
		case PRESET_CHOOSE_BLACK:
			//1 user, 1 game, user has to choose black card
			//table user: add local user
			values.put(DBContract.User.COLUMN_NAME_USERNAME, "pkoch37@gmail.com");
			long user_id = getWritableDatabase().insert(DBContract.User.TABLE_NAME, null, values);
			//table game: add game
			values.clear();
			values.put(DBContract.Game.COLUMN_NAME_LIMIT_ROUNDS, "true");
			values.put(DBContract.Game.COLUMN_NAME_ROUND_CAP, "5");
			values.put(DBContract.Game.COLUMN_NAME_LIMIT_SCORE, "false");
			values.put(DBContract.Game.COLUMN_NAME_SCORE_CAP, "0");
			long game_id = getWritableDatabase().insert(DBContract.Game.TABLE_NAME, null, values);
			//table participation
			values.clear();
			values.put(DBContract.Participation.COLUMN_NAME_GAME_ID, game_id);
			values.put(DBContract.Participation.COLUMN_NAME_USER_ID, user_id);
			getWritableDatabase().insert(DBContract.Participation.TABLE_NAME, null, values);
			//table turn
			values.clear();
			values.put(DBContract.Turn.COLUMN_NAME_GAME_ID, game_id);
			values.put(DBContract.Turn.COLUMN_NAME_ROUNDNUMBER, 1);
			values.put(DBContract.Turn.COLUMN_NAME_USER_ID, user_id);
			values.putNull(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID);
			getWritableDatabase().insert(DBContract.Turn.TABLE_NAME, null, values);
			break;
			
		}
		
	}
}
