package com.malmstein.eddystonesample.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.malmstein.eddystonesample.BuildConfig;
import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.account.AccountSharedPreferences;
import com.malmstein.eddystonesample.model.Beacon;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeaconImpl;
import com.novoda.notils.caster.Views;

public class ManageBeaconActivity extends AppCompatActivity implements BeaconLocationView.Listener {

    public static final String KEY_BEACON = BuildConfig.APPLICATION_ID + "EXTRA_BEACON";

    private static final int REQUEST_CODE_PLACE_PICKER = 1003;

    private BeaconInfoView beaconInfoView;
    private BeaconAttachmentsView beaconAttachmentsView;
    private BeaconLocationView beaconLocationView;
    private ProximityBeaconImpl proximityBeacon;

    private Beacon getBeacon() {
        return (Beacon) getIntent().getExtras().getSerializable(KEY_BEACON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        setupToolbar();

        beaconInfoView = Views.findById(this, R.id.beacon_info_view);
        beaconAttachmentsView = Views.findById(this, R.id.beacon_attachments_view);
        beaconLocationView = Views.findById(this, R.id.beacon_location_view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                beaconLocationView.addPlace(place);
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.activity_manage);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        proximityBeacon = new ProximityBeaconImpl(this, AccountSharedPreferences.newInstance(this).getAccount());
        beaconInfoView.updateWith(getBeacon());
        beaconLocationView.updateWith(getBeacon(), this);
        beaconAttachmentsView.updateWith(getBeacon());
        fetchNamespace();
    }

    private void fetchNamespace() {

    }

    @Override
    public void onRequestPlacePicker(LatLng latLng) {
        try {
            startPlacePickerIntnet(latLng);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void startPlacePickerIntnet(LatLng latLng) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        if (latLng != null) {
            builder.setLatLngBounds(new LatLngBounds(latLng, latLng));
        }
        startActivityForResult(builder.build(this), REQUEST_CODE_PLACE_PICKER);
    }
}
