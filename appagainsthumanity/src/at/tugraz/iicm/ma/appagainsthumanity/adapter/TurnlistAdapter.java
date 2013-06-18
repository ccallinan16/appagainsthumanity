package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.ChooseViewListener;
import at.tugraz.iicm.ma.appagainsthumanity.R;

public class TurnlistAdapter extends CursorAdapter {

	private final LayoutInflater inflater;
	private String username;
	private Activity activity;
	private OnClickListener black;
	private OnClickListener white;
	private OnClickListener winning;
	private OnClickListener result;
	
	//other constructors would need api level 11 - our base-level is 8
	@SuppressWarnings("deprecation") 
	public TurnlistAdapter(Activity activity, Cursor c, String username) {
		super(activity, c);
		inflater=LayoutInflater.from(activity);
		this.activity = activity;
		this.username = username;
		this.black = new ChooseViewListener(activity, ViewContext.SELECT_BLACK,0);
		this.white = new ChooseViewListener(activity, ViewContext.SELECT_WHITE,0);
		this.winning = new ChooseViewListener(activity, ViewContext.SELECT_WHITE,0);
		this.result = new ChooseViewListener(activity, ViewContext.SHOW_RESULT,0);
	}

	@Override
	public void bindView(View view, final Context context, Cursor c) {
		DatabaseUtils.dumpCursor(c);
		
		//retrieve views
		TextView round = (TextView)view.findViewById(R.id.tv_round);
		TextView status = (TextView)view.findViewById(R.id.tv_status);
		ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
		ImageView arrow = (ImageView)view.findViewById(R.id.arrow);
		
		//set roundnumber
		round.setText("Round " + c.getString(1));
		
		//set arrow to visible if it was hidden before
		arrow.setVisibility(View.VISIBLE);
		
		if (username.equals(c.getString(3)) && c.getLong(4) == 0) {
			//case: choose black card
			thumbnail.setImageResource(R.drawable.card_black);
			status.setText(context.getString(R.string.game_overview_turns_text_chooseblack));
			view.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_BLACK,c.getLong(0)));
		} else if (!username.equals(c.getString(3)) && c.getInt(5) < (c.getInt(2) - 1) && c.getLong(4) != 0) {
			//choose white card
			thumbnail.setImageResource(R.drawable.card_white);
			status.setText(context.getString(R.string.game_overview_turns_text_choosewhiteblack));
			view.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_WHITE,c.getLong(0)));
		} else if (username.equals(c.getString(3)) && c.getInt(5) == (c.getInt(2) - 1) && c.getInt(7) == 0) {
			//choose winning card
			thumbnail.setImageResource(R.drawable.winner);
			status.setText(context.getString(R.string.game_overview_turns_text_choosewinner));
			view.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SELECT_WINNER,c.getLong(0)));
		} else if (c.getInt(7) != 0) {
			//show result
			if (c.getString(6).equals(username)) {
				thumbnail.setImageResource(R.drawable.star);
				status.setText(context.getString(R.string.game_overview_turns_text_youwon));
			}
			else {
				thumbnail.setImageResource(R.drawable.star_empty);
				status.setText(context.getString(R.string.game_overview_turns_text_winner) + ": " + c.getString(6));
			}
			view.setOnClickListener(
					new ChooseViewListener(activity, ViewContext.SHOW_RESULT,c.getLong(0)));
		} else {
			//case: wait for other player action
			thumbnail.setImageResource(R.drawable.time);
			status.setText(context.getString(R.string.game_overview_turns_text_wait));
			//hide arrow
			arrow.setVisibility(View.INVISIBLE);
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
