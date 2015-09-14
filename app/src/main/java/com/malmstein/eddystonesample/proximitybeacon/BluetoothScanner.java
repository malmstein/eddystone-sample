package com.malmstein.eddystonesample.proximitybeacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.util.Log;

import com.malmstein.eddystonesample.StringUtils;
import com.malmstein.eddystonesample.model.Beacon;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothScanner {

    private static final String TAG = BluetoothScanner.class.getSimpleName();

    private BluetoothLeScanner scanner;
    private ProximityBeacon proximityBeacon;
    private ArrayList<Beacon> arrayList = new ArrayList<>();
    private Listener listener;

    public BluetoothScanner(ProximityBeacon proximityBeacon, Listener listener) {
        this.proximityBeacon = proximityBeacon;
        this.listener = listener;
    }

    public void startScan(BluetoothAdapter btAdapter) {
        scanner = btAdapter.getBluetoothLeScanner();
        scanner.startScan(Eddystone.SCAN_FILTERS, Eddystone.SCAN_SETTINGS, scanCallback);
    }

    public void stopScan() {
        scanner.stopScan(scanCallback);
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

    private void fetchBeaconStatus(final Beacon beacon) {
        if ((proximityBeacon == null)) {
            listener.onBeaconScanned(beacon);
        } else if (proximityBeacon.hasAccount()) {
            proximityBeacon.getBeacon(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e(TAG, String.format("Failed request: %s, IOException %s", request, e));
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Beacon fetchedBeacon;
                    switch (response.code()) {
                        case 200:
                            try {
                                String body = response.body().string();
                                fetchedBeacon = new Beacon(new JSONObject(body));
                            } catch (JSONException e) {
                                Log.e(TAG, "JSONException", e);
                                return;
                            }
                            break;
                        case 403:
                            fetchedBeacon = new Beacon(beacon.getType(), beacon.getId(), Beacon.Status.NOT_AUTHORIZED, beacon.getRssi());
                            break;
                        case 404:
                            fetchedBeacon = new Beacon(beacon.getType(), beacon.getId(), Beacon.Status.UNREGISTERED, beacon.getRssi());
                            break;
                        default:
                            Log.e(TAG, "Unhandled beacon service response: " + response);
                            return;
                    }
                    updateBeaconsList(fetchedBeacon);
                    listener.onBeaconScanned(fetchedBeacon);
                }
            }, beacon.getBeaconName());
        } else {
            listener.onBeaconScanned(beacon);
        }
    }

    private void updateBeaconsList(Beacon updatedBeacon) {
        ArrayList<Beacon> beacons = arrayList;
        for (Beacon beacon : beacons) {
            if (Arrays.equals(beacon.getId(), updatedBeacon.getId())) {
                arrayList.set(beacons.indexOf(beacon), updatedBeacon);
            }
        }
    }

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Beacon scannedBeacon = validateScan(result);
            if (scannedBeacon != null) {
                fetchBeaconStatus(scannedBeacon);
            }
        }
    };

    public interface Listener {
        void onBeaconScanned(Beacon beacon);
    }

}
