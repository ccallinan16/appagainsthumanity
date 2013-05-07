package at.tugraz.iicm.ma.appagainsthumanity;


import mocks.MockDealer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardCollection;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.CardFragmentAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.gui.SingleCardFragment;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;
import at.tugraz.iicm.ma.appagainsthumanity.util.MessageDialog;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.Card;
import at.tugraz.iicm.ma.appagainsthumanity.xml.serie.CardType;

public class CardSlideActivity extends FragmentActivity {
	
	private ViewContext context = ViewContext.UNKNOWN;
	public int topCardId = -1;
	
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
	  
      //TODO
	  MockDealer dealer = new MockDealer(this);
      CardCollection.instance.setupContext(context,dealer);
  
	  
      //setup the ViewPager (to flip through cards) as well as the Top card
      initSlider();
      
      
      
      Button okButton = (Button) findViewById(R.id.okButton);
      
      switch (context)
      {
      case CONFIRM_PAIR:
      case CONFIRM_SINGLE:
    	  okButton.setText(R.string.menu_send);
      }
      
      Button cancelBtn = (Button) findViewById(R.id.cancelButton);

      cancelBtn.setOnClickListener(new View.OnClickListener() {
		
		@Override 
		public void onClick(View v) {
			//do not add any entries to db
			createAndStartMainActivity(v);
		}
	});
      
      okButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
        	  
        	int id = CardCollection.instance.getSelectedID();
        	        	
        	if (id == -1)
        		showAlertDialog(v);
        	
        	else
        		createAndStartNewActivity(v,id);
        	

          }

		private void showAlertDialog(View v) {
			
			
			MessageDialog dialog = new MessageDialog(v.getContext(),R.string.pop_title_select,R.string.pop_text_select) {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//don't do anything.
				}
			};
			dialog.show();
		}

		private void createAndStartNewActivity(View v,int id) {
        	Intent intent = new Intent(v.getContext(),CardSlideActivity.class);
	       	
        	switch(context)
        	{
        	case SELECT_WHITE:
        		if (id == -1) return;
  	        	intent.putExtras(BundleCreator.getConfirmWhite());
  	        	break;
        	case SELECT_BLACK:
        		if (id == -1) return;
        		intent.putExtras(BundleCreator.getConfirmBlack());
        		break;
        	case CONFIRM_SINGLE:
        	case CONFIRM_PAIR:
        		setSelectedInDB(v.getContext(),id);
        		default:
        			intent = new Intent(v.getContext(),MainActivity.class);

        	}
        	
        	startActivity(intent);
			
		}

		private void setSelectedInDB(Context c, int id) {
			DBProxy db = new DBProxy(c);
			//TODO: get turn_id!!
    		if(context.equals(ViewContext.CONFIRM_SINGLE))
    			db.setBlackCardID(1, id);
    		else
    		{
    			int userid = db.getUserID();
    			db.setWhiteCardID(2,userid, id);
    		}
		}
      });
      
    }     
    	
	protected void createAndStartMainActivity(View v) {
		Intent intent = new Intent(v.getContext(),MainActivity.class);
    	startActivity(intent);
	}

	private void initSlider()
	{
	      //TODO
		  MockDealer dealer = new MockDealer(this);
		
		  boolean selectable = false;
		  
		  if (context == ViewContext.SELECT_BLACK || 
			  context == ViewContext.SELECT_WHITE)
			  selectable = true;
		  
	      CardFragmentAdapter pageAdapter = new CardFragmentAdapter(
	    		  getSupportFragmentManager(), 
	    		  CardCollection.instance.getCardsForPager(context),
	    		  selectable
	    		  );
	      
	      ViewPager pager = (ViewPager) findViewById(R.id.cs_card_slider);
	
	 	      
	      //TODO: exception in Unittests, comment back in for nice effects
	      pager.setPageTransformer(true, new ZoomOutPageTransformer());
	      pager.setAdapter(pageAdapter);
	
	    	  initTop(dealer);
	
	}
	
	
	private void initTop(MockDealer dealer)
	{
		boolean draw = false;
		
	      if (	  context == ViewContext.CONFIRM_PAIR ||
	    		  context == ViewContext.SELECT_WHITE ||
	    		  context == ViewContext.SHOW_RESULT)
	    	  draw = true;
		
		Card black = CardCollection.instance.getBlackCard();
		
		if (black == null || !draw)
		{
			FrameLayout v = (FrameLayout) findViewById(R.id.cs_display_frame);
			v.setVisibility(View.GONE);
			return;
		}
		
		topCardId = black.getId();
		
		SingleCardFragment scv = SingleCardFragment.newInstance(
				black.getId(),black.getType(),30f,false);

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

