package com.malmstein.eddystonesample.scan;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.malmstein.eddystonesample.view.SimpleDividerItemDecoration;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;

public class BeaconsView extends FrameLayout {

    private BeaconsAdapter beaconsAdapter;
    private RecyclerView beaconsList;
    private RecyclerView.LayoutManager beaconsLayoutManager;

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
        beaconsList.setHasFixedSize(true);
        beaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        beaconsList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        beaconsAdapter = new BeaconsAdapter(new ArrayList<Beacon>());
        beaconsList.setAdapter(beaconsAdapter);
    }

    public void updateWith(Beacon beacon) {
        beaconsAdapter.updateWith(beacon);
    }

}
