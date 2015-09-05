package com.malmstein.eddystonesample.scan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

public class BeaconsView extends FrameLayout {

    private static final String TAG = BeaconsView.class.getSimpleName();

    private RecyclerView beaconsList;

    public BeaconsView(Context context) {
        super(context);
    }

    public BeaconsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeaconsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.view_beacons, this, true);
        beaconsList = Views.findById(this, R.id.beacons_list);
    }

    public void updateWith(Beacon beacon) {

    }

}
