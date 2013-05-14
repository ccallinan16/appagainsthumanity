package at.tugraz.iicm.ma.appagainsthumanity;


import java.util.ArrayList;
import java.util.List;

import mocks.IDToCardTranslator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.ServerConnector;
import at.tugraz.iicm.ma.appagainsthumanity.gui.OnCardSelectionListener;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardSlideActivity extends FragmentActivity {
	
	private ViewContext context = ViewContext.UNKNOWN;
	public int topCardId = -1;
	private long turnID = 0;
	
	DBProxy proxy;
	
	//to be used by test functions
	public void setProxy(DBProxy proxy)
	{
		this.proxy = proxy;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_screen_slide);
                  
      //we cannot create a view without a context.
	  if (getIntent() == null || getIntent().getExtras() == null)
		  return; 
	   
	  context = ViewContext.getContextFromString(
				  		  getIntent().getExtras()
				  		  .getString(BundleCreator.CONTEXT));
	  
	  turnID = getIntent().getExtras().getLong(BundleCreator.TURN_ID);
	  	  
	  if (		 context == ViewContext.CONFIRM_WHITE 
			  || context == ViewContext.CONFIRM_BLACK
			  || context == ViewContext.SHOW_RESULT)
	  {
			TextView lbl = (TextView) findViewById(R.id.cs_label);
			lbl.setVisibility(View.GONE);
	  }
	 	  
      initSlider();
      
      initTop();

      initButtons();
    }     
    
    @Override
    protected void onStop() {
    	
    	
    	try {
    		super.onStop();

    		if (this.proxy != null) {
    			this.proxy.onStop();
    			this.proxy = null;

    		}
    	} catch (Exception error) {
        /** Error Handler Code **/
    	}// end try/catch (Exception error)
    }
    	
	private void initButtons() {

		
		if (	context == ViewContext.SELECT_BLACK ||
				context == ViewContext.SELECT_WHITE ||
				context == ViewContext.SELECT_WINNER)
		{
		      LinearLayout btns = (LinearLayout) findViewById(R.id.footer);
		      btns.setVisibility(View.GONE);
		}
		else
		{
		      Button okButton = (Button) findViewById(R.id.okButton);
		      
		      switch (context)
		      {
		      case CONFIRM_WHITE:
		      case CONFIRM_BLACK:
		    	  okButton.setText(R.string.menu_send);
		    	  default:
		      }
		      		      	      
		      okButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					int id = CardCollection.instance.getSelectedID();
					
			  		Intent intent = new Intent(v.getContext(),MainActivity.class);
								
			  		if (proxy == null)
			  			proxy = new DBProxy(v.getContext());
			  		
					ServerConnector connector = new ServerConnector(proxy);
										
					switch (context)
					{
					case CONFIRM_BLACK:
						connector.selectCardBlack(turnID, id);
						break;
					case CONFIRM_WHITE:
						connector.selectCardWhite(turnID, id); 
						break;
					case CONFIRM_WINNER:
						connector.selectWinner(turnID, id);
						break;
					default:
						break;
					}
				
			    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				  	v.getContext().startActivity(intent);									
				}
			});

		}  
	}     

	protected void createAndStartMainActivity(View v) {
		Intent intent = new Intent(v.getContext(),MainActivity.class);
		//add flag to get back to main activity and clean intermediate activities
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
	}

	private void initSlider()
	{
		  boolean selectable = false;
		  
		  if (context == ViewContext.SELECT_BLACK || 
			  context == ViewContext.SELECT_WHITE ||
			  context == ViewContext.SELECT_WINNER)
			  selectable = true;
		  
		  if (proxy == null)
		  {
			  proxy = new DBProxy(this);		  
		  }
		  
		  ServerConnector serverConnector = new ServerConnector(proxy);
		  IDToCardTranslator dealer = new IDToCardTranslator(this);
		  
		  List<Integer> cardIDs = new ArrayList<Integer>();

		  //what if server connector returns an empty list?
		  
		  switch(context)
		  {
		  case SELECT_BLACK:
		  case SELECT_WHITE:
			  if (turnID > 0)
				  cardIDs = serverConnector.getDealtCards(dealer,context.getCardType(),turnID);

			  if (cardIDs == null) //obviously, we didn't get cards from the db
			  {
				  CardCollection.instance.setupContext(context, dealer);
				  cardIDs = CardCollection.instance.getCardsForPager(context);
			  }
			  break;
		  case SELECT_WINNER:
		  case SHOW_RESULT:
			  if (turnID > 0)
				  cardIDs = serverConnector.getPlayedCards(turnID);
			  break;
		  case CONFIRM_BLACK:
		  case CONFIRM_WHITE:
		  case CONFIRM_WINNER:
			  cardIDs.add(CardCollection.instance.getSelectedID());
			  break;
			  default:
		  }
		  		  
		  //TODO: do something else for confirm views
		  CardFragmentAdapter pageAdapter = new CardFragmentAdapter(
	    		  getSupportFragmentManager(),
	    		  cardIDs,
	    		  selectable,
	    		  context,
	    		  turnID
	    		  );
	      
	      ViewPager pager = (ViewPager) findViewById(R.id.cs_card_slider);
	      
	      //TODO: exception in Unittests, comment back in for nice effects
	      //pager.setPageTransformer(true, new ZoomOutPageTransformer());
	      pager.setAdapter(pageAdapter);
	      
	      pager.setOnClickListener(new OnCardSelectionListener(context,turnID));
	
	}
	
	
	private void initTop()
	{
		
		if (context == ViewContext.SELECT_BLACK ||
			context == ViewContext.CONFIRM_BLACK)
		{
			FrameLayout v = (FrameLayout) findViewById(R.id.cs_display_frame);
			v.setVisibility(View.GONE);
			return;
		}
		
		ServerConnector serverConnector = new ServerConnector(proxy);
		int blackID = serverConnector.getBlackCardForTurn(turnID);
		
		IDToCardTranslator dealer = new IDToCardTranslator(this);
		Card black = dealer.getCardFromID(CardType.BLACK, blackID);
		
		//for testing purposes only TODO
		if (black == null)
			black = CardCollection.instance.getBlackCard();
		
		if (black == null)
		{
			FrameLayout v = (FrameLayout) findViewById(R.id.cs_display_frame);
			v.setVisibility(View.GONE);
			return;
		}
		
		topCardId = black.getId();
		
		SingleCardFragment scv = SingleCardFragment.newInstance(
				black.getId(),ViewContext.SELECT_BLACK,false,turnID);

		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.cs_display_frame, scv)
		.commit();
	}

	public Object getViewContext() {
			return context;
	}
    
}

	
class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static float MIN_SCALE = 0.85f;
    private static float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}

