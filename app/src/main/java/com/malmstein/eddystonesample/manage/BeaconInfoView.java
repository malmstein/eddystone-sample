package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

public class BeaconInfoView extends CardView {

    private Listener listener;
    private TextView beaconType;
    private TextView beaconId;
    private TextView beaconStatus;
    private TextView beaconStability;
    private TextView beaconDescription;

    public BeaconInfoView(Context context) {
        super(context);
    }

    public BeaconInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeaconInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_info, this, true);

        beaconType = Views.findById(this, R.id.beacon_info_type);
        beaconId = Views.findById(this, R.id.beacon_info_id);
        beaconStatus = Views.findById(this, R.id.beacon_info_status);
        beaconStability = Views.findById(this, R.id.beacon_info_stability);
        beaconDescription = Views.findById(this, R.id.beacon_info_description);
    }

    public void updateWith(Beacon beacon, Listener listener) {
        this.listener = listener;
        beaconType.setText(beacon.getType());
        beaconId.setText(beacon.getHexId());
        beaconStatus.setText(beacon.getStatus().name());
        beaconDescription.setText(beacon.getDescription());

        bindStability(beacon);
    }

    private void bindStability(final Beacon beacon) {
        beaconStability.setText(beacon.getExpectedStability() == null ?
                getContext().getResources().getString(R.string.beacon_stability) :
                beacon.getExpectedStability());
        beaconStability.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShowStabilityDialog(beacon.getExpectedStability());
            }
        });
    }

    public interface Listener {
        void onShowStabilityDialog(String expectedStability);
    }

}
