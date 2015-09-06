package com.malmstein.eddystonesample.scan;

import android.accounts.AccountManager;
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

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.common.AccountPicker;
import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.malmstein.eddystonesample.proximitybeacon.BluetoothScanner;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeacon;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeaconImpl;
import com.novoda.notils.caster.Views;

public class ScanningActivity extends AppCompatActivity implements BluetoothScanner.Listener, AccountView.Listener {

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_ENABLE_BLE = 1001;

    private ProximityBeacon proximityBeacon;
    private BluetoothScanner bluetoothScanner;

    private AccountView accountView;
    private BeaconsView beaconsView;
    private FloatingActionButton scanFab;
    private FABProgressCircle progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        beaconsView = Views.findById(this, R.id.beacons_view);
        scanFab = Views.findById(this, R.id.action_scan);
        progressCircle = Views.findById(this, R.id.action_scan_progress_circle);

        setupToolbar();
        setupActions();
        setupAccount();
    }

    private void setupActions() {
        scanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressCircle.show();
                scanOrRequestPermission();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.activity_scanning);
    }

    private void setupAccount() {
        accountView = Views.findById(this, R.id.accounts_view);
        accountView.setup(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Snackbar.make(accountView, getString(R.string.use_account, name), Snackbar.LENGTH_LONG).show();
                proximityBeacon = new ProximityBeaconImpl(this, name);
                showBeaconsView();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(accountView, R.string.please_pick_account, Snackbar.LENGTH_LONG).show();
            }
            if (requestCode == REQUEST_CODE_ENABLE_BLE) {
                if (resultCode == Activity.RESULT_OK) {
                    scanOrRequestPermission();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(beaconsView, R.string.bluetooth_please_enable, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showBeaconsView() {
        accountView.setVisibility(View.GONE);
        beaconsView.setVisibility(View.VISIBLE);
        scanFab.show();
    }

    private void scanOrRequestPermission() {
        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        } else {
            bluetoothScanner = new BluetoothScanner(proximityBeacon, this);
            bluetoothScanner.startScan(btAdapter);
        }
    }

    @Override
    public void onBeaconScanned(Beacon fetchedBeacon) {
        beaconsView.updateWith(fetchedBeacon);
    }

    @Override
    public void onAccountSelectorClicked() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(
                null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }
}
