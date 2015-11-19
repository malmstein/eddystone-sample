package com.malmstein.eddystonesample;

import android.accounts.AccountManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.common.AccountPicker;
import com.malmstein.eddystonesample.account.AccountFragment;
import com.malmstein.eddystonesample.account.AccountSharedPreferences;
import com.malmstein.eddystonesample.model.Beacon;
import com.malmstein.eddystonesample.proximitybeacon.BluetoothScanner;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeacon;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeaconImpl;
import com.malmstein.eddystonesample.scan.ScanFragment;
import com.novoda.notils.caster.Views;

public class EddystoneActivity extends AppCompatActivity implements BluetoothScanner.Listener, AccountFragment.Listener {

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_ENABLE_BLE = 1001;

    private static final long SCAN_TIME_MILLIS = 10000;
    private static final Handler handler = new Handler(Looper.getMainLooper());

    private ProximityBeacon proximityBeacon;
    private BluetoothScanner bluetoothScanner;

    private FloatingActionButton scanFab;
    private FABProgressCircle progressCircle;

    private ViewPager viewPager;
    private EddystonePagerAdapter eddystonePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        scanFab = Views.findById(this, R.id.action_scan);
        progressCircle = Views.findById(this, R.id.action_scan_progress_circle);

        setupToolbar();
        setupHeaders();
        setupTabs();
        setupActions();
    }

    private void setupActions() {
        scanFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanOrRequestPermission();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.activity_scanning);
    }

    private void setupHeaders() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        eddystonePagerAdapter = new EddystonePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(eddystonePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    scanFab.show();
                } else {
                    scanFab.hide();
                }
            }
        });
    }

    private void setupTabs() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                findAccountFragment().signInUser(name);
                Snackbar.make(viewPager, getString(R.string.use_account, name), Snackbar.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(viewPager, R.string.please_pick_account, Snackbar.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_ENABLE_BLE) {
            if (resultCode == Activity.RESULT_OK) {
                scanOrRequestPermission();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(viewPager, R.string.bluetooth_please_enable, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void scanOrRequestPermission() {
        BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = btManager.getAdapter();

        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BLE);
        } else {
            startScanning(btAdapter);
        }
    }

    private void startScanning(BluetoothAdapter btAdapter) {
        ScanFragment scanFragment = findScanFragment();
        scanFragment.reset();

        progressCircle.show();
        proximityBeacon = new ProximityBeaconImpl(this, AccountSharedPreferences.newInstance(this).getAccount());
        bluetoothScanner = new BluetoothScanner(proximityBeacon, this);
        bluetoothScanner.startScan(btAdapter);
        Runnable stopScanning = new Runnable() {
            @Override
            public void run() {
                progressCircle.hide();
                bluetoothScanner.stopScan();
            }
        };
        handler.postDelayed(stopScanning, SCAN_TIME_MILLIS);
    }

    private ScanFragment findScanFragment() {
        return (ScanFragment) getSupportFragmentManager().findFragmentByTag(eddystonePagerAdapter.getTag(EddystonePagerAdapter.POSITION_SCAN));
    }

    private AccountFragment findAccountFragment() {
        return (AccountFragment) getSupportFragmentManager().findFragmentByTag(eddystonePagerAdapter.getTag(EddystonePagerAdapter.POSITION_ACCOUNT));
    }

    @Override
    public void onBeaconScanned(Beacon fetchedBeacon) {
        ScanFragment scanFragment = findScanFragment();
        scanFragment.updateWith(fetchedBeacon);
    }

    @Override
    public void onSignInClicked() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(
                null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    public void onSignOutClicked() {
        proximityBeacon = null;
    }

}
