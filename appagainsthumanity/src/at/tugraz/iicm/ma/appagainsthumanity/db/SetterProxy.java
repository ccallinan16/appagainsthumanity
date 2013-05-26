package at.tugraz.iicm.ma.appagainsthumanity.db;

import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.Cursor;

public class SetterProxy {
	
	public static final int NUM_POINTS_INCR = 1;
	
	 DBProxy db;
	
	public SetterProxy(DBProxy dbProxy) {

		this.db = dbProxy;
	}
	

	public long addDealtWhiteCards(long turn_id, Integer num) {
		
		//we get userID from shared prefs, as we are only calling 
		//that function for ourselves
		
		ContentValues values = new ContentValues();
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_GAME_ID, db.getter.getGameIDFromTurn(turn_id));
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_PLAYER_ID, db.getUserID());
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID, num);

		return insertIgnoreOverwrite(DBContract.DealtWhiteCard.TABLE_NAME, DBContract.DealtWhiteCard._ID, values);
		
	}
	
	public void dropDealtWhiteCards(long game_id) {
		db.getWritableDatabase().delete(DBContract.DealtWhiteCard.TABLE_NAME, 
										DBContract.DealtWhiteCard.COLUMN_NAME_GAME_ID + " = ?",
										new String[]{String.valueOf(game_id)});
	}
	
	public long addDealtWhiteCard(long id, long game_id, long player_id, long white_card_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.DealtWhiteCard._ID, id);
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_PLAYER_ID, player_id);
		values.put(DBContract.DealtWhiteCard.COLUMN_NAME_WHITE_CARD_ID, white_card_id);

		return insertIgnoreOverwrite(DBContract.DealtWhiteCard.TABLE_NAME, DBContract.DealtWhiteCard._ID, values);
	}
	
	public void dropDealtBlackCards(long game_id) {
		db.getWritableDatabase().delete(DBContract.DealtBlackCard.TABLE_NAME, 
										DBContract.DealtBlackCard.COLUMN_NAME_GAME_ID + " = ?",
										new String[]{String.valueOf(game_id)});
	}
	
	public long addDealtBlackCard(long id, long game_id, long player_id, long black_card_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.DealtBlackCard._ID, id);
		values.put(DBContract.DealtBlackCard.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.DealtBlackCard.COLUMN_NAME_PLAYER_ID, player_id);
		values.put(DBContract.DealtBlackCard.COLUMN_NAME_BLACK_CARD_ID, black_card_id);

		return insertIgnoreOverwrite(DBContract.DealtBlackCard.TABLE_NAME, DBContract.DealtBlackCard._ID, values);
	}
	
	public long addUser(String username) {
		ContentValues values = new ContentValues();
		values.put(DBContract.User.COLUMN_NAME_USERNAME, username);
		return insertIgnoreOverwrite(DBContract.User.TABLE_NAME, DBContract.User._ID, values);
	}
	
	public long addUser(long id, String username) {
		ContentValues values = new ContentValues();
		values.put(DBContract.User._ID, id);
		values.put(DBContract.User.COLUMN_NAME_USERNAME, username);
		return insertIgnoreOverwrite(DBContract.User.TABLE_NAME, DBContract.User._ID, values);
	}
	
	public long addGame(int roundCap, int scoreCap) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Game.COLUMN_NAME_ROUND_CAP, roundCap);
		values.put(DBContract.Game.COLUMN_NAME_SCORE_CAP, scoreCap);
		return insertIgnoreOverwrite(DBContract.Game.TABLE_NAME, DBContract.Game._ID, values);
	}
	
	public long addGame(int id, int roundCap, int scoreCap) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Game._ID, id);
		values.put(DBContract.Game.COLUMN_NAME_ROUND_CAP, roundCap);
		values.put(DBContract.Game.COLUMN_NAME_SCORE_CAP, scoreCap);
		return insertIgnoreOverwrite(DBContract.Game.TABLE_NAME, DBContract.Game._ID, values);
	}
	
	public long addParticipation(long game_id, long user_id, int score) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Participation.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Participation.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.Participation.COLUMN_NAME_SCORE, score);
		return insertIgnoreOverwrite(DBContract.Participation.TABLE_NAME, DBContract.Participation._ID, values);
	}
	
	public long addParticipation(long id, long game_id, long user_id, int score) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Participation._ID, id);
		values.put(DBContract.Participation.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Participation.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.Participation.COLUMN_NAME_SCORE, score);
		return insertIgnoreOverwrite(DBContract.Participation.TABLE_NAME, DBContract.Participation._ID, values);
	}
	
	public long addTurn(long game_id, int roundnumber, long user_id, Integer black_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Turn.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Turn.COLUMN_NAME_ROUNDNUMBER, roundnumber);
		values.put(DBContract.Turn.COLUMN_NAME_USER_ID, user_id);
		if (black_id != null)
			values.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, black_id);
		
		//as card IDs start with 1, we can set 0 instead of null. this allows us
		//to check the values with =? syntax, instead of IS NULL. 
		
		return insertIgnoreOverwrite(DBContract.Turn.TABLE_NAME, DBContract.Turn._ID, values);
	}
	
	public long addTurn(long id, long game_id, int roundnumber, long user_id, Integer black_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Turn._ID, id);
		values.put(DBContract.Turn.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Turn.COLUMN_NAME_ROUNDNUMBER, roundnumber);
		values.put(DBContract.Turn.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, black_id);
		return insertIgnoreOverwrite(DBContract.Turn.TABLE_NAME, DBContract.Turn._ID, values);
	
	}
	
	public void updateTurn(long id, long game_id, int roundnumber, long user_id, Integer black_id) {
		ContentValues values = new ContentValues();
		values.put(DBContract.Turn.COLUMN_NAME_GAME_ID, game_id);
		values.put(DBContract.Turn.COLUMN_NAME_ROUNDNUMBER, roundnumber);
		values.put(DBContract.Turn.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, black_id);
		db.getWritableDatabase().update(DBContract.Turn.TABLE_NAME, values, 
									  	DBContract.Turn._ID + " = ?", 
									  	new String[]{String.valueOf(id)});
	}
	
	
	public long addPlayedWhiteCard(long turn_id, long user_id, long white_card_id, Boolean won) {
		ContentValues values = new ContentValues();
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID, turn_id);
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID, user_id);
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID, white_card_id);
		if (won != null)
			values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WON, won);
		
		return insertIgnoreOverwrite(DBContract.PlayedWhiteCard.TABLE_NAME, DBContract.PlayedWhiteCard._ID, values);
	}
	
	public boolean setBlackCardID(long turn_id, int cardIndex) {

	    ContentValues args = new ContentValues();
	    args.put(DBContract.Turn.COLUMN_NAME_BLACK_CARD_ID, cardIndex);
	    
	    int affected = db.getWritableDatabase().update(
	    		DBContract.Turn.TABLE_NAME, 
	    		args, 
	    		DBContract.Turn._ID + "=" + turn_id, 
	    		null);
	    
	    return (affected > 0);
	}
	
	public boolean setWhiteCardID(long turn_id, long user_id, int cardIndex) {

	    ContentValues values = new ContentValues();
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID, turn_id);
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_USER_ID, user_id);
	    values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID, cardIndex);
	 
	    // Inserting Row
	    long ret = insertIgnoreOverwrite(DBContract.PlayedWhiteCard.TABLE_NAME, DBContract.PlayedWhiteCard._ID, values);
	    
	    return (ret != -1);
	}

	/*
	 * UPDATE QUERIES
	 * 
	 */	
	public void updatePlayedWhiteCard(long turn_id, int chosen_card) {
		ContentValues values = new ContentValues();
		values.put(DBContract.PlayedWhiteCard.COLUMN_NAME_WON, true);
		
		//TODO: everything else is still null... change?
		
		db.getWritableDatabase().update(
				DBContract.PlayedWhiteCard.TABLE_NAME, values,
				DBContract.PlayedWhiteCard.COLUMN_NAME_TURN_ID +"=? AND "+
				DBContract.PlayedWhiteCard.COLUMN_NAME_WHITE_CARD_ID + "=?",
				new String[] {String.valueOf(turn_id),String.valueOf(chosen_card)});

	}
	
	/**
	 * 
	 * @param turn_id
	 * @return success
	 */
	public boolean updateScores(long turn_id) {
		
		/**
		 * get the game id of the turn as well as the user id of the card that won
		 */
	    Cursor cursor = db.getReadableDatabase().rawQuery(
	    		"SELECT g.game_id, g.user_id FROM participation g "+
	    		"INNER JOIN turn t ON t.game_id=g.game_id " +
	    		"INNER JOIN played_white_card p ON " +
	    			"t._id=p.turn_id AND g.user_id=p.user_id " +
	    		"WHERE t._id=? AND p.won"
	    		,new String[]{String.valueOf(turn_id)});
	    
		long game_id = -1;
		long user_id = -1;
	    	    
	    if (cursor != null)
	    {
		    if(cursor.moveToFirst() && cursor.getColumnCount() > 1)
		    {
			    game_id = cursor.getLong(0);
			    user_id = cursor.getLong(1);
		    }
		    cursor.close();    
	    }
		
	    if (game_id < 1 || user_id < 1)
	    	return false;
	    
	    /**
	     * get the old score of the player
	     */
	    long score = db.getter.getScore(game_id, user_id);
				
	    if (score < 0)
	    	return false;
	    
	    
	    /**
	     * update the old score by an increment
	     */
		ContentValues values = new ContentValues();
		values.put(DBContract.Participation.COLUMN_NAME_SCORE,
				(score + NUM_POINTS_INCR));
		
		int affected = db.getWritableDatabase().update(
				DBContract.Participation.TABLE_NAME, 
				values, 
				DBContract.Participation.COLUMN_NAME_GAME_ID + "=? AND " +
				DBContract.Participation.COLUMN_NAME_USER_ID + "=?",
				new String[] {
						String.valueOf(game_id), 
						String.valueOf(user_id)});
				
		return (affected > 0);		
	}
	
	
	/**
	 * 
	 * @return the pk of the column that was inserted, or the column that matches the description
	 */
	private long insertIgnoreOverwrite(String tblName, String idCol,  ContentValues values)
	{	
		long id = -1;
		try {			
			id = db.getWritableDatabase().insertOrThrow(tblName, null, values);

		} catch (android.database.SQLException exception)
		{
			
			String query = "";
			String[] args = new String[values.size()];
						
			int count = 0;
			
			for (Entry<String, Object> e : values.valueSet())
			{
				if (count > 0 && count < values.size())
					query += " AND ";

				query += e.getKey() + "=?";
				args[count++] = String.valueOf(e.getValue());
			}

			//if at all possible, update the values to the new (FOR TESTING; TODO)
			db.getWritableDatabase().update(tblName, values, query, args);
			
			Cursor c = db.getWritableDatabase().query(tblName, new String[] { idCol }, 
					query, args, null,null,null);
			
		    if (c != null)
		    {
		    	if (c.moveToFirst())
				    id = c.getInt(0);
		    	
			    c.close();    
		    }
		}
		
		return id;
	}	

	
	
	/**
	 * old preset setting method
	 */
	/*
	 * DEBUG QUERIES
	 */
	public void setPreset(int preset) {
		long user_1, user_2, user_3, user_4;
		long game_1, game_2, game_3, game_4;
		long turn_1, turn_2, turn_3, turn_4;		
		
		//retrieve username
		String username = db.getUsername();
		
		switch(preset) {
		case PresetHelper.NO_GAMES:
			//1 user, no games
			addUser(username);
			break;
			
		case PresetHelper.SELECT_BLACK:
						
			//3 users, 1 game, 2 rounds, user has to choose black card
			//table user: add local user
				user_1 = addUser(username);
				user_2 = addUser("user2@dummy.com");
				user_3 = addUser("user3@dummy.com");
			//table game: add game
				game_1 = addGame(5, 0);
			//table participation
				addParticipation(game_1, user_1, 0);
				addParticipation(game_1, user_2, 0);
				addParticipation(game_1, user_3, 1);
			//table turn
				turn_1 = addTurn(game_1, 1, user_1, 1);
				turn_2 = addTurn(game_1, 2, user_1, null);
			//table playedWhiteCards
				//turn1, player2
				addPlayedWhiteCard(turn_1, user_2, 11, null);
				//turn1, player3
				addPlayedWhiteCard(turn_1, user_3, 12, null);
			break;
		case PresetHelper.SELECT_WHITE:
			//3 users, 1 game, 2 rounds, user has to choose black card
			//table user: add local user
				user_1 = addUser(username);
				user_2 = addUser("user2@dummy.com");
				user_3 = addUser("user3@dummy.com");
			//table game: add game
				game_1 = addGame(5, 0);
			//table participation
				addParticipation(game_1, user_1, 0);
				addParticipation(game_1, user_2, 1);
				addParticipation(game_1, user_3, 0);
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
			break;
		}
	}
	

	
}
