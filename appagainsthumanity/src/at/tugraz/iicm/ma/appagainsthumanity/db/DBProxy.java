package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.util.DebugPrinter;


public class DBProxy {

	/*
	 * CONSTANTS
	 */
	public static final String[] PRESETS = {
		"PRESET_NO_GAMES",
		"PRESET_SELECT_BLACK",
		"PRESET_SELECT_WHITE",
	};
	
	/*
	 * UTILITY
	 */
	
	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase readableDatabase;
	private SQLiteDatabase writableDatabase;
	
	private SetterProxy setter;
	public GetterProxy getter;

	
	public DBProxy(Context context) {
		this.context = context;
		this.dbHelper = new DBHelper(context);
		this.readableDatabase = null;
		this.writableDatabase = null;
		
		setDBSetter(new SetterProxy(this));
		getter = new GetterProxy(this);
	}
	
	protected SQLiteDatabase getReadableDatabase() {
	
		if (readableDatabase == null)
			readableDatabase = dbHelper.getReadableDatabase();
		return readableDatabase;
	}
	
	protected SQLiteDatabase getWritableDatabase() {
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
	
	public int getUserID()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		
	    //this should only happen in testenvironments
	    if (getUsername().equals(""))
	    	return -1;
	    
	    Cursor cursor = db.query(DBContract.User.TABLE_NAME, 
	    		new String[] { DBContract.User._ID }, 
	    		DBContract.User.COLUMN_NAME_USERNAME + "=?" , 
	    		new String[] { getUsername() } , 
	    		null, null, null);
	        
	    int userID = -1;
	    
	    if (cursor != null)
	    {
		    cursor.moveToFirst();
		    userID = cursor.getInt(0);
		    cursor.close();    
	    }
	    return userID;
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
		    DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_SCORE,
		    DBContract.Game.COLUMN_NAME_SCORE_CAP,
		    "COUNT(DISTINCT participation2." + DBContract.Participation.COLUMN_NAME_USER_ID + " ) AS numplayers",
		    DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " AS user_id",
		    "t1." + DBContract.Turn.COLUMN_NAME_USER_ID + " AS czar_user_id",
		    "t1." + DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID,
		    "COUNT(DISTINCT "+DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard._ID + ") AS numwhitechosen",
		    "t1." + DBContract.Turn._ID + " AS turn_id"
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
	
	public Cursor readKnownOtherUsers(String username) {
		String[] projection = {
			DBContract.User.TABLE_NAME + "." + DBContract.User._ID,
		    DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME
		};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = DBContract.User.COLUMN_NAME_USERNAME + " ASC";

		return getReadableDatabase().query(
			DBContract.User.TABLE_NAME,		// The table to query
		    projection,                     // The columns to return
		    DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME + " != ? AND +" +
		    DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " > 1",	// The WHERE clause
		    new String[]{username},         // The values for the WHERE clause
		    null,                           // don't group the rows
		    null,                           // don't filter by row groups
		    sortOrder                       // The sort order
		    );
	}
	
	public Cursor readTurnlist(long game_id) {
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    DBContract.Turn.TABLE_NAME + "." + DBContract.Turn._ID,
		    DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_ROUNDNUMBER,
		    "COUNT(DISTINCT " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_USER_ID + " ) AS numplayers",
		    DBContract.User.TABLE_NAME + "1" + "." + DBContract.User.COLUMN_NAME_USERNAME,
		    DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID,
		    "COUNT(DISTINCT "+DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard._ID + ") AS numwhitechosen",
		    DBContract.User.TABLE_NAME + "2" + "." + DBContract.User.COLUMN_NAME_USERNAME + " AS winner",
		};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_ROUNDNUMBER + " DESC";

		return getReadableDatabase().query(
			DBContract.Game.TABLE_NAME +
			" INNER JOIN " + DBContract.Participation.TABLE_NAME + 
			" ON " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID +        
			" INNER JOIN " + DBContract.Turn.TABLE_NAME + 
			" ON " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID + " = " + DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_GAME_ID + 
			" INNER JOIN " + DBContract.User.TABLE_NAME + " AS " + DBContract.User.TABLE_NAME + "1" +  
			" ON " + DBContract.User.TABLE_NAME + "1" + "." + DBContract.User._ID + " = " + DBContract.Turn.TABLE_NAME + "." + DBContract.Turn.COLUMN_NAME_USER_ID + 
			" LEFT JOIN " + DBContract.PlayedWhiteCard.TABLE_NAME + 
			" ON " + DBContract.Turn.TABLE_NAME + "." + DBContract.Turn._ID + " = " + DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + 
			" LEFT JOIN " + DBContract.User.TABLE_NAME + " AS " + DBContract.User.TABLE_NAME + "2" +  
			" ON " + DBContract.User.TABLE_NAME + "2" + "." + DBContract.User._ID + " = " + DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID,
			
			// The table to query
		    projection,                               // The columns to return
		    DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID + " = ? ",
		    new String[]{String.valueOf(game_id)},    // The values for the WHERE clause
		    DBContract.Turn.TABLE_NAME + "." + DBContract.Turn._ID, // don't group the rows
		    null,//DBContract.PlayedWhiteCard.TABLE_NAME + "." + DBContract.PlayedWhiteCard.COLUMN_NAME_WON + " = 1",                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
	}
	
	public Cursor readPlayerList(long game_id) {
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			DBContract.Participation.TABLE_NAME + "." + DBContract.Participation._ID,
			DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME,
			DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_SCORE
		};

		// How you want the results sorted in the resulting Cursor
		String sortOrder = DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_SCORE + " DESC";

		return getReadableDatabase().query(
			DBContract.Participation.TABLE_NAME +
			" INNER JOIN " + DBContract.User.TABLE_NAME + 
			" ON " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_USER_ID + " = " + DBContract.User.TABLE_NAME + "." + DBContract.User._ID,
			// The table to query
		    projection,                               // The columns to return
		    DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = ? ",
		    new String[]{String.valueOf(game_id)},    // The values for the WHERE clause
		    null, // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
	}
	
	public void setPreset(int preset) {
		dbHelper.reinitialize(getWritableDatabase());
		getDBSetter().setPreset(preset);
	}
	
	public void reinitializeDB()
	{
		dbHelper.reinitialize(getWritableDatabase());
	}
	
	public void printTables() {
			
		System.out.println("--------");
		//table game
		SQLiteDatabase db = this.getReadableDatabase();

		DebugPrinter printer = new DebugPrinter(db);
		
		printer.printTableWithOnlyInts(DBContract.Participation.TABLE_NAME);
		System.out.println("\n----------------");

		
		printer.printTableWithOnlyInts(DBContract.Turn.TABLE_NAME);
		System.out.println("\n----------------");

		printer.printTableWithOnlyInts(DBContract.DealtWhiteCard.TABLE_NAME);
		System.out.println("\n----------------");

		printer.printPlayedWhiteCards();
		printer.printUsers();
	}

	public SetterProxy getDBSetter() {
		return setter;
	}

	public void setDBSetter(SetterProxy setter) {
		this.setter = setter;
	}

	
}
