package at.tugraz.iicm.ma.appagainsthumanity.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class SingleCardFragment extends Fragment {

	 protected static final String ID = "CARD_ID";
	 protected static final String TEXTSIZE = "TEXT_SIZE";
 	
	 protected CardType TYPE;
	     
	public static SingleCardFragment newInstance(int cardID, CardType type, float textSize, boolean selectable) {
		
		   SingleCardFragment f = (selectable)?new SelectableCardFragment():new SingleCardFragment();
		   f.TYPE = type;
		   Bundle bdl = new Bundle();
		   bdl.putInt(ID, cardID);
		   bdl.putFloat(TEXTSIZE, textSize);
		   f.setArguments(bdl);
		   
		   return f;
		}    

	
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
			 Bundle savedInstanceState) {

		 int cardId = getArguments().getInt(ID);
		 View v = inflater.inflate(R.layout.single_card_view, container, false);

		 Card card = CardCollection.instance.getCard(cardId, TYPE);

		 if (card == null)
			 System.err.println("card id: " + cardId);

		 float textSize = getArguments().getFloat(TEXTSIZE);

		 TextView messageTextView = (TextView)v.findViewById(R.id.cardText);
		 messageTextView.setText(card.getText());
		 messageTextView.setTextSize(textSize); 
		 messageTextView.setTextColor(card.getType().getTextColor());

		 v.setBackgroundColor(card.getType().getBGColor());

		 return v;
	 }
}
