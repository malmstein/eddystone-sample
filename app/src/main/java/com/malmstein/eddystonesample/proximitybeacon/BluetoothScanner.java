package com.malmstein.eddystonesample.proximitybeacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.malmstein.eddystonesample.StringUtils;
import com.malmstein.eddystonesample.model.Beacon;

import java.util.ArrayList;
import java.util.Arrays;

public class BluetoothScanner {

    private static final String TAG = BluetoothScanner.class.getSimpleName();

    private ArrayList<Beacon> arrayList;
    private BluetoothLeScanner scanner;
    private Listener listener;

    public BluetoothScanner(Listener listener) {
        this.listener = listener;
    }

    public void startScan(BluetoothAdapter btAdapter) {
        scanner = btAdapter.getBluetoothLeScanner();
        scanner.startScan(Eddystone.SCAN_FILTERS, Eddystone.SCAN_SETTINGS, new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Beacon scannedBeacon = validateScan(result);
                if (scannedBeacon != null) {
                    fetchBeaconStatus(scannedBeacon);
                }
            }
        });
    }

    private Beacon validateScan(ScanResult result) {
        if (Eddystone.isValid(result)) {
            byte[] id = Eddystone.getId(result.getScanRecord());
            if (beaconAlreadyScanned(id)) {
                return null;
            }

            Log.i(TAG, "id " + StringUtils.toHexString(id) + ", rssi " + result.getRssi());
            Beacon beacon = Beacon.from(id, result.getRssi());
            arrayList.add(beacon);
            return beacon;
        } else {
            return null;
        }
    }

    private boolean beaconAlreadyScanned(byte[] id) {
        for (Beacon beacon : arrayList) {
            if (Arrays.equals(beacon.getId(), id)) {
                return true;
            }
        }
        return false;
    }

    private void fetchBeaconStatus(Beacon beacon) {

    }

    public interface Listener {
        void onBeaconScanned(Beacon beacon);
    }

}
