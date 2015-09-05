package com.malmstein.eddystonesample.scan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.ble.BleManager;
import com.novoda.notils.caster.Views;

public class ScanningActivity extends AppCompatActivity implements BleManager.Listener {

    private BleManager bleManager;
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
        bleManager = new BleManager(this);
    }

    private void setupActions() {
        scanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleManager.startScan();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.activity_scanning);
    }

    @Override
    public void onBluetoothNotAvailable() {
        Snackbar.make(beaconsView, R.string.bluetooth_cant_enable, Snackbar.LENGTH_LONG).show();
    }
}
