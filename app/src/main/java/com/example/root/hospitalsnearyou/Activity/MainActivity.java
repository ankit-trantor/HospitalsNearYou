package com.example.root.hospitalsnearyou.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.hospitalsnearyou.DB.HospitalDataBase;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private static final String HOME_FRAGMENT = "homeFragment";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    List<Address> addresses = new ArrayList<Address>();
    // nav drawer title
    private CharSequence mDrawerTitle;
    private boolean flag = false, flagForLocation;
    // used to store app title
    private CharSequence mTitle;
    static boolean locationStatus = false;
    private static String cityForbundle, stateForBundle;

    boolean checkStatus = false;
    String provider;
    boolean status = false;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private LocationManager locationManager;
    ProgressDialog dialog;
    HospitalDataBase hospitalDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hospitalDataBase = new HospitalDataBase(MainActivity.this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Hospital", MODE_PRIVATE);
        status = pref.getBoolean("Status", false);

        if (hospitalDataBase.readHospitalDataFromDatabase().size() == 0) {
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            startService(intent);
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading. Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                do {
                    if (checkStatus == true && status == false) {
                        dialog.dismiss();
                        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref1.edit();
                        editor1.putBoolean("Status", true);
                        editor1.commit();

                    }
                    Log.e("size", "" + hospitalDataBase.readBloddBankDataFromDB().size());
                    if (hospitalDataBase.readBloddBankDataFromDB().size() >= 2900) {
                        checkStatus = true;
                    }
                } while (hospitalDataBase.readBloddBankDataFromDB().size() < 2946);
            }

        }).start();
        flagForLocation = false;

        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        crit.setPowerRequirement(Criteria.POWER_LOW);
        //or getBestProvider(criteria, false), but network is better not to jump location.
        provider = locationManager.GPS_PROVIDER;//getBestProvider(crit,true);
        //Location location = locationManager.getLastKnownLocation(provider);

        //drawer
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
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.rsz,//nav menu toggle icon
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
//        HomeFrag hospitalDetailsFrag = new HomeFrag();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frame_container, hospitalDetailsFrag, HOME_FRAGMENT).commit();

    }//fused location


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, (long) 0.1, (float) 0.1, (LocationListener) this);
    }

    public void hospitalDetailsFrag(int position, String state, String city) {
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
        GotoUserHospDetails bloodBankDetails = new GotoUserHospDetails();
        bloodBankDetails.setArguments(bundle);
        FragmentManager fragmentManagerForBlood = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManagerForBlood.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, bloodBankDetails).commit();
    }

    public void gotoBloodBankDeatails(int position, String state, String city) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("state", state);
        bundle.putString("city", city);
        BloodBankDetails bloodBankDetails = new BloodBankDetails();
        bloodBankDetails.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, bloodBankDetails).commit();
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
//        MenuItem map = menu.findItem(R.id.map);
//        map.setVisible(false);
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
//        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
////        menu.findItem(R.id.map).setVisible(!drawerOpen);
//        MenuItem map = menu.findItem(R.id.map);
//        map.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }


    private void displayView(int position) {
        Fragment fragment = null;
        switch (position) {

            case 0:
                hideKeyboard();
                HomeFrag homeFrag = new HomeFrag();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, homeFrag, HOME_FRAGMENT).addToBackStack(null).commit();
                mDrawerList.setItemChecked(position, true);
                mDrawerList.setSelection(position);
                setTitle(navMenuTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 1:
                hideKeyboard();
                HospitalList playListFragment = new HospitalList();
                Bundle bundle = new Bundle();
                bundle.putString("city", cityForbundle);
                bundle.putString("state", stateForBundle);
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
                hideKeyboard();
                BloodBankList bloodBankList = new BloodBankList();
                SharedPreferences sharedPreferences = getSharedPreferences("location", MODE_PRIVATE);
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", sharedPreferences.getString("city", ""));
                bundle1.putString("state", sharedPreferences.getString("state", ""));
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
        public void onLocationChanged(Location location) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();

//            updateCamera(lat,lon);
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

    /*private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences("location", MODE_PRIVATE);

            locationStatus = pref1.getBoolean("Location", false);

            if (location != null && locationStatus == false) {
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.putBoolean("Location", true);


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
                    flag = true;
                    HospitalDataBase.checkBundle = true;
                    cityForbundle = addresses.get(0).getLocality();
                    stateForBundle = addresses.get(0).getAdminArea();
                    editor1.putString("city", cityForbundle);
                    editor1.putString("state", stateForBundle);
                    editor1.commit();

                    Log.e("hi", "" + addresses.get(0).getLocality());//city
                    Log.e("hi", "" + addresses.get(0).getAddressLine(1));//local area
                    Log.e("hi", "" + addresses.get(0).getAdminArea());//state
                }
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
    };*/

};

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        HomeFrag playListFragment = new HomeFrag();
        FragmentManager listManager = getFragmentManager();
        if (getFragmentManager().findFragmentByTag(HOME_FRAGMENT) != null) {
            FragmentTransaction listTransaction = listManager.beginTransaction();
            listTransaction.remove(getFragmentManager().findFragmentByTag(HOME_FRAGMENT));
            listTransaction.replace(R.id.frame_container, playListFragment, HOME_FRAGMENT);
            listTransaction.commit();

        } else {
            FragmentTransaction listTransaction = listManager.beginTransaction();
            listTransaction.replace(R.id.frame_container, playListFragment, HOME_FRAGMENT);
//            listTransaction.addToBackStack(null);
            listTransaction.commit();
        }

    }
}
