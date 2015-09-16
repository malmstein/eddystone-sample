package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;

public class BeaconAttachmentsView extends FrameLayout {

    public BeaconAttachmentsView(Context context) {
        super(context);
    }

    public BeaconAttachmentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeaconAttachmentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_attachments, this, true);
    }

    public void updateWith(Beacon beacon) {
        updateVisibility(beacon);
    }

    private void updateVisibility(Beacon beacon) {
        if (beacon.getStatus().equals(Beacon.Status.UNREGISTERED)) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }
}
