package test.slow.gui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.util.SQLTestRunner;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import at.tugraz.iicm.ma.appagainsthumanity.CardSlideActivity;
import at.tugraz.iicm.ma.appagainsthumanity.CreateGameActivity;
import at.tugraz.iicm.ma.appagainsthumanity.GameOverviewActivity;
import at.tugraz.iicm.ma.appagainsthumanity.MainActivity;
import at.tugraz.iicm.ma.appagainsthumanity.R;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.GamelistAdapter;
import at.tugraz.iicm.ma.appagainsthumanity.adapter.ViewContext;
import at.tugraz.iicm.ma.appagainsthumanity.db.DBProxy;
import at.tugraz.iicm.ma.appagainsthumanity.db.PresetHelper;
import at.tugraz.iicm.ma.appagainsthumanity.util.BundleCreator;

import com.xtremelabs.robolectric.matchers.StartedMatcher;

@RunWith(SQLTestRunner.class)
public class ShowResultsTransitionTest {

    @Before
    public void setUp() throws Exception {
    	DBProxy proxy = new DBProxy(new Activity());
    	PresetHelper.setPreset(proxy, PresetHelper.SELECT_BLACK);
    }
	
	
	@Test
	public void testTransitionFromListToCardSlideActivity()
	{		
		CardSlideActivity csa = new CardSlideActivity();
    	Intent i = new Intent(csa, CardSlideActivity.class);
    	i.putExtras(BundleCreator.createBundle(ViewContext.SHOW_RESULT,PresetHelper.man.getSecondToLastTurnID()));

		csa.setIntent(i);
				
		csa.onCreate(null);
		
		Button okButton = (Button) csa.findViewById(R.id.okButton);

		okButton.performClick();
	
    	Assert.assertThat(csa, new StartedMatcher(GameOverviewActivity.class));

	}
	
}
