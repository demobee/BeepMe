package com.softxpliot.demob.beepme;

/**
 * Created by demob on 8/22/14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demob on 7/22/14.
 */
public class BeepMeBroadCastReceiver extends BroadcastReceiver {
    BeepMe activity;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    static List peers = new ArrayList();


    public BeepMeBroadCastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, BeepMe activity) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.activity = activity;
    }


    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            if(peers.size() > 0) {
                /**while(peerList.getDeviceList().iterator().hasNext()){
                 String deviceAddress = peerList.getDeviceList().iterator().next().deviceAddress;
                 }**/
                Log.d(activity.getApplicationInfo().name, peers.size() + peers.toString());

            }
            //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
            if(peers.size() == 0 ){
                Log.d(activity.getApplicationInfo().name, " "+ peers.size());
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            Log.d(activity.getApplicationInfo().name, "WIFI_P2P_STATE_CHANGED_ACTION");
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context, "WI-FI direct is enable", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context, "WI-FI direct is not enable", Toast.LENGTH_LONG).show();
            }
        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
            Log.d(activity.getApplicationInfo().name, "WIFI_P2P_PEERS_CHANGED_ACTION");
            if(mManager != null){
                mManager.requestPeers(mChannel, peerListListener);
            }
        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            Log.d(activity.getApplicationInfo().name, "WIFI_P2P_CONNECTION_CHANGED_ACTION");
        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            Log.d(activity.getApplicationInfo().name, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION");

        }
    }

    public static List getPeers()
    {
        return peers;
    }

}
