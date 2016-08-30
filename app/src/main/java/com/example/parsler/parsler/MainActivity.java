package com.example.parsler.parsler;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Components.Navigation.NavigationDrawerFragment;
import com.example.parsler.parsler.Components.RecyclerView.RecyclerItemClickListener;
import com.example.parsler.parsler.Models.ProfileFragmentObject;
import com.example.parsler.parsler.Utility.ActionBarUtils;
import com.example.parsler.parsler.Utility.MessageController;
import com.example.parsler.parsler.Utility.OMSController;
import com.example.parsler.parsler.databinding.FragmentProfileBinding;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private static LinearLayout linearLayout;
    private static RecyclerView recyclerView;
    private static ScrollView scrollViewContainer;
    private static TextView textViewOrderCount;
    private static TextView textViewNoCount;
    private static ProgressBar progressBar;
    private static View rootView = null;
    private static int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1: mTitle = StringConstants.NAVIGATION_ACTIVE_ORDERS;
                    break;
            case 2: mTitle = StringConstants.NAVIGATION_ORDER_HISTORY;
                    break;
            case 3: mTitle = StringConstants.NAVIGATION_PROFILE;
                    break;
            case 4: mTitle = StringConstants.NAVIGATION_MESSAGES;
                    break;
            case 5: mTitle = StringConstants.NAVIGATION_LOGOUT;
                    break;
        }
    }


    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call_manager:
                ActionBarUtils.callManager(getApplicationContext());
                return true;

            case R.id.action_settings:
                ActionBarUtils.logout(getApplicationContext());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            switch(getArguments().getInt(ARG_SECTION_NUMBER)) {

                case 1:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);

                    // Initialize view components
                    linearLayout = (LinearLayout) rootView.findViewById(R.id.content);
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.active_order_list);
                    scrollViewContainer = (ScrollView) rootView.findViewById(R.id.active_order_container);
                    textViewOrderCount = (TextView) rootView.findViewById(R.id.active_order_count);
                    textViewNoCount = (TextView) rootView.findViewById(R.id.no_active_order);
                    progressBar = (ProgressBar) rootView.findViewById(R.id.loader);
                    onRecyclerViewClickListener(getActivity(), recyclerView);

                    OMSController.initializeOrderList(recyclerView, scrollViewContainer,
                            textViewOrderCount, textViewNoCount, StringConstants.KEY_ACTIVE_ORDERS,
                            progressBar, linearLayout);
                    if(count != 0) {
                        OMSController.bindOrderList(getActivity().getApplicationContext(),
                                StringConstants.KEY_ACTIVE_ORDERS);
                    }
                    count++;
                    break;


                case 2:
                    rootView = inflater.inflate(R.layout.fragment_history, container, false);

                    // Initialize view components
                    linearLayout = (LinearLayout) rootView.findViewById(R.id.content);
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.history_order_list);
                    scrollViewContainer = (ScrollView) rootView.findViewById(R.id.history_order_container);
                    textViewOrderCount = (TextView) rootView.findViewById(R.id.history_order_count);
                    textViewNoCount = (TextView) rootView.findViewById(R.id.no_history_order);
                    progressBar = (ProgressBar) rootView.findViewById(R.id.loader);
                    onRecyclerViewClickListener(getActivity(), recyclerView);

                    OMSController.initializeOrderList(recyclerView, scrollViewContainer,
                            textViewOrderCount, textViewNoCount, StringConstants.KEY_HISTORY_ORDERS,
                            progressBar, linearLayout);
                    if(count != 0) {
                        OMSController.bindOrderList(getActivity().getApplicationContext(),
                                StringConstants.KEY_HISTORY_ORDERS);
                    }
                    count++;
                    break;


                case 3:
                    rootView = inflater.inflate(R.layout.fragment_profile, container, false);
                    FragmentProfileBinding binding = DataBindingUtil.bind(rootView);
                    ProfileFragmentObject profileFragmentObject = new ProfileFragmentObject(getActivity());
                    binding.setProfileObject(profileFragmentObject);
                    break;


                case 4:
                    rootView = inflater.inflate(R.layout.fragment_messages, container, false);

                    // Initialize view components
                    linearLayout = (LinearLayout) rootView.findViewById(R.id.content);
                    recyclerView = (RecyclerView) rootView.findViewById(R.id.message_list);
                    textViewNoCount = (TextView) rootView.findViewById(R.id.no_message_received);
                    progressBar = (ProgressBar) rootView.findViewById(R.id.loader);
                    onRecyclerViewClickListener(getActivity(), recyclerView);


                    MessageController.initializeMessageList(recyclerView, textViewNoCount,
                            progressBar, linearLayout);
                    if(count != 0) {
                        MessageController.bindMessageList(getActivity().getApplicationContext());
                    }
                    count++;
                    break;


                case 5:
                    ActionBarUtils.logout(getActivity().getApplicationContext());
                    break;
            }

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



    /**
     * Function to fetch pickup id and navigate to detail view
     * @param activity
     * @param recyclerView
     */
    public static void onRecyclerViewClickListener(final Activity activity, RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(activity.getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        TextView pickupIdTextView = (TextView) view.findViewById(R.id.pickupId);
                        TextView pickupIsActiveTextView = (TextView) view.findViewById(R.id.pickupIsActive);

                        Intent intent = new Intent(activity, PickupSummaryActivity.class);
                        intent.putExtra(StringConstants.KEY_PICKUP_ID, pickupIdTextView.getText().toString());
                        intent.putExtra(StringConstants.KEY_IS_ACTIVE,
                                Boolean.valueOf(pickupIsActiveTextView.getText().toString()));
                        activity.startActivity(intent);
                    }
                })
        );
    }
}
