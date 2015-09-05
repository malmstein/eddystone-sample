package com.malmstein.eddystonesample.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanner {

    private static final String TAG = BluetoothScanner.class.getSimpleName();

    // The Eddystone Service UUID, 0xFEAA.
    public static final ParcelUuid EDDYSTONE_SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");

    private static List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(EDDYSTONE_SCAN_FILTER);
        return scanFilters;
    }

    // A filter that scans only for devices with the Eddystone Service UUID.
    private static final ScanFilter EDDYSTONE_SCAN_FILTER = new ScanFilter.Builder()
            .setServiceUuid(EDDYSTONE_SERVICE_UUID)
            .build();

    public static final List<ScanFilter> SCAN_FILTERS = buildScanFilters();

    // An aggressive scan for nearby devices that reports immediately.
    public static final ScanSettings SCAN_SETTINGS =
            new ScanSettings.Builder().
                    setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(0)
                    .build();

    private BluetoothLeScanner scanner;
    private BleScanCallback bleScanCallback;
    private final Activity activity;

    public BluetoothScanner(Activity activity) {
        this.activity = activity;
        this.bleScanCallback = new BleScanCallback();
    }

    public void startScan(BluetoothAdapter btAdapter) {
        scanner = btAdapter.getBluetoothLeScanner();
        scanner.startScan(SCAN_FILTERS, SCAN_SETTINGS, bleScanCallback);
    }

}
