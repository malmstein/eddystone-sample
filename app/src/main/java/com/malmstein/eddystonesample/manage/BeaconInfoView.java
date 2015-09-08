package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.malmstein.eddystonesample.R;

public class BeaconInfoView extends CardView {

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
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_info, null);

    }
}
