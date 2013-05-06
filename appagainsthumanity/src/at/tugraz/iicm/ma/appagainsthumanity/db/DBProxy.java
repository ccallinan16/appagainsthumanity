package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Build;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBContract.Turn;


public class DBProxy {

	/*
	 * CONSTANTS
	 */
	
	public static final int NO_GAMES = 0; 		//1 user, no games
	public static final int CHOOSE_BLACK = 1; 	//3 users, 1 game, 2 rounds user has to choose black card
	public static final int CHOOSE_WHITE = 2; 	//3 users, 1 game, 2 rounds user has to choose white card
	
	public static final String[] PRESETS = {
		"PRESET_NO_GAMES",
		"PRESET_CHOOSE_BLACK",
		"PRESET_CHOOSE_WHITE",
	};
	
	/*
	 * UTILITY
	 */
	
	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writableDatabase;
	
	
	public DBProxy(Context context) {
		this.context = context;
		this.dbHelper = new DBHelper(context);
		this.readableDatabase = null;
		this.writableDatabase = null;
	}
	
	private SQLiteDatabase getReadableDatabase() {
	
		if (readableDatabase == null)
			readableDatabase = dbHelper.getReadableDatabase();
		return readableDatabase;
	}
	
	private SQLiteDatabase getWritableDatabase() {
		if (writableDatabase == null)
			writableDatabase = dbHelper.getWritableDatabase();
		return writableDatabase;
	}
	
	public void closeReadableDatabase() {
	
		if (readableDatabase != null)
			readableDatabase.close();
		this.readableDatabase = null;
	}
	
	public void closeWritableDatabase() {
		if (writableDatabase != null)
			writableDatabase.close();
		this.writableDatabase = null;
	}
	
	public void onStop() {
		closeReadableDatabase();
		closeWritableDatabase();
	}
	
	public String getUsername()
	{
		return context.getSharedPreferences(
				context.getString(R.string.sharedpreferences_filename), 
				Context.MODE_PRIVATE)
				.getString(
						context.getString(R.string.sharedpreferences_key_username), "");

	}
	
	
	public void printTables() {
			
		System.out.println("--------");
		//table game

		printTableWithOnlyInts(DBContract.Turn.TABLE_NAME);
		printTableWithOnlyInts(DBContract.Participation.TABLE_NAME);
		printPlayedWhiteCards();
		
	}

