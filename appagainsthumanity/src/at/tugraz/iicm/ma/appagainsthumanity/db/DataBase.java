package at.tugraz.iicm.ma.appagainsthumanity.db;

import android.content.Context;
import android.content.Intent;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class DataBase {

	DBProxy dbProxy;
	
	public DataBase(Context context)
	{
		this.dbProxy = new DBProxy(context);
	}
	
	public void setPreset(int id)
	{
		this.dbProxy.setPreset(id);
	}
	
	private boolean setSelectedCardID(CardType type, int id) {
				
		if (type.equals(CardType.BLACK))
			return dbProxy.setBlackCardID(dbProxy.getLastTurnID(),id);
		else
			return dbProxy.setWhiteCardID(dbProxy.getLastTurnID(), id);
	}

	public int getSelectedCardID(CardType type) {
		
		int id = dbProxy.getLastTurnID();
				
		if (id == -1)
			return id;
		
		if (type.equals(CardType.BLACK))
			return dbProxy.getBlackCard(id);
		else
			return dbProxy.getPlayedWhiteCard( id);
	}

}
