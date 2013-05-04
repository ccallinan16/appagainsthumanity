package at.tugraz.iicm.ma.appagainsthumanity;

import java.util.HashMap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import at.tugraz.iicm.ma.appagainsthumanity.fragments.OptionsFragment;
import at.tugraz.iicm.ma.appagainsthumanity.fragments.PlayersFragment;
import at.tugraz.iicm.ma.appagainsthumanity.fragments.TurnsFragment;


public class GameOverviewActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap<String,TabInfo> mapTabInfo = new HashMap<String,TabInfo>();
    private TabInfo mLastTab = null;

    private class TabInfo {
        private String tag;
        private Class clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }
    }
    
    class TabFactory implements TabContentFactory {
    	 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Step 1: Inflate layout
		setContentView(R.layout.tabs_layout);
		// Step 2: Setup TabHost
        initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
    }
	
	/** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
	
	private void initialiseTabHost(Bundle args) {
	    mTabHost = (TabHost)findViewById(android.R.id.tabhost);
	    mTabHost.setup();
	    TabInfo tabInfo = null;
	    addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Turns", getApplicationContext().getResources().getDrawable(R.drawable.icon_turns_tab)), ( tabInfo = new TabInfo("Tab1", TurnsFragment.class, args)));
	    this.mapTabInfo.put(tabInfo.tag, tabInfo);
	    addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Players", getApplicationContext().getResources().getDrawable(R.drawable.icon_players_tab)), ( tabInfo = new TabInfo("Tab2", PlayersFragment.class, args)));
	    this.mapTabInfo.put(tabInfo.tag, tabInfo);
	    addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Options", getApplicationContext().getResources().getDrawable(R.drawable.icon_options_tab)), ( tabInfo = new TabInfo("Tab3", OptionsFragment.class, args)));
	    this.mapTabInfo.put(tabInfo.tag, tabInfo);
	    // Default to first tab
	    this.onTabChanged("Tab1");
	    //
	    mTabHost.setOnTabChangedListener(this);
	}
	
	private static void addTab(GameOverviewActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        String tag = tabSpec.getTag();
 
        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(tabInfo.fragment);
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
 
        tabHost.addTab(tabSpec);
    }
	
	/** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        TabInfo newTab = this.mapTabInfo.get(tag);
        if (mLastTab != newTab) {
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    ft.detach(mLastTab.fragment);
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(this,
                            newTab.clss.getName(), newTab.args);
                    ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
                } else {
                    ft.attach(newTab.fragment);
                }
            }
 
            mLastTab = newTab;
            ft.commit();
            this.getSupportFragmentManager().executePendingTransactions();
        }
    }
	
	
	/*
	 * DEFAULT METHODS
	 */
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_overview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
