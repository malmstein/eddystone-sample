package com.malmstein.eddystonesample.proximitybeacon;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Eddystone {

    private static final String TAG = Eddystone.class.getSimpleName();

    // The Eddystone-UID frame type byte https://github.com/google/eddystone
    public static final byte EDDYSTONE_UID_FRAME_TYPE = 0x00;

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

    // Extract the beacon ID from the service data. Offset 0 is the frame type, 1 is the
    // Tx power, and the next 16 are the ID.
    // See https://github.com/google/eddystone/eddystone-uid for more information.
    public static byte[] getId(ScanRecord scanRecord) {
        byte[] serviceData = scanRecord.getServiceData(Eddystone.EDDYSTONE_SERVICE_UUID);
        return Arrays.copyOfRange(serviceData, 2, 18);
    }

    public static boolean isValid(ScanResult result) {
        ScanRecord scanRecord = result.getScanRecord();
        if (result.getScanRecord() == null) {
            Log.w(TAG, "Null ScanRecord for device " + result.getDevice().getAddress());
            return false;
        }

        byte[] serviceData = scanRecord.getServiceData(Eddystone.EDDYSTONE_SERVICE_UUID);
        if (serviceData == null) {
            return false;
        }

        if (serviceData[0] != Eddystone.EDDYSTONE_UID_FRAME_TYPE) {
            return false;
        }

        return true;

    }

}
