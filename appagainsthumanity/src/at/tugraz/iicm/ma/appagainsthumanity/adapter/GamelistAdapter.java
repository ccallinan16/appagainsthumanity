package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.ChooseViewListener;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

public class GamelistAdapter extends CursorAdapter {

	private Cursor cursor;
	private final LayoutInflater inflater;
	private ChooseViewListener black;
	private ChooseViewListener white;
	private ChooseViewListener winning;

	//other constructors would need api level 11 - our base-level is 8
	@SuppressWarnings("deprecation") 
	public GamelistAdapter(Context context, Cursor c, 
			ChooseViewListener chooseBlackCardListener, 
			ChooseViewListener chooseWhiteCardListener, 
			ChooseViewListener chooseWinningCardListener) {
		super(context, c);
		cursor = c;
		inflater=LayoutInflater.from(context);
		this.black = chooseBlackCardListener;
		this.white = chooseWhiteCardListener;
		this.winning = chooseWinningCardListener;
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
		//case: choose black card
		if (c.getLong(6) == c.getLong(7) && c.getLong(8) == 0) {
			thumbnail.setImageResource(R.drawable.card_black);
			black.setTurnID(c.getLong(10));
			thumbnail.setOnClickListener(black);
		} else if (c.getLong(6) != c.getLong(7) && c.getInt(9) < (c.getInt(5) - 1)) {
			//choose white card
			thumbnail.setImageResource(R.drawable.card_white);
			white.setTurnID(c.getLong(10));
			thumbnail.setOnClickListener(white);
		} else if (c.getLong(6) == c.getLong(7) && c.getInt(9) == (c.getInt(5) - 1)) {
			//choose winning card
			thumbnail.setImageResource(R.drawable.winner);
			winning.setTurnID(c.getLong(10));
			thumbnail.setOnClickListener(winning);
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
