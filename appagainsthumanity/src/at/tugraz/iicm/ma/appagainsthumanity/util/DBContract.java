package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.provider.BaseColumns;

public abstract class DBContract {

	//tables
	
	//table describing game and options
	public static abstract class Game implements BaseColumns {
	    public static final String TABLE_NAME = "game";
	    public static final String COLUMN_NAME_GAMETYPE = "gametype";
	    public static final String COLUMN_NAME_CAP =	"cap";
	    public static final String COLUMN_NAME_UPDATED = "updated";
	}
	
	//table describing participating users of a game
	public static abstract class Participation implements BaseColumns {
	    public static final String TABLE_NAME = "participation";
	    public static final String COLUMN_NAME_GAME_ID = "game_id";
	    public static final String COLUMN_NAME_USER_ID = "user_id";
	}	
	
	//table describing users
	public static abstract class User implements BaseColumns {
	    public static final String TABLE_NAME = "user";
	    public static final String COLUMN_NAME_USERNAME = "username";
	}
	
	//table describing turns
	public static abstract class Turn implements BaseColumns {
	    public static final String TABLE_NAME = "turn";
	    public static final String COLUMN_NAME_GAME_ID = "game_id";
	    public static final String COLUMN_NAME_ROUNDNUMBER = "roundnumber";
	    public static final String COLUMN_NAME_USER_ID = "user_id";
	    public static final String COLUMN_NAME_BLACK_CARD_ID = "black_card_id";
	}
	
	//table describing played white cards
	public static abstract class PlayedWhiteCard implements BaseColumns {
	    public static final String TABLE_NAME = "played_white_card";
	    public static final String COLUMN_NAME_TURN_ID = "turn_id";
	    public static final String COLUMN_NAME_USER_ID = "user_id";
	    public static final String COLUMN_NAME_WHITE_CARD_ID = "white_card_id";
	    public static final String COLUMN_NAME_WON = "won";
	}
	
	//table describing dealt white cards
	public static abstract class DealtWhiteCard implements BaseColumns {
	    public static final String TABLE_NAME = "dealt_white_card";
	    public static final String COLUMN_NAME_GAME_ID = "game_id";
	    public static final String COLUMN_NAME_WHITE_CARD_ID = "white_card_id";
	    public static final String COLUMN_NAME_PLAYER_ID = "player_id";
	}
	
	//table describing white cards
	public static abstract class WhiteCard implements BaseColumns {
	    public static final String TABLE_NAME = "white_card";
	}
	
	//table describing black cards
	public static abstract class BlackCard implements BaseColumns {
	    public static final String TABLE_NAME = "black_card";
	}
	
	//default queries
	
	private static final String TYPE_TEXT = " TEXT";
	private static final String TYPE_INTEGER = " INTEGER";
	private static final String TYPE_BOOLEAN = " BOOLEAN";
	private static final String TYPE_DATE =	 " DATE";
	private static final String TYPE_PRIMARY_KEY = " INTEGER PRIMARY KEY";
	private static final String COMMA_SEP = ",";
	
