package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class SingleCardFragment extends Fragment {

	private static final float MAX_SIZE = 28f;
	private static final float MIN_SIZE = 12f;

	
	 protected static final String ID = "CARD_ID";
	 protected static final String TEXTSIZE = "TEXT_SIZE";
	 protected static final String SELECTABLE = "SELECTABLE";

	 protected ViewContext context;
	 private long turnID;
	 private boolean isWinner;
	     
	public static SingleCardFragment newInstance(int cardID, ViewContext type, boolean selectable, long turnID, boolean isWinner) {
		
		   SingleCardFragment f = new SingleCardFragment();
		   f.context = type;
		   f.turnID = turnID;
		   f.isWinner = isWinner;
		   Bundle bdl = new Bundle();
		   bdl.putInt(ID, cardID);
		   bdl.putBoolean(SELECTABLE,selectable);
		   f.setArguments(bdl);
		   
		   return f;
		}    

	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
			 Bundle savedInstanceState) {

		 int cardId = getArguments().getInt(ID);
		 View v = inflater.inflate(R.layout.single_card_view, container, false);

		 //TODO: replace all occurences of this, as we only want to do this once!
		 //IDToCardTranslator translator = new IDToCardTranslator(this.getActivity());
		 
		 Card card = CardCollection.instance.getCardSafe(cardId,context.getCardType());
		 
		 TextView messageTextView = (TextView)v.findViewById(R.id.cardText);
		 
		 System.out.println(card.getId() + " " + card.getText());
		 
		 messageTextView.setText(card.getText());
		 messageTextView.setTextAppearance(v.getContext(), card.getTextAppearance());
		 
		 
		 if (card.getType().equals(CardType.BLACK))
			 messageTextView.setBackgroundResource(R.drawable.border_black_final);
		 else
			 messageTextView.setBackgroundResource(R.drawable.border_white_final);

		 //messageTextView.setTextColor(card.getType().getTextColor());
		 		 
		 //messageTextView.setBackground(getResources().getDrawable(R.drawable.border_white2));
		 
		 if (!isWinner)
		 {
			 View winner = v.findViewById(R.id.winnerImage);
			 winner.setVisibility(View.INVISIBLE);
		 }
		 
		 
		 if (getArguments().getBoolean(SELECTABLE))
		 {
			 v.setOnClickListener(new OnCardSelectionListener(context,turnID));
		 }
		 
		 messageTextView.setTextSize(card.getRelativeTextSize(MAX_SIZE, MIN_SIZE));
		 
		// v.setBackgroundColor(card.getType().getBGColor());
		 //v.setBackground(getResources().getDrawable(R.drawable.gradient_bg_hover));
		 
		 return v;
	 }
	 
		void adjustTextScale(TextView t, float max, float providedWidth, float providedHeight) {
			
		    // sometimes width and height are undefined (0 here), so if something was provided, take it ;-)
		    if (providedWidth == 0f)
		        providedWidth = ((float) (t.getWidth()-t.getPaddingLeft()-t.getPaddingRight()));
		    if (providedHeight == 0f)
		        providedHeight = ((float) (t.getHeight()-t.getPaddingTop()-t.getPaddingLeft()));
		
		    float pix = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
		    		1, t.getResources().getDisplayMetrics());
		
		    String[] lines = t.getText().toString().split("\\r?\\n");
		
		    // ask paint for the bounding rect if it were to draw the text at current size
		    Paint p = new Paint();
		    p.setTextScaleX(1.0f);
		    p.setTextSize(t.getTextSize());
		    Rect bounds = new Rect();
		    float usedWidth = 0f;
		
		    // determine how much to scale the width to fit the view
		    for (int i =0;i<lines.length;i++){
		        p.getTextBounds(lines[i], 0, lines[i].length(), bounds);
		        usedWidth = Math.max(usedWidth,(bounds.right - bounds.left)*pix);
		    }
		
		    // same for height, sometimes the calculated height is to less, so use ����{ instead
		    p.getTextBounds("����{", 0, 3, bounds);
		    float usedHeight = (bounds.bottom - bounds.top)*pix*lines.length;
		
		    float scaleX = providedWidth / usedWidth;
		    float scaleY = providedHeight / usedHeight;
		
		    t.setTextSize(TypedValue.COMPLEX_UNIT_PX,t.getTextSize()*Math.min(max,Math.min(scaleX,scaleY)));

		}

	 
}
