package at.tugraz.iicm.ma.appagainsthumanity.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "AppAgainstHumanity.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
    	for (String query : DBContract.SQL_CREATE_ENTRIES) 
    		db.execSQL(query);
    	for (String query : DBContract.SQL_DEFAULT_ENTRIES) 
    		db.execSQL(query);
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
    

}
