package com.malmstein.eddystonesample.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

public class BleManager {

    public static final int REQUEST_CODE_ENABLE_BLE = 1001;

    private final Activity activity;

    public BleManager(Activity activity) {
        this.activity = activity;
    }

    public void startScan(){
        BluetoothManager btManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        }

    }


}
