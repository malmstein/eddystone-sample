package com.malmstein.eddystonesample.scan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;

public class ScanFragment extends Fragment {

    private BeaconsAdapter beaconsAdapter;
    private RecyclerView beaconsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        beaconsList = Views.findById(rootView, R.id.beacons_list);
        beaconsList.setHasFixedSize(true);
        beaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        beaconsList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        beaconsAdapter = new BeaconsAdapter(new ArrayList<Beacon>());
        beaconsList.setAdapter(beaconsAdapter);

        return rootView;
    }

    public void updateWith(Beacon beacon) {
        beaconsAdapter.updateWith(beacon);
    }

    public void reset() {
        beaconsAdapter.clear();
    }
}
