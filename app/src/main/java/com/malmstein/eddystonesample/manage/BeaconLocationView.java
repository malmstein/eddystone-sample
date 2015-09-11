package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

public class BeaconLocationView extends CardView {

    private TextView beaconCoordinates;
    private TextView beaconPlace;
    private ImageView beaconMap;
    private Listener listener;

    public BeaconLocationView(Context context) {
        super(context);
    }

    public BeaconLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeaconLocationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_location, this, true);

        beaconPlace = Views.findById(this, R.id.beacon_info_place_id);
        beaconCoordinates = Views.findById(this, R.id.beacon_info_coordinates);
        beaconMap = Views.findById(this, R.id.beacon_info_map);
    }

    public void updateWith(Beacon beacon, Listener listener) {
        this.listener = listener;
        if (beacon.getPlaceId() != null) {
            beaconPlace.setText(beacon.getPlaceId());
        }
        if (beacon.getLatLng() != null) {
            beaconCoordinates.setText(beacon.getLatLng().toString());
        }

        bindMap(beacon);
    }

    private void bindMap(final Beacon beacon) {
        if (beacon.getLatLng() != null) {
            updateStaticMapPicture(beacon.getLatLng());
        }

        beaconMap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRequestPlacePicker(beacon.getLatLng());
            }
        });
    }

    private void updateStaticMapPicture(LatLng latLng) {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/staticmap?size=%1dx%2d&scale=2&markers=%.6f,%.6f",
                beaconMap.getWidth(), beaconMap.getHeight(), latLng.latitude, latLng.longitude);
        new FetchStaticMapTask(beaconMap).execute(url);
    }

    public void addPlace(Place place) {
        beaconPlace.setText(place.getId());
        beaconCoordinates.setText(place.getLatLng().toString());
        updateStaticMapPicture(place.getLatLng());
    }

    public interface Listener {
        void onRequestPlacePicker(LatLng latLng);
    }
}
