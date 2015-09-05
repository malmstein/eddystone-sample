package com.malmstein.eddystonesample.scan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.ble.BluetoothScanner;
import com.malmstein.eddystonesample.ble.EddystoneFilters;
import com.novoda.notils.caster.Views;

public class ScanningActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ENABLE_BLE = 1001;

    private BluetoothScanner bluetoothScanner;
    private BeaconsView beaconsView;
    private FloatingActionButton scanFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        beaconsView = Views.findById(this, R.id.beacons_view);
        scanFab = Views.findById(this, R.id.action_scan);

        setupToolbar();
        setupActions();
        setupBleManager();
    }

    private void setupBleManager() {
        bluetoothScanner = new BluetoothScanner(this);
    }

    private void setupActions() {
        scanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothScanner.startScan();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.activity_scanning);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BluetoothScanner.REQUEST_CODE_ENABLE_BLE) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothScanner.startScan(btAdapter);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(beaconsView, R.string.bluetooth_please_enable, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void startScan() {
        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        } else {
            bluetoothScanner.startScan(btAdapter);
            scanner = btAdapter.getBluetoothLeScanner();
            scanner.startScan(EddystoneFilters.SCAN_FILTERS, EddystoneFilters.SCAN_SETTINGS, this);
        }
    }

}
