/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.familab.app;

//import static android.support.v4.app.FragmentActivity.TAG;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public final static String EXTRA_LINK = "org.familab.app.LINK";

    // url to make request
    private static String url = "http://famitracker.herokuapp.com/unique_items.json";

    // JSON Node names
    //Main List
    private static final String TAG_UNIQUE_ID = "unique_id";
    private static final String TAG_AREA = "area";
    private static final String TAG_CREATED_AT = "created_at";
    private static final String TAG_FUID = "fuid";
    private static final String TAG_ID = "id";
    private static final String TAG_LOGGABLE = "loggable";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHOTO_CONTENT_TYPE = "photo_content_type";
    private static final String TAG_PHOTO_FILE_NAME = "photo_file_name";
    private static final String TAG_PHOTO_FILE_SIZE = "photo_file_size";
    private static final String TAG_PHOTO_UPDATED_AT = "photo_updated_at";
    private static final String TAG_TICKETABLE = "ticketable";
    private static final String TAG_UPDATED_AT = "updated_at";


    //JSON Node names
    //individual Items






    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();
                    
                
                case 1:
                	//Display Calendar Events
                	return new EventsList();
                
                case 2:
                	//Display Famiduino picture
                	return new StatusPageJSON();

                case 3:
                    //Display Status webview
                    return new TwitterPage();

                	

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	if(position == 0){
        		return "Collaborate";
        	}
        	else if(position == 1){
        		return "Events";
        	}
            else if(position == 2){
                return "Status";
            }
        	else{
        		return "FamiDuino";
        	}
            
        }
    }

    /**
     * A fragment that launches different links and applications of interest.
     */
    public static class LaunchpadSectionFragment extends ListFragment {
    	
    	public void onActivityCreated(Bundle savedInstanceState) {
    		super.onActivityCreated(savedInstanceState);
    	    String[] values = new String[] { "FamiLAB.org", "Forums", "IRC",
        	        "Map", "Twitter", "Google+"};
    	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
    	        android.R.layout.simple_list_item_1, values);
    	    setListAdapter(adapter);
            setHasOptionsMenu(true);
    	    
        }

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.web, menu);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(getActivity(), Settings.class);
                    startActivity(intent);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }



        @Override
    	public void onListItemClick(ListView l, View v, int position, long id) {
    	    super.onListItemClick(l, v, position, id);
    		//get item that was clicked
    	    Object o = this.getListAdapter().getItem(position);
    		String keyword = o.toString();
    		  
            //launch intents when different list items are clicked
            if(keyword.equals("FamiLAB.org")){
    	        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.familab.org"));
    			startActivity(browserIntent);
    		}
    		else if(keyword.equals("Forums")){
    			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forums.familab.org"));
    			  startActivity(browserIntent);
    		  }
    	    else if(keyword.equals("IRC")){
    			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://webchat.freenode.net/?channels=#familab"));
    			  startActivity(browserIntent);
    			  //Intent intent = new Intent(getActivity(), WebViewActivity.class);
                  //String message = "http://webchat.freenode.net/?channels=#familab";
                  //intent.putExtra(EXTRA_LINK, message);
                  //startActivity(intent);
    		  }
    		else if(keyword.equals("Map")){
    			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/maps?q=FamiLAB,+1355+Bennett+Dr+%23129,+Longwood,+FL&hl=en&ll=28.687802,-81.353073&spn=0.040509,0.083857&sll=28.699909,-81.35064&sspn=0.081008,0.167713&oq=famil&t=h&hq=familab+1355+bennett+dr+129&hnear=Longwood,+Seminole,+Florida&z=14&iwloc=A"));
    			  startActivity(browserIntent);
    		  }
    		else if(keyword.equals("Twitter")){
    			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/FamiLAB"));
    			  startActivity(browserIntent);
    		  }
    		else if(keyword.equals("Google+")){
    			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/100828404477153041391/posts"));
    			  startActivity(browserIntent);
    		  }
    		  

    	  }
    }

    /**
     * Displays Upcoming Events with embedded google calendar in a webview.
     * @author Conner Brooks
     *
     */
    public static class EventsList extends Fragment{
    	private WebView webView;

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.web, menu);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(getActivity(), Settings.class);
                    startActivity(intent);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.calendar_section, container, false);
    	    
    	    webView = (WebView) rootView.findViewById(R.id.webView1);
    	    webView.getSettings().setJavaScriptEnabled(true);
    	    webView.loadUrl("https://www.google.com/calendar/embed?showTitle=0&showNav=0&showDate=0&showPrint=0&showTabs=0&showCalendars=0&mode=AGENDA&height=300&wkst=1&bgcolor=%23FFFFFF&src=familab.4am%40gmail.com&color=%23AB8B00&ctz=America%2FNew_York");
            setHasOptionsMenu(true);
            return rootView;
    	}


    }

    /**
     * Display Twitter FamiDuino Posts and other pictures
     */
    public static class TwitterPage extends Fragment{
    	private WebView webView;

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.famiduino, menu);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(getActivity(), Settings.class);
                    startActivity(intent);
                    return true;

                case R.id.menu_refresh:
                    webView.reload();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.twitter_page, container, false);
    	    
    	    webView = (WebView) rootView.findViewById(R.id.webView2);
    	    webView.getSettings().setJavaScriptEnabled(true);
    	    webView.loadUrl("http://randomphp.levelsetlabs.com/a.php");
            setHasOptionsMenu(true);
            return rootView;
    	}


    }

    /***
     * Display webview with status page
     * todo- make this a native part of the application
     */

    public static class StatusPage extends Fragment{
        private WebView webView;

        WebViewClient MyWebViewClient = new WebViewClient()
        {
            // Override page so it's load on my view only
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url)
            {
                view.loadUrl(url);
                return true;
            }
        };

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.status_page, container, false);

            webView = (WebView) rootView.findViewById(R.id.webView3);
            webView.setWebViewClient(MyWebViewClient);
            webView.getSettings().setJavaScriptEnabled(true);

            if(savedInstanceState != null){
                ((WebView)rootView.findViewById(R.id.webView3)).restoreState(savedInstanceState);
            }
            else{
            webView.loadUrl("http://famitracker.herokuapp.com/unique_items");
            }
            setHasOptionsMenu(true);


            return rootView;
        }
        @Override
        public void onSaveInstanceState(Bundle outState){
            webView.saveState(outState);
        }

        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.status, menu);
        }

        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(getActivity(), Settings.class);
                    startActivity(intent);
                    return true;

                case R.id.menu_refresh:
                    webView.reload();
                    return true;

                case R.id.menu_back:
                    webView.goBack();
                    return true;

                case R.id.menu_QR:
                    IntentIntegrator integrator = new IntentIntegrator((Fragment)this);
                    integrator.initiateScan();

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanResult != null) {
                // handle scan result
                //url famitracker.herokuapp.com/unique_items/new?fuid=809430970974
                String basedUrl = "http://famitracker.herokuapp.com/unique_items/new?fuid=";

                basedUrl += scanResult.getContents();

                webView.loadUrl(basedUrl);
            }
         // else continue with any other code you need in the method
        }
    }


    public static class StatusPageJSON extends ListFragment{
        JSONArray unique_objects = null;

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);




            // Hashmap for ListView
            ArrayList<HashMap<String, String>> uniqueItemList = new ArrayList<HashMap<String, String>>();
            String[] values = null;

            //testing JSON Parser
            // Creating JSON Parser instance
            JSONParser jParser = new JSONParser();

            // getting JSON string from URL
            JSONObject json = null;
            try{
                json = jParser.getJSON(url);
            } catch (JSONException e){
                Log.e("oh god", "oh no");
            }

            try {
                // Getting Array of Contacts
                unique_objects = json.getJSONArray(TAG_UNIQUE_ID);
                values = new String[unique_objects.length()];
                // looping through All Contacts
                for(int i = 0; i < unique_objects.length(); i++){
                    JSONObject c = unique_objects.getJSONObject(i);

                    // Storing each json item in variable
                    String area = c.getString(TAG_AREA);
                    String name = c.getString(TAG_NAME);
                    String fuid = c.getString(TAG_FUID);
                    Boolean ticketable = c.getBoolean(TAG_TICKETABLE);
                    values[i] = name;
                    // creating new HashMap
                    //HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    //map.put(TAG_NAME, name);
                    //map.put(TAG_AREA, area);
                    //map.put(TAG_FUID, fuid);

                    //uniqueItemList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**
            ListAdapter adapter = new SimpleAdapter(getActivity(), uniqueItemList,
                    R.layout.list_item,
                    new String[] { TAG_NAME, TAG_AREA, TAG_FUID}, new int[] {
                    R.id.name, R.id.area, R.id.fuid });
            */

            //String[] values = new String[] { "FamiLAB.org", "Forums", "IRC",
            //        "Map", "Twitter", "Google+"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, values);
            setListAdapter(adapter);
            setHasOptionsMenu(true);

        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}


