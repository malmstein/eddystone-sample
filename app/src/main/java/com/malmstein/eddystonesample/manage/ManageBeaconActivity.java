package com.malmstein.eddystonesample.manage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.malmstein.eddystonesample.BuildConfig;
import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

public class ManageBeaconActivity extends AppCompatActivity {

    public static final String KEY_BEACON = BuildConfig.APPLICATION_ID + "EXTRA_BEACON";
    private BeaconInfoView beaconInfoView;
    private BeaconAttachmentsView beaconAttachmentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        setupToolbar();

        beaconInfoView = Views.findById(this, R.id.beacon_info_view);
        beaconAttachmentsView = Views.findById(this, R.id.beacon_attachments_view);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        beaconInfoView.updateWith(getBeacon());
        beaconAttachmentsView.updateWith(getBeacon());
    }

    private Beacon getBeacon(){
        return (Beacon) getIntent().getExtras().getSerializable(KEY_BEACON);
    }
}
