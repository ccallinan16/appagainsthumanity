package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;

public class TurnlistAdapter extends CursorAdapter {

	private final LayoutInflater inflater;
	private String username;
	private OnClickListener black;
	private OnClickListener white;
	private OnClickListener winning;
	private OnClickListener result;
	
	//other constructors would need api level 11 - our base-level is 8
	@SuppressWarnings("deprecation") 
	public TurnlistAdapter(Context context, Cursor c, String username, OnClickListener chooseBlackCardListener, OnClickListener chooseWhiteCardListener, OnClickListener chooseWinningCardListener, OnClickListener showResultListener) {
		super(context, c);
		inflater=LayoutInflater.from(context);
		this.username = username;
		this.black = chooseBlackCardListener;
		this.white = chooseWhiteCardListener;
		this.winning = chooseWinningCardListener;
		this.result = showResultListener;
	}

	@Override
	public void bindView(View view, final Context context, Cursor c) {
		//retrieve views
		TextView round = (TextView)view.findViewById(R.id.tv_round);
		TextView status = (TextView)view.findViewById(R.id.tv_status);
		ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
		
		//set roundnumber
		round.setText("Round " + c.getString(1));
		
		if (username.equals(c.getString(3)) && c.getLong(4) == 0) {
			//case: choose black card
			thumbnail.setImageResource(R.drawable.card_black);
			status.setText(context.getString(R.string.game_overview_turns_text_chooseblack));
			view.setOnClickListener(black);
		} else if (!username.equals(c.getString(3)) && c.getInt(5) < (c.getInt(2) - 1)) {
			//choose white card
			thumbnail.setImageResource(R.drawable.card_white);
			status.setText(context.getString(R.string.game_overview_turns_text_choosewhiteblack));
			view.setOnClickListener(white);
		} else if (username.equals(c.getString(3)) && c.getInt(5) == (c.getInt(2) - 1) && c.getString(6).equals(null)) {
			//choose winning card
			thumbnail.setImageResource(R.drawable.winner);
			status.setText(context.getString(R.string.game_overview_turns_text_choosewinner));
			view.setOnClickListener(winning);
		} else if (!c.getString(6).equals(null)) {
			//show result
			if (c.getString(6).equals(username)) {
				thumbnail.setImageResource(R.drawable.star);
				status.setText(context.getString(R.string.game_overview_turns_text_youwon));
			}
			else {
				thumbnail.setImageResource(R.drawable.star_empty);
				status.setText(context.getString(R.string.game_overview_turns_text_winner) + ": " + c.getString(6));
			}
			view.setOnClickListener(result);
		} else {
			thumbnail.setImageResource(R.drawable.time);
			status.setText(context.getString(R.string.game_overview_turns_text_wait));
			thumbnail.setEnabled(false);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.listitem_turnlist,  parent, false);
	}
	
	public boolean simulateClick(ViewContext context)
	{
		switch(context)
		{
		case SELECT_BLACK:
			black.onClick(null);
			break;
		case SELECT_WHITE:
			white.onClick(null);
			break;			
		}
		return false;
	}
	
//	public View getView(int position, View convertView, ViewGroup parent) {
//	    if (!mDataValid) {
//	        throw new IllegalStateException("this should only be called when the cursor is valid");
//	    }
//	    if (!mCursor.moveToPosition(position)) {
//	        throw new IllegalStateException("couldn't move cursor to position " + position);
//	    }
//	    View v;
//	    if (convertView == null) {
//	        v = newView(mContext, mCursor, parent);
//	    } else {
//	        v = convertView;
//	    }
//	    bindView(v, mContext, mCursor);
//	    return v;
//	}

}