	private void printPlayedWhiteCards()
	{
		SQLiteDatabase db = this.getReadableDatabase();

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
	
	private void printTableWithOnlyInts(String tblName)
	{
		SQLiteDatabase db = this.getReadableDatabase();

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
		    "MAX(t1." + DBContract.Turn.COLUMN_NAME_ROUNDNUMBER + " ) as roundnumber",
		    DBContract.Game.COLUMN_NAME_ROUND_CAP,
		    DBContract.Game.COLUMN_NAME_LIMIT_ROUNDS,
		    DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_SCORE,
		    DBContract.Game.COLUMN_NAME_SCORE_CAP,
		    DBContract.Game.COLUMN_NAME_LIMIT_SCORE,
		    "COUNT(DISTINCT participation2." + DBContract.Participation.COLUMN_NAME_USER_ID + " ) AS numplayers",
		    DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " AS user_id",
		    "t1." + DBContract.Turn.COLUMN_NAME_USER_ID + " AS czar_user_id",
		    "t1." + DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID,
		    "COUNT(DISTINCT "+DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard._ID + ") AS numwhitechosen"
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
			" INNER JOIN " + DBContract.Turn.TABLE_NAME + " AS t1 ON " + 
			DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID + " = " + "t1." + DBContract.Turn.COLUMN_NAME_GAME_ID + 
			" LEFT OUTER JOIN " + DBContract.Turn.TABLE_NAME + " AS t2 ON " + 
			" t2.game_id = t1.game_id AND t2.roundnumber > t1.roundnumber " + 
			" LEFT JOIN " + DBContract.PlayedWhiteCard.TABLE_NAME + " ON " +
			"t1." + DBContract.Turn._ID + " = " + DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID,
			
			// The table to query
		    projection,                               // The columns to return
		    DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME + " = ? " + 
		    "AND t2.game_id IS NULL",
		    new String[]{username},                   // The values for the WHERE clause
		    DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID,                                     // don't group the rows
		    "t1.roundnumber = MAX(t1.roundnumber)",                                    // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
	}
	
	
	/*
	 * UPDATE QUERIES
	 * 
	 */
	
	public String checkIfTableExists(String table_name)
	{
	    SQLiteDatabase db = this.getReadableDatabase();

	    String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
	    +DBContract.Turn.TABLE_NAME+"'";
	    
	    Cursor cursor = db.rawQuery(query, null);
	    
	    if (cursor != null)
	    {
	        cursor.moveToLast();
	        
	        return cursor.getString(0);
	    }
	    return null;
	}
	
	public boolean checkEntryExistsWhere(String tableName, String game_id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.query(tableName,
    			null, //want all columns
    			game_id,
    			null, null,null,null);

	   	    
	    int numRows = 0;
	    
	    System.out.println("checkEntry exists");
	    
	    if (cursor != null)
	    {
		    cursor.moveToFirst();
		    numRows = cursor.getCount();
		    
		    System.out.println("checkEntry exists, rows: " + numRows);

		    cursor.close();    
	    }    
	    return (numRows > 0);
	}	

		
	public boolean setBlackCardID(long turn_id, int cardIndex) {
	    SQLiteDatabase db = this.getReadableDatabase();

	    ContentValues args = new ContentValues();
	    args.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, cardIndex);
	    
	    int affected = db.update(
	    		DBContract.Turn.TABLE_NAME, 
	    		args, 
	    		DBContract.Turn._ID + "=" + turn_id, 
	    		null);
	    
	    return (affected > 0);
	}
	
