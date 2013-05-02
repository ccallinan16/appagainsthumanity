package at.tugraz.iicm.ma.appagainsthumanity.gui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class SingleCardView extends Fragment {

	 public static final String TEXT = "CARD_TEXT";
	 public static final String BGCOLOR = "BG_COLOR";
	 public static final String TEXTSIZE = "TEXT_SIZE";

	 boolean onCreateViewCalled = false;
	 	
	public static SingleCardView newInstance(Card card, float textSize) {
	   SingleCardView f = new SingleCardView();
	   Bundle bdl = new Bundle(3);
	   bdl.putString(TEXT, card.getText());
	   bdl.putInt(BGCOLOR, card.getBGColor());
	   bdl.putFloat(TEXTSIZE, textSize);

	   f.setArguments(bdl);
	   
	   return f;
	}

	public static List<Fragment> getFragmentFromCards(List<Card> cards, float textSize) {
  	  List<Fragment> fList = new ArrayList<Fragment>();

  	  for (Card card : cards)
  		  fList.add(SingleCardView.newInstance(card, textSize));
  	  
  	  return fList;
	}
	 
    public static final List<Fragment> getSCVFragments(CardType type, String[] strings) {
    	
    	  List<Fragment> fList = new ArrayList<Fragment>();

    	  for (String text : strings)
    		  fList.add(SingleCardView.newInstance(new Card(text,0,type), 50f));
    	  
    	  return fList;
    }
	 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		 onCreateViewCalled = true;
	   String message = getArguments().getString(TEXT);
	   int bgColor = getArguments().getInt(BGCOLOR);
	   float textSize = getArguments().getFloat(TEXTSIZE);
	   
	   View v = inflater.inflate(R.layout.single_card_view, container, false);
	   TextView messageTextView = (TextView)v.findViewById(R.id.cardText);
	   messageTextView.setText(message);
	   messageTextView.setTextSize(textSize);
	   int textColor = (bgColor == Color.BLACK)?Color.WHITE:Color.BLACK;
	   messageTextView.setTextColor(textColor);
	   
	   v.setBackgroundColor(bgColor);
	   
	   return v;
	 }

	 
	 /**
	  * for testing purposes only
	  * @return
	  */
	 public String getText()
	 {
		 
		 return getArguments().getString(TEXT);
	 }

	 /**
	  * for testing purposes only
	  * @return
	  */
	public float getTextSize() {
		return getArguments().getFloat(TEXTSIZE);
	}



	
}
