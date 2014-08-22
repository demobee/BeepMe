package com.softxpliot.demob.beepme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;

/**
 * Created by demob on 7/24/14.
 */
public class BMDialog extends DialogFragment {

    Activity mActivity;

    public BMDialog(Activity mActivity) {
        this.mActivity = mActivity;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.title_wifi_header);
        final WifiManager wifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        builder.setPositiveButton(R.string.turn_on_wifi,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(true);
                }
            }
        }).setNegativeButton(R.string.do_not_turn_on_wifi,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

}
