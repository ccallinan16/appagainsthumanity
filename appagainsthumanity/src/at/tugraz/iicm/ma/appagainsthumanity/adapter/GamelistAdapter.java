package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;

public class GamelistAdapter extends CursorAdapter {

	private Cursor cursor;
	private final LayoutInflater inflater;
	private OnClickListener black;
	private OnClickListener white;
	private OnClickListener winning;

	//other constructors would need api level 11 - our base-level is 8
	@SuppressWarnings("deprecation") 
	public GamelistAdapter(Context context, Cursor c, OnClickListener chooseBlackCardListener, OnClickListener chooseWhiteCardListener, OnClickListener chooseWinningCardListener) {
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
		ImageView image = (ImageView)view.findViewById(R.id.list_image);
		LinearLayout thumbnail = (LinearLayout)view.findViewById(R.id.thumbnail);
		//case: choose black card
		if (c.getLong(6) == c.getLong(7) && c.getLong(8) == 0) {
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_black));
			thumbnail.setBackgroundResource(R.drawable.list_selector);
			thumbnail.setOnClickListener(black);
		} else if (c.getLong(6) != c.getLong(7) && c.getInt(9) < (c.getInt(5) - 1)) {
			//choose white card
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_white));
			thumbnail.setBackgroundResource(R.drawable.list_selector);
			thumbnail.setOnClickListener(white);
		} else if (c.getLong(6) == c.getLong(7) && c.getInt(9) == (c.getInt(5) - 1)) {
			//choose winning card
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
			thumbnail.setBackgroundResource(R.drawable.list_selector);
			thumbnail.setOnClickListener(winning);
		} else 
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.time));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.listitem_gamelist,  parent, false);
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
