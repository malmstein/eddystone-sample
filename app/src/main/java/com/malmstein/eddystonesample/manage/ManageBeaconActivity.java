package com.malmstein.eddystonesample.manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class ManageBeaconActivity extends AppCompatActivity implements BeaconLocationView.Listener {

    private static final String TAG = "ManageBeaconActivity";
    public static final String KEY_BEACON = BuildConfig.APPLICATION_ID + "EXTRA_BEACON";

    private static final int REQUEST_CODE_PLACE_PICKER = 1003;

    private BeaconInfoView beaconInfoView;
    private BeaconAttachmentsView beaconAttachmentsView;
    private BeaconLocationView beaconLocationView;

    private Beacon beacon;
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                beaconLocationView.addPlace(place);
                beacon.setPlace(place);
                updateRemoteBeacon();
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
        beacon = getBeacon();
        updateWith(beacon);
        fetchNamespace();
    }

    private void updateWith(Beacon beacon) {
        beaconInfoView.updateWith(beacon);
        beaconLocationView.updateWith(beacon, this);
        beaconAttachmentsView.updateWith(beacon);
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

    private void updateRemoteBeacon() {
        Callback updateBeaconCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "Failed request: " + request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        beacon = new Beacon(new JSONObject(body));
                    } catch (JSONException e) {
                        Log.d(TAG, "Failed JSON creation from response: " + body, e);
                        return;
                    }
                    updateWith(beacon);
                } else {
                    Log.d(TAG, "Unsuccessful updateBeacon request: " + body);
                }
            }
        };

        JSONObject json;
        try {
            json = beacon.toJson();
        } catch (JSONException e) {
            Log.d(TAG, "JSONException in creating update request", e);
            return;
        }

        proximityBeacon.updateBeacon(updateBeaconCallback, beacon.getBeaconName(), json);
    }
}
