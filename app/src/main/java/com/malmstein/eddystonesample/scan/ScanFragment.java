package com.malmstein.eddystonesample.scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.account.AccountSharedPreferences;
import com.malmstein.eddystonesample.manage.ManageBeaconActivity;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;

public class ScanFragment extends Fragment implements BeaconsAdapter.Listener {

    private BeaconsAdapter beaconsAdapter;
    private RecyclerView beaconsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        beaconsList = Views.findById(rootView, R.id.beacons_list);
        beaconsList.setHasFixedSize(true);
        beaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        beaconsList.addItemDecoration(new SimpleDividerItemDecoration(getContext()));

        beaconsAdapter = new BeaconsAdapter(new ArrayList<Beacon>(), this);
        beaconsList.setAdapter(beaconsAdapter);

        return rootView;
    }

    public void updateWith(Beacon beacon) {
        beaconsAdapter.updateWith(beacon);
    }

    public void reset() {
        beaconsAdapter.clear();
    }

    @Override
    public void onBeaconClicked(Beacon beacon) {
        AccountSharedPreferences accountSharedPreferences = AccountSharedPreferences.newInstance(getActivity());
        if (accountSharedPreferences.isLoggedIn()){
            Intent manageBeacon = new Intent(getActivity(), ManageBeaconActivity.class);
            manageBeacon.putExtra(ManageBeaconActivity.KEY_BEACON, beacon);
            startActivity(manageBeacon);
        } else {
            Snackbar.make(beaconsList, getString(R.string.choose_account), Snackbar.LENGTH_LONG).show();
        }
    }
}
