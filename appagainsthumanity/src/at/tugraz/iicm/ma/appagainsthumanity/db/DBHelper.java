package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "AppAgainstHumanity.db";
    public static final String TEST_DB_NAME = 
    		"/home/egetzner/appagainsthumanity/HumanityTest/testdata/test.db";
    
    
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public DBHelper(Context c, String string, CursorFactory object,
			int currentDbVersion) {
        super(c, TEST_DB_NAME, object, currentDbVersion);
	}

    @Override
	public void onCreate(SQLiteDatabase db) {
    	
    	try{
        	for (String query : DBContract.SQL_CREATE_ENTRIES) 
        		db.execSQL(query);
    	} catch (Exception e)
    	{
    		//e.printStackTrace();
    		//reinitialize(db);
    	}
    	
    	addDefaultEntries(db);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
    	for (String query : DBContract.SQL_DELETE_ENTRIES)
    		db.execSQL(query);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    private void addDefaultEntries(SQLiteDatabase db) {
    	ContentValues values = new ContentValues();
    	values.put(DBContract.User.COLUMN_NAME_USERNAME, "PLAYED_CARDS");
    	db.insert(DBContract.User.TABLE_NAME, null, values);
    }
    
    public void reinitialize(SQLiteDatabase db) {
    	for (String query : DBContract.SQL_DELETE_ENTRIES)
    		db.execSQL(query);
    	for (String query : DBContract.SQL_CREATE_ENTRIES) 
    		db.execSQL(query);
    	addDefaultEntries(db);
    }
    
    
    
    
}
