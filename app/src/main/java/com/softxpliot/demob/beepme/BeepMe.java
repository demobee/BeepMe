package com.softxpliot.demob.beepme;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class BeepMe extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    //Helper helper;
    private final IntentFilter filter  = new IntentFilter();;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BeepMeBroadCastReceiver mReceiver;
    private List<WifiP2pDevice> peers = new List<WifiP2pDevice>() {
        @Override
        public void add(int location, WifiP2pDevice object) {

        }

        @Override
        public boolean add(WifiP2pDevice object) {
            return false;
        }

        @Override
        public boolean addAll(int location, Collection<? extends WifiP2pDevice> collection) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends WifiP2pDevice> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean contains(Object object) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return false;
        }

        @Override
        public WifiP2pDevice get(int location) {
            return null;
        }

        @Override
        public int indexOf(Object object) {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public Iterator<WifiP2pDevice> iterator() {
            return null;
        }

        @Override
        public int lastIndexOf(Object object) {
            return 0;
        }

        @Override
        public ListIterator<WifiP2pDevice> listIterator() {
            return null;
        }

        @Override
        public ListIterator<WifiP2pDevice> listIterator(int location) {
            return null;
        }

        @Override
        public WifiP2pDevice remove(int location) {
            return null;
        }

        @Override
        public boolean remove(Object object) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> collection) {
            return false;
        }

        @Override
        public WifiP2pDevice set(int location, WifiP2pDevice object) {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public List<WifiP2pDevice> subList(int start, int end) {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] array) {
            return null;
        }
    };
    static View fragmentView = null;
    ListView list_available_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beep_me);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));



        //helper = new Helper(this);
        //helper.initializeNsd();
        //mReceiver = new BeepMeBroadCastReceiver(mManager, mChannel,this);

        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        enableWiFi();
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        discoverPeers(mChannel);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                discoverPeers(mChannel);
                if(peers.size() > 0 && peers != null){
                    //displayWiFiDevice(peers);
                    list_available_connection = (ListView) findViewById(R.id.list_available_connections);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, displayWiFiDevice(peers));
                    list_available_connection.setAdapter(adapter);
                    list_available_connection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            WifiP2pConfig config = new WifiP2pConfig();
                            for (WifiP2pDevice peer : peers){
                                if(peer.deviceName.equals (((TextView) view).getText())){
                                    config.deviceAddress = peer.deviceAddress;
                                    config.wps.setup = WpsInfo.PBC;
                                    connect(config);
                                }
                            }
                        }
                    });
                }
                Log.d(this.getApplicationInfo().name, peers.size() + " Android device found.");

                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.beep_me, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        //if(helper != null) {
        //helper.stopDiscovery();
        unregisterReceiver(mReceiver);
        //}
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //helper.tearDown();
        //connection.tearDown();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new BeepMeBroadCastReceiver(mManager, mChannel,this);
        registerReceiver(mReceiver, filter);
        //discoverPeers(mChannel);
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
            View rootView = inflater.inflate(R.layout.fragment_beep_me, container, false);
            fragmentView = rootView;
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((BeepMe) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void discoverPeers(final WifiP2pManager.Channel mChannel){
        mManager.discoverPeers(mChannel,new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(BeepMe.this, "Searching for available WI-FI connections.", Toast.LENGTH_LONG).show();
                peers = mReceiver.getPeers();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(BeepMe.this, "Sorry could not find peers.", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void enableWiFi()
    {
        WifiManager wifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);

        if(!wifiManager.isWifiEnabled()){
            BMDialog bmDialog = new BMDialog(this);
            bmDialog.show(this.getFragmentManager(),"Wifi Status");
        }

    }

    public ArrayList<String> displayWiFiDevice(List<WifiP2pDevice> devices){
        ArrayList<String> deviceNames = new ArrayList<String>();
        for( WifiP2pDevice device : devices){
            deviceNames.add(device.deviceName);
            //final EditText view = (EditText) fragmentView.findViewById(R.id.section_label);
            //view.setText(device.deviceName,TextView.BufferType.EDITABLE);
        }
        return deviceNames;
    }

    public void connect(WifiP2pConfig config){
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }

    public Dialog pairWithDevice(String message,final WifiP2pConfig config){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to pair with " +message + " ?");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connect(config);
            }
        }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return alertDialog.create();
    }
}