package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.provider.BaseColumns;

public abstract class DBContract {

	//tables
	
	//table describing game and options
	public static abstract class Game implements BaseColumns {
	    public static final String TABLE_NAME = "game";
	    public static final String COLUMN_NAME_GAME_TYPE = "gametype";
	}
	
	//table describing participating users of a game
	public static abstract class Participation implements BaseColumns {
	    public static final String TABLE_NAME = "participation";
	    public static final String COLUMN_NAME_GAME_ID = "game_id";
	    public static final String COLUMN_NAME_User_ID = "user_id";
	}	
	
	//default queries
	
	private static final String TYPE_TEXT = " TEXT";
	private static final String TYPE_INTEGER = " INTEGER";
	private static final String TYPE_PRIMARY_KEY = " INTEGER PRIMARY KEY";
	private static final String COMMA_SEP = ",";
	private static final String SEM_SEP = "; ";
	
	public static final String SQL_CREATE_ENTRIES =
	    //Table Game
		"CREATE TABLE " + DBContract.Game.TABLE_NAME + " (" +
	    DBContract.Game._ID + 							TYPE_PRIMARY_KEY + COMMA_SEP + 
		DBContract.Game.COLUMN_NAME_GAME_TYPE + 		TYPE_TEXT + 
	    " )" + SEM_SEP + 
	    //Table Participation
	    "CREATE TABLE " + DBContract.Participation.TABLE_NAME + " (" +
	    DBContract.Participation._ID + 					TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.Participation.COLUMN_NAME_GAME_ID + 	TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.Participation.COLUMN_NAME_User_ID + 	TYPE_INTEGER +
	    " )";
	

	public static final String SQL_DELETE_ENTRIES =
		//Table Game
	    "DROP TABLE IF EXISTS " + DBContract.Game.TABLE_NAME + SEM_SEP + 
	    //Table Participation
	    "DROP TABLE IF EXISTS " + DBContract.Participation.TABLE_NAME;
	
	//private constructor - don't construct this!
	private DBContract() {}
}
