package com.malmstein.eddystonesample.ble;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.malmstein.eddystonesample.StringUtils;
import com.malmstein.eddystonesample.model.Beacon;

import java.util.ArrayList;
import java.util.Arrays;

public class BleScanCallback extends ScanCallback {

    private static final String TAG = BleScanCallback.class.getSimpleName();

    // The Eddystone-UID frame type byte.
    // See https://github.com/google/eddystone for more information
    private static final byte EDDYSTONE_UID_FRAME_TYPE = 0x00;

    private ArrayList<Beacon> arrayList;

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        ScanRecord scanRecord = result.getScanRecord();
        if (scanRecord == null) {
            Log.w(TAG, "Null ScanRecord for device " + result.getDevice().getAddress());
            return;
        }

        byte[] serviceData = scanRecord.getServiceData(BluetoothScanner.EDDYSTONE_SERVICE_UUID);
        if (serviceData == null) {
            return;
        }

        // We're only interested in the UID frame time since we need the beacon ID to register.
        if (serviceData[0] != EDDYSTONE_UID_FRAME_TYPE) {
            return;
        }

        // Extract the beacon ID from the service data. Offset 0 is the frame type, 1 is the
        // Tx power, and the next 16 are the ID.
        // See https://github.com/google/eddystone/eddystone-uid for more information.
        byte[] id = Arrays.copyOfRange(serviceData, 2, 18);
        if (arrayListContainsId(arrayList, id)) {
            return;
        }

        // Draw it immediately and kick off a async request to fetch the registration status,
        // redrawing when the server returns.
        Log.i(TAG, "id " + StringUtils.toHexString(id) + ", rssi " + result.getRssi());

        Beacon beacon = new Beacon("EDDYSTONE", id, Beacon.Status.STATUS_UNSPECIFIED, result.getRssi());
//        insertIntoListAndFetchStatus(beacon);
    }

    @Override
    public void onScanFailed(int errorCode) {
        Log.e(TAG, "onScanFailed errorCode " + errorCode);
    }

    private boolean arrayListContainsId(ArrayList<Beacon> list, byte[] id) {
        for (Beacon beacon : list) {
            if (Arrays.equals(beacon.getId(), id)) {
                return true;
            }
        }
        return false;
    }

}
