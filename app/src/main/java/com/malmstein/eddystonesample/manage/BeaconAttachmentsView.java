package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

public class BeaconAttachmentsView extends FrameLayout {

    private TextView addAttachment;

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

        addAttachment = Views.findById(this, R.id.beacon_attachments_add);
    }

    public void updateWith(Beacon beacon) {
        updateVisibility(beacon);
        bindClickListener(beacon);
    }

    private void bindClickListener(Beacon beacon) {
        addAttachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void updateVisibility(Beacon beacon) {
        if (beacon.getStatus().equals(Beacon.Status.UNREGISTERED)) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }
}
