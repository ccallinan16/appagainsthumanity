package at.tugraz.iicm.ma.appagainsthumanity.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;

public class GetterProxy {
	
	DBProxy db;
	
	 public GetterProxy( DBProxy dbProxy) {
		// TODO Auto-generated constructor stub
		this.db = dbProxy;

	}
	
	public List<Integer> getPlayedWhiteCards(long turnid)
	{
	    Cursor cursor = db.getReadableDatabase().query(DBContract.PlayedWhiteCard.TABLE_NAME, 
	    		new String[] { DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID }, 
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + "=?", 
	    		new String[] { String.valueOf(turnid) } , 
	    		null, null, null);
	        
	    List<Integer> list = new ArrayList<Integer>();
	    
	    if (cursor != null && cursor.moveToFirst())
	    {
	    	do
	    	{
			    list.add(cursor.getInt(0));
	    	}
	    	while (cursor.moveToNext());
	    }
	    cursor.close();
	    
	    return list;

	}
	 
	public int getPlayedWhiteCard(long turnid, long user) {
	    
	    Cursor cursor = db.getReadableDatabase().query(DBContract.PlayedWhiteCard.TABLE_NAME, 
	    		new String[] { DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID }, 
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + "=? AND " +
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID + "=?", 
	    		new String[] { String.valueOf(turnid), String.valueOf(user) } , 
	    		null, null, null);
	        
	    int cardId = -1;
	    
	    if (cursor != null && cursor.getCount() > 0)
	    {
		    cursor.moveToFirst();
		    cardId = cursor.getInt(0);
		    cursor.close();
	    }
	    return cardId;
	}
	
	public int getLastTurnID()
	{
	    String selectQuery = "SELECT * FROM " + DBContract.Turn.TABLE_NAME;
	    Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
	   
	    if (cursor != null)
	        if (!cursor.moveToLast()) {
	        	cursor.close();
	        	return -1;
	        }
	    
	    int turnid = cursor.getInt(0);
	    cursor.close();
	    
	    return turnid;
	}
		
	public int getGameIDFromTurn(long turn_id)
	{
	    Cursor cursor = db.getReadableDatabase().query(DBContract.Turn.TABLE_NAME, 
	    		new String[] { DBContract.Turn.COLUMN_NAME_GAME_ID }, 
	    		DBContract.Turn._ID + "=?" , 
	    		new String[] { String.valueOf(turn_id) } , 
	    		null, null, null);
	        
	    int gameID = -1;
	    	    
	    if (cursor != null)
	    {
	    	if (cursor.getCount() > 0) {
			    cursor.moveToFirst();
			    gameID = cursor.getInt(0);
	    	}
		    cursor.close();    
	    }
	    return gameID;

	}
	
	public long getScore(long game_id, long user_id) {

	    Cursor cursor = db.getReadableDatabase().query(
	    		DBContract.Participation.TABLE_NAME, 
	    		new String[] { DBContract.Participation.COLUMN_NAME_SCORE }, 
	    		DBContract.Participation.COLUMN_NAME_GAME_ID + "=? AND " +
	    		DBContract.Participation.COLUMN_NAME_USER_ID + "=?", 
	    		new String[] { String.valueOf(game_id), String.valueOf(user_id) } , 
	    		null, null, null);
	    	    
	    long score = -1;
	    
	    if (cursor != null && cursor.getCount() > 0)
	    {
	    	cursor.moveToFirst();
	    	score = cursor.getInt(0);
	    	cursor.close();
	    }
	    return score;
	}
	
	public long getUserOfWonCard(long turn_id)
	{
	    Cursor cursor = db.getReadableDatabase().query(DBContract.PlayedWhiteCard.TABLE_NAME, 
	    		new String[] { DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID }, 
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID + "=? AND " +
	    		DBContract.PlayedWhiteCard.COLUMN_NAME_WON + " = 1", 
	    		new String[] { String.valueOf(turn_id) } , 
	    		null, null, null);
	    	    
	    long userID = 0;
	    
	    if (cursor != null && cursor.getCount() > 0)
	    {
	    	cursor.moveToFirst();
	    	//for (int index = 0; index < cursor.getCount(); index++)
	    	{
	    		userID = cursor.getInt(0);
	    		//cursor.moveToNext();
	    	}
	    	cursor.close();
	    	
	    }

	    return userID;
	}
	
