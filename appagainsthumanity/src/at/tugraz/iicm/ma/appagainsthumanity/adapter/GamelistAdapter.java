package at.tugraz.iicm.ma.appagainsthumanity.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
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
		round.setText("round: " + c.getString(1) + (c.getInt(3) == 1 ? " / " + c.getString(2) : ""));
		//TextView score
		TextView score = (TextView)view.findViewById(R.id.tvScore);
		score.setText("score: " + c.getString(4) + (c.getInt(6) == 1 ? " / " + c.getString(5) : ""));
		//TextView numplayers
		TextView numplayers = (TextView)view.findViewById(R.id.tvNumplayers);
		numplayers.setText(c.getString(7) + " players");
		//ImageView thumbnail
		ImageView image = (ImageView)view.findViewById(R.id.list_image);
		//case: choose black card
		if (c.getLong(8) == c.getLong(9) && c.getLong(10) == 0) {
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_black));
			image.setOnClickListener(black);
		} else if (c.getLong(8) != c.getLong(9) && c.getInt(11) < c.getInt(7)) {
			//choose white card
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.card_white));
			image.setOnClickListener(white);
		} else if (c.getLong(8) == c.getLong(9) && c.getInt(11) == c.getInt(7)) {
			//choose winning card
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
			image.setOnClickListener(winning);
		} else 
			image.setImageDrawable(context.getResources().getDrawable(R.drawable.time));
		
		
		
		//c.get
	//	cursor.getString(columnIndex)
		
		//		TextView mobileNo=(TextView)view.findViewById(R.id.mobileNolistitem);
//        mobileNo.setText(cursor.getString(cursor.getColumnIndex(TextMeDBAdapter.KEY_MOBILENO)));
//
//        TextView frequency=(TextView)view.findViewById(R.id.frequencylistitem);
//        frequency.setText(cursor.getString(cursor.getColumnIndex(TextMeDBAdapter.KEY_FREQUENCY)));
//
//        TextView rowid=(TextView)view.findViewById(R.id.rowidlistitem);
//        rowid.setText(cursor.getString(cursor.getColumnIndex(TextMeDBAdapter.KEY_ID)));

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
