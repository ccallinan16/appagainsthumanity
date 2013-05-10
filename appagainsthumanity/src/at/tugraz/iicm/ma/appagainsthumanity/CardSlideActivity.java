package at.tugraz.iicm.ma.appagainsthumanity;


import java.util.ArrayList;
import java.util.List;

import mocks.MockDealer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
	  	  
      initSlider();
      
      initTop();

      initButtons();
    }      
    	
	private void initButtons() {
		
		if (context == ViewContext.SELECT_BLACK ||
				context == ViewContext.SELECT_WHITE)
		{
		      LinearLayout btns = (LinearLayout) findViewById(R.id.footer);
		      btns.setVisibility(View.GONE);
		}
		else
		{
		      Button okButton = (Button) findViewById(R.id.okButton);
		      
		      switch (context)
		      {
		      case CONFIRM_PAIR:
		      case CONFIRM_SINGLE:
		    	  okButton.setText(R.string.menu_send);
		      }
		      
		      Button cancelBtn = (Button) findViewById(R.id.cancelButton);
		      cancelBtn.setVisibility(View.GONE);
		      
		      cancelBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override 
				public void onClick(View v) {
					//do not add any entries to db
					createAndStartMainActivity(v);
				}
			});
		      
		      okButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					int id = CardCollection.instance.getSelectedID();
					
			  		Intent intent = new Intent(v.getContext(),MainActivity.class);
								
					ServerConnector connector = new ServerConnector(new DBProxy(v.getContext()));
					
					switch (context)
					{
					case CONFIRM_SINGLE:
						connector.selectCardBlack(turnID, id);
						break;
					case CONFIRM_PAIR:
						connector.selectCardWhite(turnID, id); 
						break;
					default:
						break;
					}
										
				  	v.getContext().startActivity(intent);									
				}
			});

		}  
	}     

	protected void createAndStartMainActivity(View v) {
		Intent intent = new Intent(v.getContext(),MainActivity.class);
    	startActivity(intent);
	}

	private void initSlider()
	{
		
		  boolean selectable = false;
		  
		  if (context == ViewContext.SELECT_BLACK || 
			  context == ViewContext.SELECT_WHITE)
			  selectable = true;
		  
		  if (proxy == null)
			  proxy = new DBProxy(this);
		  
		  ServerConnector serverConnector = new ServerConnector(proxy);
		  MockDealer dealer = new MockDealer(this);
		  
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
			  
		  case CONFIRM_SINGLE:
		  case CONFIRM_PAIR:
			  cardIDs.add(CardCollection.instance.getSelectedID());
			  break;
			  default:
		  }
		  		  
		  //TODO: do something else for confirm views
		  CardFragmentAdapter pageAdapter = new CardFragmentAdapter(
	    		  getSupportFragmentManager(),
	    		  cardIDs,
	    		  selectable,
	    		  context.getCardType(),
	    		  turnID
	    		  );
	      
	      ViewPager pager = (ViewPager) findViewById(R.id.cs_card_slider);
	      
	      //TODO: exception in Unittests, comment back in for nice effects
	      //pager.setPageTransformer(true, new ZoomOutPageTransformer());
	      pager.setAdapter(pageAdapter);
	      
	      pager.setOnClickListener(new OnCardSelectionListener(context.getCardType(),turnID));
	
	}
	
	
	private void initTop()
	{
		boolean draw = false;
				
		if (	  context == ViewContext.CONFIRM_PAIR ||
				  context == ViewContext.SELECT_WHITE ||
				  context == ViewContext.SHOW_RESULT)
			draw = true;
		
	   
		ServerConnector serverConnector = new ServerConnector(proxy);
		int blackID = serverConnector.getBlackCardForTurn(turnID);
		
		MockDealer dealer = new MockDealer(this);
		Card black = dealer.getCardFromID(CardType.BLACK, blackID);
		
		if (black == null)
			black = CardCollection.instance.getBlackCard();
		
		if (black == null || !draw)
		{
			FrameLayout v = (FrameLayout) findViewById(R.id.cs_display_frame);
			v.setVisibility(View.GONE);
			return;
		}
		
		topCardId = black.getId();
		
		SingleCardFragment scv = SingleCardFragment.newInstance(
				black.getId(),black.getType(),false,turnID);

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

