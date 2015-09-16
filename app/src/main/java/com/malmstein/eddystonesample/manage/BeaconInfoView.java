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
    private TextView beaconAction;

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
        beaconAction = Views.findById(this, R.id.beacon_info_action);
    }

    public void updateWith(Beacon beacon, Listener listener) {
        this.listener = listener;
        beaconType.setText(beacon.getType());
        beaconId.setText(beacon.getHexId());

        bindDescription(beacon);
        bindStability(beacon);
        bindStatus(beacon.getStatus());
    }

    private void bindDescription(final Beacon beacon) {
        beaconDescription.setText(beacon.getDescription());
        beaconDescription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeDescription();
            }
        });
    }

    private void bindStability(final Beacon beacon) {
        beaconStability.setText(beacon.getExpectedStability() == null ?
                getContext().getResources().getString(R.string.beacon_stability) :
                beacon.getExpectedStability());
        beaconStability.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangeStability();
            }
        });
    }

    private void bindStatus(Beacon.Status status) {
        beaconStatus.setText(status.name());

        switch (status) {
            case UNREGISTERED:
                enableRegister();
                break;
            case ACTIVE:
                enableDeactivate();
                decommissionButton.setEnabled(false);
                decommissionButton.setVisibility(View.GONE);
                break;
            case INACTIVE:
                enableActivate();
                decommissionButton.setEnabled(true);
                decommissionButton.setVisibility(View.VISIBLE);
                break;
            case DECOMMISSIONED:
                actionButton.setVisibility(View.GONE);
                decommissionButton.setEnabled(false);
                decommissionButton.setVisibility(View.GONE);
                break;
        }
    }

    private void enableRegister() {
        beaconAction.setText(R.string.action_register);
        beaconAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRegisterBeacon();
            }
        });
    }

    private void enableDeactivate() {
        beaconAction.setText(R.string.action_deactivate);
        beaconAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeactivateBeacon();
            }
        });
    }

    private void enableActivate() {
        beaconAction.setText(R.string.action_activate);
        beaconAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActivateBeacon();
            }
        });
    }

    public interface Listener {
        void onChangeStability();

        void onChangeDescription();

        void onRegisterBeacon();

        void onDeactivateBeacon();

        void onActivateBeacon();

        void onChangeStatus();
    }

}
