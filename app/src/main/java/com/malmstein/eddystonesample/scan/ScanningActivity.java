package com.malmstein.eddystonesample.scan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.ble.BleManager;
import com.novoda.notils.caster.Views;

public class ScanningActivity extends AppCompatActivity {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BleManager.REQUEST_CODE_ENABLE_BLE) {
            if (resultCode == Activity.RESULT_OK) {
                bleManager.startScan();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(beaconsView, R.string.bluetooth_please_enable, Snackbar.LENGTH_LONG).show();
            }
        }
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

}