	public List<Integer> getDealtWhiteCards(long turn_id) {
	    Cursor cursor = db.getReadableDatabase().query(DBContract.DealtWhiteCard.TABLE_NAME, 
	    		new String[] { DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID }, 
	    		DBContract.DealtWhiteCard.COLUMN_NAME_GAME_ID + "=?", 
	    		new String[] { String.valueOf(getGameIDFromTurn(turn_id)) } , 
	    		null, null, null);
	    
	    ArrayList<Integer> cardIDs = new ArrayList<Integer>();
	    
	    if (cursor != null)
	    {
	    	cursor.moveToFirst();
	    	for (int index = 0; index < cursor.getCount(); index++)
	    	{
	    		cardIDs.add(cursor.getInt(0));
	    		cursor.moveToNext();
	    	}
	    	cursor.close();
	    	
	    }
	    
	    return cardIDs;
	        
	}	
	
	public int getBlackCard(long turnid) {
	    
	    Cursor cursor = db.getReadableDatabase().query(DBContract.Turn.TABLE_NAME, 
	    		new String[] { DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID }, 
	    		DBContract.Turn._ID + "=?", 
	    		new String[] { String.valueOf(turnid) } , 
	    		null, null, null);
	    	    
	    int cardId = -1;
	    
	    if (cursor != null && cursor.getCount() > 0)
	    {
	    	if (cursor.getCount() > 0) {
			    cursor.moveToFirst();
			    cardId = cursor.getInt(0);
	    	}
		    cursor.close();    
	    }
	    return cardId;
	}
	
	public String checkIfTableExists(String table_name)
	{

	    String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"
	    +DBContract.Turn.TABLE_NAME+"'";
	    
	    Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
	    
	    if (cursor != null)
	    {
	        cursor.moveToLast();
	        String tmp = cursor.getString(0);
	        cursor.close();
	        return tmp;
	    }
	    return null;
	}
	
	public boolean checkEntryExistsWhere(String tableName, String whereArguments) {
	    Cursor cursor = db.getReadableDatabase().query(tableName,
    			null, //want all columns
    			whereArguments,
    			null, null,null,null);

	   	    
	    int numRows = 0;
	    	    
	    if (cursor != null)
	    {
		    cursor.moveToFirst();
		    numRows = cursor.getCount();
		    cursor.close();    
	    }    
	    return (numRows > 0);
	}

//	public HashMap<String, String> getGameInfoList() {
//		String selectQuery = DBContract.User.TABLE_NAME +
//				" INNER JOIN " + DBContract.Participation.TABLE_NAME + 
//				" ON " + DBContract.User.TABLE_NAME + "." + DBContract.User._ID + " = " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_USER_ID +
//				" INNER JOIN " + DBContract.Game.TABLE_NAME + 
//				" ON " + DBContract.Participation.TABLE_NAME + "." + DBContract.Participation.COLUMN_NAME_GAME_ID + " = " + DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID;
//		
//		String[] projection = new String[] {
//			DBContract.Game.TABLE_NAME + "." + DBContract.Game._ID,
//			DBContract.Game.TABLE_NAME + "." + DBContract.Game.COLUMN_NAME_UPDATED
//		};
//		
//	    Cursor cursor = db.getReadableDatabase().query(selectQuery,
//	    		projection, 
//	    		DBContract.User.TABLE_NAME + "." + DBContract.User.COLUMN_NAME_USERNAME + "=?", 
//	    		new String[] { db.getUsername() } , 
//	    		null, null, null);
//	    
//	    HashMap<String, String> data = new HashMap<String, String>();
//	    
//	    if (cursor != null)
//	    {
//	    	if (cursor.moveToFirst())
//	    		do {
//	    			data.put(cursor.getString(0), cursor.getString(1).replace("-", "").replace(" ", "T").replace(":", ":"));
//	    		} while (cursor.moveToNext());
//	    	cursor.close();
//	    }
//	    return data;
//	}	
	
}
	
