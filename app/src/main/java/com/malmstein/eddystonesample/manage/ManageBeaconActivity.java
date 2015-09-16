package com.malmstein.eddystonesample.manage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import com.malmstein.eddystonesample.model.BeaconConverter;
import com.malmstein.eddystonesample.proximitybeacon.BluetoothScanner;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeaconImpl;
import com.novoda.notils.caster.Views;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageBeaconActivity extends AppCompatActivity implements BeaconLocationView.Listener, BeaconInfoView.Listener, BeaconStabilityDialogFragment.Listener, BeaconDescriptionDialogFragment.Listener, BluetoothScanner.Listener, BeaconAttachmentsView.Listener, BeaconAttachmentDialogFragment.Listener {

    private static final String TAG = "ManageBeaconActivity";
    public static final String KEY_BEACON = BuildConfig.APPLICATION_ID + "EXTRA_BEACON";

    private static final int REQUEST_CODE_PLACE_PICKER = 1003;

    private BeaconInfoView beaconInfoView;
    private BeaconAttachmentsView beaconAttachmentsView;
    private BeaconLocationView beaconLocationView;
    private BeaconConverter beaconConverter = new BeaconConverter();

    private Beacon beacon;
    private ProximityBeaconImpl proximityBeacon;
    private BluetoothScanner bluetoothScanner;

    private String namespace;

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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return false;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent updatedBeacon = new Intent();
        updatedBeacon.putExtra(KEY_BEACON, beacon);
        setResult(Activity.RESULT_OK, updatedBeacon);
        super.onBackPressed();
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
        bluetoothScanner = new BluetoothScanner(proximityBeacon, this);
        beacon = getBeacon();
        fetchNamespace();
    }

    private void fetchNamespace() {
        Callback listNamespacesCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "Failed request: " + request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(body);
                        JSONArray namespaces = json.getJSONArray("namespaces");
                        String tmp = namespaces.getJSONObject(0).getString("namespaceName");
                        if (tmp.startsWith("namespaces/")) {
                            namespace = tmp.substring("namespaces/".length());
                        } else {
                            namespace = tmp;
                        }
                        updateWith(beacon);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException", e);
                    }
                } else {
                    Log.d(TAG, "Unsuccessful listNamespaces request: " + body);
                }
            }
        };
        proximityBeacon.listNamespaces(listNamespacesCallback);
    }

    private void updateWith(Beacon beacon) {
        beaconInfoView.updateWith(beacon, this);
        beaconLocationView.updateWith(beacon, this);
        beaconAttachmentsView.updateWith(beacon, namespace, this);
    }

    @Override
    public void onRequestPlacePicker(LatLng latLng) {
        try {
            startPlacePickerIntnet(latLng);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
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
        JSONObject json;
        try {
            json = beaconConverter.toJson(beacon);
        } catch (JSONException e) {
            Log.d(TAG, "JSONException in creating update request", e);
            return;
        }
        Snackbar.make(beaconInfoView, R.string.manage_beacon_update, Snackbar.LENGTH_LONG).show();
        proximityBeacon.updateBeacon(updateBeaconCallback, beacon.getBeaconName(), json);
    }

    Callback updateBeaconCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            Log.d(TAG, "Failed request: " + request, e);
            Snackbar.make(beaconInfoView, R.string.manage_beacon_update_failure, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            String body = response.body().string();
            if (response.isSuccessful()) {
                bluetoothScanner.fetchBeaconStatus(beacon);
            } else {
                Log.d(TAG, "Unsuccessful updateBeacon request: " + body);
                Snackbar.make(beaconInfoView, R.string.manage_beacon_update_failure, Snackbar.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onChangeStability() {
        DialogFragment newFragment = new BeaconStabilityDialogFragment();
        newFragment.show(getSupportFragmentManager(), "BeaconStability");
    }

    @Override
    public void onChangeDescription() {
        DialogFragment newFragment = new BeaconDescriptionDialogFragment();
        newFragment.show(getSupportFragmentManager(), "BeaconDescription");
    }

    @Override
    public void onShowAttachmentDialog() {
        DialogFragment newFragment = new BeaconAttachmentDialogFragment();
        newFragment.show(getSupportFragmentManager(), "BeaconAttachment");
    }

    @Override
    public void onRegisterBeacon() {
        try {
            JSONObject activeBeacon = beaconConverter.toJson(beacon).put("status", Beacon.Status.ACTIVE.name());
            Snackbar.make(beaconInfoView, R.string.manage_beacon_update, Snackbar.LENGTH_LONG).show();
            proximityBeacon.registerBeacon(updateBeaconCallback, activeBeacon);
        } catch (JSONException e) {
            Log.d(TAG, "Unsuccessful onRegisterBeacon request: " + e.getMessage());
        }
    }

    @Override
    public void onDeactivateBeacon() {
        Snackbar.make(beaconInfoView, R.string.manage_beacon_update, Snackbar.LENGTH_LONG).show();
        proximityBeacon.deactivateBeacon(updateBeaconCallback, beacon.getBeaconName());
    }

    @Override
    public void onActivateBeacon() {
        Snackbar.make(beaconInfoView, R.string.manage_beacon_update, Snackbar.LENGTH_LONG).show();
        proximityBeacon.activateBeacon(updateBeaconCallback, beacon.getBeaconName());
    }

    @Override
    public void onDecommissionBeacon() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_decommission_title)
                .setMessage(R.string.dialog_decommission_message)
                .setPositiveButton(R.string.action_decommission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(beaconInfoView, R.string.manage_beacon_update, Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                        Callback decommissionCallback = new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.d(TAG, "Failed request: " + request, e);
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                if (response.isSuccessful()) {
//                                    beacon.status = Beacon.STATUS_DECOMMISSIONED;
//                                    updateBeacon();

                                    bluetoothScanner.fetchBeaconStatus(beacon);
                                } else {
                                    String body = response.body().string();
                                    Log.d(TAG, "Unsuccessful decommissionBeacon request: " + body);
                                }
                            }
                        };
                        proximityBeacon.decommissionBeacon(decommissionCallback, beacon.getBeaconName());
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onAddAttachment(String type, String data) {
        beaconAttachmentsView.addAttachment(proximityBeacon, type, data);
    }

    @Override
    public void onBeaconStabilityChanged(String newStability) {
        beacon.setExpectedStability(newStability);
        updateRemoteBeacon();
    }

    @Override
    public void onBeaconDescriptionChanged(String description) {
        beacon.setDescription(description);
        updateRemoteBeacon();
    }

    @Override
    public void onBeaconScanned(Beacon updatedBeacon) {
        beacon = updatedBeacon;
        updateWith(beacon);
        Snackbar.make(beaconInfoView, R.string.manage_beacon_update_complete, Snackbar.LENGTH_LONG).show();
    }

}