	public boolean setWhiteCardID(int turnid, int cardIndex) {
	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = new ContentValues();
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID, turnid);
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID, 1);
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID, cardIndex);
	 
	    // Inserting Row
	    long ret = db.insert(DBContract.PlayedWhiteCard.TABLE_NAME, null, values);
	    
	    return (ret != -1);
	}
	
	public int getPlayedWhiteCard(long turnid) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query(DBContract.PlayedWhiteCard.TABLE_NAME, 
	    		new String[] { DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID }, 
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + "=?", 
	    		new String[] { String.valueOf(turnid) } , 
	    		null, null, null);
	        
	    int cardId = -1;
	    
	    if (cursor != null)
	    {
		    cursor.moveToFirst();
		    cardId = cursor.getInt(0);
		    cursor.close();    
	    }
	    return cardId;
	}


	
	public int getLastTurnID()
	{
	    SQLiteDatabase db = this.getReadableDatabase();
	    String selectQuery = "SELECT * FROM " + DBContract.Turn.TABLE_NAME;
	    Cursor cursor = db.rawQuery(selectQuery, null);
	   
	    if (cursor != null)
	        if (!cursor.moveToLast())
	        	return -1;
	    
	    int turnid = cursor.getInt(0);
	    cursor.close();
	    
	    return turnid;
	}
	
	
	
	public int getBlackCard(long turnid) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    
	    Cursor cursor = db.query(DBContract.Turn.TABLE_NAME, 
	    		new String[] { DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID }, 
	    		DBContract.Turn._ID + "=?", 
	    		new String[] { String.valueOf(turnid) } , 
	    		null, null, null);
	        
	    if (cursor == null)
	    	return -1;
	    	    
	    int cardId = -1;
	    
	    cursor.moveToFirst();
	    cardId = cursor.getInt(0);
	    cursor.close();    
	    return cardId;
	}
	
	/*
	 * DEBUG QUERIES
	 */
	public long setPreset(int preset) {
		dbHelper.reinitialize(getWritableDatabase());
		long user_1, user_2, user_3, user_4;
		long game_1, game_2, game_3, game_4;
		long turn_1, turn_2, turn_3, turn_4;		
		
		//retrieve username
		String username = context.getSharedPreferences(context.getString(R.string.sharedpreferences_filename), Context.MODE_PRIVATE).getString(context.getString(R.string.sharedpreferences_key_username), "");
		
		switch(preset) {
		case NO_GAMES:
			//1 user, no games
			addUser(username);
			return 0;
			
		case CHOOSE_BLACK:
			//3 users, 1 game, 2 rounds, user has to choose black card
			//table user: add local user
				user_1 = addUser(username);
				user_2 = addUser("user2@dummy.com");
				user_3 = addUser("user3@dummy.com");
			//table game: add game
				game_1 = addGame(true, 5, false, 0);
			//table participation
				addParticipation(game_1, user_1);
				addParticipation(game_1, user_2);
				addParticipation(game_1, user_3);
			//table turn
				turn_1 = addTurn(game_1, 1, user_1, 22);
				turn_2 = addTurn(game_1, 2, user_1, null);
			//table playedWhiteCards
				//turn1, player2
				addPlayedWhiteCard(turn_1, user_2, 11, false);
				//turn1, player3
				addPlayedWhiteCard(turn_1, user_3, 12, true);
			return game_1;
			
		case CHOOSE_WHITE:
			//3 users, 1 game, 2 rounds, user has to choose black card
			//table user: add local user
				user_1 = addUser(username);
				user_2 = addUser("user2@dummy.com");
				user_3 = addUser("user3@dummy.com");
			//table game: add game
				game_1 = addGame(true, 5, false, 0);
			//table participation
				addParticipation(game_1, user_1);
				addParticipation(game_1, user_2);
				addParticipation(game_1, user_3);
			//table turn
				turn_1 = addTurn(game_1, 1, user_1, 1);
				turn_2 = addTurn(game_1, 2, user_2, 2);
			//table playedWhiteCards
				//turn1, player2
				addPlayedWhiteCard(turn_1, user_2, 11, null);
				//turn1, player3
				addPlayedWhiteCard(turn_1, user_3, 12, null);
				//turn2, player 3
				addPlayedWhiteCard(turn_2, user_3, 13, null);
			return game_1;
		}
		return 0;
	}
	
	protected long addUser(String username) {
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_USERNAME, username);
		return getWritableDatabase().insert(DBContract.User.TABLE_NAME, null, values);
	}
	
	protected long addGame(boolean limitRounds, int roundCap, boolean limitScore, int scoreCap) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Game.COLUMN_NAME_LIMIT_ROUNDS, limitRounds);
		values.put(DBContract.Game.COLUMN_NAME_ROUND_CAP, roundCap);
		values.put(DBContract.Game.COLUMN_NAME_LIMIT_SCORE, limitScore);
		values.put(DBContract.Game.COLUMN_NAME_SCORE_CAP, scoreCap);
		return getWritableDatabase().insert(DBContract.Game.TABLE_NAME, null, values);
	}
	
	protected long addParticipation(long game_id, long user_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Participation.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Participation.COLUMN_NAME_USER_ID, user_id);
		return getWritableDatabase().insert(DBContract.Participation.TABLE_NAME, null, values);
	}
	
	protected long addTurn(long game_id, int roundnumber, long user_id, Integer black_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Turn.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Turn.COLUMN_NAME_ROUNDNUMBER, roundnumber);
		values.put(DBContract.Turn.COLUMN_NAME_USER_ID, user_id);
		if (black_id != null)
			values.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, black_id);
		else
			values.putNull(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID);
		return getWritableDatabase().insert(DBContract.Turn.TABLE_NAME, null, values);
	}
	
	protected long addPlayedWhiteCard(long turn_id, long user_id, long white_card_id, Boolean won) {
		ContentValues values = new ContentValues();
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID, turn_id);
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID, white_card_id);
		if (won != null)
			values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WON, won);
		else
			values.putNull(DBContract.PlayedWhiteCard.COLUMN_NAME_WON);
		return getWritableDatabase().insert(DBContract.PlayedWhiteCard.TABLE_NAME, null, values);
	}


}
