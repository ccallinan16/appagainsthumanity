package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.ChooseViewListener;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;

public class GamelistAdapter extends CursorAdapter {

	private Activity activity;
	private final LayoutInflater inflater;
	private ChooseViewListener black;
	private ChooseViewListener white;
	private ChooseViewListener winning;

	//other constructors would need api level 11 - our base-level is 8
	@SuppressWarnings("deprecation") 
	public GamelistAdapter( Activity activity, Cursor c) {
		super(activity.getApplicationContext(), c);
				
		this.activity = activity;
		//cursor = c;
		inflater=LayoutInflater.from(activity.getApplicationContext());
		this.black = new ChooseViewListener(activity, ViewContext.SELECT_BLACK,0);
		this.white = new ChooseViewListener(activity, ViewContext.SELECT_WHITE,0);
		this.winning = new ChooseViewListener(activity, ViewContext.SELECT_WHITE,0);
	}

	@Override
	public void bindView(View view, final Context context, Cursor c) {
		//TextView round
		TextView round = (TextView)view.findViewById(R.id.tvRound);
		round.setText("round: " + c.getString(1) + (c.getInt(2) > 0 ? " / " + c.getString(2) : ""));
		//TextView score
		TextView score = (TextView)view.findViewById(R.id.tvScore);
		score.setText("score: " + c.getString(3) + (c.getInt(4) > 0 ? " / " + c.getString(4) : ""));
		//TextView numplayers
		TextView numplayers = (TextView)view.findViewById(R.id.tvNumplayers);
		numplayers.setText(c.getString(5) + " players");
		//ImageView thumbnail
		ImageButton thumbnail = (ImageButton)view.findViewById(R.id.thumbnail);
		//set focusable to false - otherwise listitem won't be clickable
		thumbnail.setFocusable(false);
		
		long turn = c.getLong(10);
		
		//case: choose black card
		if (c.getLong(6) == c.getLong(7) && c.getLong(8) == 0) {
			thumbnail.setImageResource(R.drawable.card_black);
			thumbnail.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_BLACK,turn));
		} else if (c.getLong(6) != c.getLong(7) && c.getInt(9) < (c.getInt(5) - 1) && c.getLong(8) != 0 && c.getLong(11) == 0) {
			//choose white card
			thumbnail.setImageResource(R.drawable.card_white);
			thumbnail.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_WHITE,turn));
		} else if (c.getLong(6) == c.getLong(7) && c.getInt(9) == (c.getInt(5) - 1)) {
			//choose winning card
			thumbnail.setImageResource(R.drawable.winner);
			thumbnail.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_WINNER,turn));
		} else {
			thumbnail.setImageResource(R.drawable.time);
			thumbnail.setEnabled(false);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.listitem_gamelist,  parent, false);
	}
	
	public boolean simulateClick(ViewContext context)
	{
		System.out.println("sim click: " + context + ", last: " + PresetHelper.man.getLastTurnID());
		switch(context)
		{
		case SELECT_BLACK:
			black.setTurnID(PresetHelper.man.getLastTurnID());
			black.onClick(null);
			break;
		case SELECT_WHITE:
			white.setTurnID(PresetHelper.man.getLastTurnID());
			white.onClick(null);
			break;
		}
		return false;
	}
}
