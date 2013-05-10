package at.tugraz.iicm.ma.appagainsthumanity.db;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public class GetterProxy {
	
	DBProxy db;
	
	 public GetterProxy( DBProxy dbProxy) {
		// TODO Auto-generated constructor stub
		this.db = dbProxy;

	}
	
	public int getPlayedWhiteCard(long turnid) {
	    
	    Cursor cursor = db.getReadableDatabase().query(DBContract.PlayedWhiteCard.TABLE_NAME, 
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
}
