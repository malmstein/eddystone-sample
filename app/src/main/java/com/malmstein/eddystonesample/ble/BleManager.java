package com.malmstein.eddystonesample.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.scan.ScanningActivity;
import com.novoda.notils.caster.Classes;

public class BleManager {

    private static final String TAG = ScanningActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ENABLE_BLE = 1001;

    private final Listener listener;
    private final Activity activity;

    public BleManager(Activity activity) {
        this.activity = activity;
        this.listener = Classes.from(activity);
    }

    public void startScan(){
        BluetoothManager btManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        }

        if (btAdapter == null || !btAdapter.isEnabled()) {
            listener.onBluetoothNotAvailable();
            Log.e(TAG, activity.getString(R.string.bluetooth_cant_enable));

            return;
        }
    }

    public interface Listener {
        void onBluetoothNotAvailable();
    }

}