	public static final String[] SQL_CREATE_ENTRIES = new String[]{
		
	    //Table Game
		"CREATE TABLE " + DBContract.Game.TABLE_NAME + " (" +
	    DBContract.Game._ID + 					TYPE_PRIMARY_KEY + 	COMMA_SEP + 
		DBContract.Game.COLUMN_NAME_GAMETYPE + 	TYPE_BOOLEAN +  	COMMA_SEP +
		DBContract.Game.COLUMN_NAME_CAP + 	TYPE_INTEGER + 		COMMA_SEP + 
		DBContract.Game.COLUMN_NAME_UPDATED + 	TYPE_DATE + 
	    " )",
	    
	    //Table Participation
	    "CREATE TABLE " + DBContract.Participation.TABLE_NAME + " (" +
	    DBContract.Participation._ID + 					TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.Participation.COLUMN_NAME_GAME_ID + 	TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.Participation.COLUMN_NAME_USER_ID + 	TYPE_INTEGER +
	    " )",
	    
	    //Table user
	    "CREATE TABLE " + DBContract.User.TABLE_NAME + " (" +
	    DBContract.User._ID + 					TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.User.COLUMN_NAME_USERNAME + 	TYPE_TEXT +
	    " )",
	    
	    //Table turn
	    "CREATE TABLE " + DBContract.Turn.TABLE_NAME + " (" +
	    DBContract.Turn._ID + 						TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.Turn.COLUMN_NAME_GAME_ID + 		TYPE_INTEGER +		COMMA_SEP +
	    DBContract.Turn.COLUMN_NAME_ROUNDNUMBER + 	TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.Turn.COLUMN_NAME_USER_ID + 		TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID + TYPE_INTEGER + 		" DEFAULT NULL"  +
	    " )",
	    
	    //Table played_white_card
	    "CREATE TABLE " + DBContract.PlayedWhiteCard.TABLE_NAME + " (" +
	    DBContract.PlayedWhiteCard._ID + 						TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + 		TYPE_INTEGER +		COMMA_SEP +
	    DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID + 		TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID + 	TYPE_INTEGER +		COMMA_SEP + 
	    DBContract.PlayedWhiteCard.COLUMN_NAME_WON + 			TYPE_BOOLEAN + 		" DEFAULT false" +  
	    " )",
	    
	    //Table dealt_white_card
	    "CREATE TABLE " + DBContract.DealtWhiteCard.TABLE_NAME + " (" +
	    DBContract.DealtWhiteCard._ID + 						TYPE_PRIMARY_KEY + 	COMMA_SEP +
	    DBContract.DealtWhiteCard.COLUMN_NAME_GAME_ID + 		TYPE_INTEGER +		COMMA_SEP +
	    DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID + 	TYPE_INTEGER + 		COMMA_SEP +
	    DBContract.DealtWhiteCard.COLUMN_NAME_PLAYER_ID + 		TYPE_INTEGER +
	    " )",
	    
	    //Table white_card
	    "CREATE TABLE " + DBContract.WhiteCard.TABLE_NAME + " (" +
	    DBContract.WhiteCard._ID + TYPE_PRIMARY_KEY +
	    " )",
	    
	    //Table black_card
	    "CREATE TABLE " + DBContract.BlackCard.TABLE_NAME + " (" +
	    DBContract.BlackCard._ID + TYPE_PRIMARY_KEY +	    
	    " )"};
	

	public static final String[] SQL_DELETE_ENTRIES = new String[]{
		//Table Game
	    "DROP TABLE IF EXISTS " + DBContract.Game.TABLE_NAME,
	    //Table Participation
	    "DROP TABLE IF EXISTS " + DBContract.Participation.TABLE_NAME,
	    //Table User
	    "DROP TABLE IF EXISTS " + DBContract.User.TABLE_NAME,
	    //Table Turn
	    "DROP TABLE IF EXISTS " + DBContract.Turn.TABLE_NAME,
	    //Table PlayedWhiteCard
	    "DROP TABLE IF EXISTS " + DBContract.PlayedWhiteCard.TABLE_NAME,
	    //Table DealtWhiteCard
	    "DROP TABLE IF EXISTS " + DBContract.DealtWhiteCard.TABLE_NAME,
	    //Table WhiteCard
	    "DROP TABLE IF EXISTS " + DBContract.WhiteCard.TABLE_NAME,
	    //Table WhiteCard
	    "DROP TABLE IF EXISTS " + DBContract.BlackCard.TABLE_NAME};
	
	public static final String[] SQL_DEFAULT_ENTRIES = new String[]{
		//Table User
	    "INSERT INTO " + DBContract.User.TABLE_NAME + " VALUES ( PLAYED_CARDS )"
	};
	
	
	//private constructor - don't construct this!
	private DBContract() {}
}
