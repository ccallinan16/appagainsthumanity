package test.util;

import java.sql.ResultSet;
import com.xtremelabs.robolectric.util.DatabaseConfig.DatabaseMap;

public class SQLiteMap implements DatabaseMap {

    private String _dbFile;
	

    /**
     * This constructor will use in-memory database.
     */
    public SQLiteMap() {}
    
    
    /**
     * This constructor will use a database file
     *
     * @param dbFile: path to the SQLite database file
     */
    public SQLiteMap(String dbFile) {
        _dbFile = dbFile;
    }

	@Override
    public String getDriverClassName() {
        return "org.sqlite.JDBC";
    }

	@Override
    public String getConnectionString() {
        if (_dbFile == null)
            return "jdbc:sqlite::memory:";
        else
            return String.format("jdbc:sqlite:%s", _dbFile);
    
		}

	@Override
    public String getScrubSQL(String sql) {
        return sql;
    }

	@Override
    public String getSelectLastInsertIdentity() {
        return "SELECT last_insert_rowid() AS id";
    }

	@Override
    public int getResultSetType() {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

}