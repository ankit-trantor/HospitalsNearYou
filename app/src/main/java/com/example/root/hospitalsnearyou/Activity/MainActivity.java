package com.example.root.hospitalsnearyou.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.hospitalsnearyou.Fragment.BloodBankDetails;
import com.example.root.hospitalsnearyou.Fragment.BloodBankList;
import com.example.root.hospitalsnearyou.Fragment.GotoUserHospDetails;
import com.example.root.hospitalsnearyou.Fragment.HomeFrag;
import com.example.root.hospitalsnearyou.Fragment.HospitalDetailsFrag;
import com.example.root.hospitalsnearyou.Fragment.HospitalList;
import com.example.root.hospitalsnearyou.ModelClass.NavDrawerItem;
import com.example.root.hospitalsnearyou.R;
import com.example.root.hospitalsnearyou.Service.DownloadService;
import com.example.root.hospitalsnearyou.adapter.NavDrawerListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    List<Address> addresses = null;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        startService(intent);

        //location search
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0, mLocationListener);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, 0)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, 0)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
//        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
        HomeFrag hospitalDetailsFrag = new HomeFrag();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag).commit();

    }//fused location

    public void hospitalDetailsFrag(int position,String state,String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        HospitalDetailsFrag hospitalDetailsFrag = new HospitalDetailsFrag();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag).commit();
    }

    public void gotoUserHospDetails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        GotoUserHospDetails hospitalDetailsFrag = new GotoUserHospDetails();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag).commit();
    }

    public void gotoUserBloodBankDetails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        GotoUserHospDetails hospitalDetailsFrag = new GotoUserHospDetails();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag).commit();
    }

    public void gotoBloodBankDeatails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        BloodBankDetails hospitalDetailsFrag = new BloodBankDetails();
        hospitalDetailsFrag.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag).commit();
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//        menu.findItem(R.id.map).setVisible(!drawerOpen);
//            MenuItem map = menu.findItem(R.id.map);
//        map.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {

            case 0:
                HomeFrag homeFrag = new HomeFrag();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, homeFrag).commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 1:
                HospitalList playListFragment = new HospitalList();
                    Bundle bundle = new Bundle();
                    bundle.putString("city", addresses.get(0).getLocality());
                    bundle.putString("state", addresses.get(0).getAdminArea());
                    playListFragment.setArguments(bundle);
                FragmentManager listManager = getFragmentManager();
                FragmentTransaction listTransaction = listManager.beginTransaction();
                listTransaction.replace(R.id.frame_container, playListFragment, "");
                listTransaction.commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 2:
//                fragment = new PhotosFragment();
                BloodBankList bloodBankList = new BloodBankList();
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", addresses.get(0).getLocality());
                bundle1.putString("state", addresses.get(0).getAdminArea());
                bloodBankList.setArguments(bundle1);
                FragmentManager listManagerBlood = getFragmentManager();
                FragmentTransaction listTransactionBlood = listManagerBlood.beginTransaction();
                listTransactionBlood.replace(R.id.frame_container, bloodBankList, "");
                listTransactionBlood.commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 3:
//                fragment = new CommunityFragment();
                break;
            case 4:
//                fragment = new PagesFragment();
                break;
            case 5:
//                fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Log.e("location", "khedg");

            //your code here
            Double lat = location.getLatitude();
            Double longg = location.getLongitude();
            Log.e("location", "" + location.getLatitude());
            Log.e("location", "" + location.getLongitude());

            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = gcd.getFromLocation(lat, longg, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                Log.e("hi", "" + addresses.get(0).getLocality());//city
                Log.e("hi", "" + addresses.get(0).getAddressLine(1));//local area
                Log.e("hi", "" + addresses.get(0).getAdminArea());//state
            }


        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

}
