// Copyright 2015 Google Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.malmstein.eddystonesample.scan;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.Arrays;

public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.ViewHolder> {

    private static final int BLACK = Color.rgb(0, 0, 0);
    private static final int GREEN = Color.rgb(0, 142, 9);
    private static final int ORANGE = Color.rgb(255, 165, 0);
    private static final int RED = Color.rgb(255, 5, 5);
    private static final int GREY = Color.rgb(150, 150, 150);

    private ArrayList<Beacon> beacons;

    public BeaconsAdapter(ArrayList<Beacon> beacons) {
        this.beacons = beacons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_beacon_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BeaconsAdapter.ViewHolder holder, int position) {
        final Beacon beacon = beacons.get(position);

        holder.beaconId.setText(beacon.getHexId());
        holder.beaconStatus.setText(beacon.getStatus().name());
        switch (beacon.getStatus()) {
            case UNREGISTERED:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_lock_open);
                holder.registrationStatus.setColorFilter(BLACK);
                holder.beaconId.setTextColor(BLACK);
                break;
            case STATUS_ACTIVE:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_check_circle);
                holder.registrationStatus.setColorFilter(GREEN);
                holder.beaconId.setTextColor(BLACK);
                break;
            case STATUS_INACTIVE:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_check_circle);
                holder.registrationStatus.setColorFilter(ORANGE);
                holder.beaconId.setTextColor(BLACK);
                break;
            case STATUS_DECOMMISSIONED:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_highlight_off);
                holder.registrationStatus.setColorFilter(RED);
                holder.beaconId.setTextColor(GREY);
                break;
            case NOT_AUTHORIZED:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_lock);
                holder.registrationStatus.setColorFilter(GREY);
                holder.beaconId.setTextColor(GREY);
                break;
            case STATUS_UNSPECIFIED:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_help);
                holder.registrationStatus.setColorFilter(GREY);
                holder.beaconId.setTextColor(GREY);
                break;
            default:
                holder.registrationStatus.setImageResource(R.drawable.ic_action_help);
                holder.registrationStatus.setColorFilter(BLACK);
                holder.beaconId.setTextColor(BLACK);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public void updateWith(Beacon updatedBeacon) {
        if (alreadyScanned(updatedBeacon)) {
            updateBeaconsList(updatedBeacon);
        } else {
            beacons.add(updatedBeacon);
        }
        notifyDataSetChanged();
    }

    private boolean alreadyScanned(Beacon newBeacon) {
        for (Beacon beacon : beacons) {
            if (Arrays.equals(beacon.getId(), newBeacon.getId())) {
                return true;
            }
        }
        return false;
    }

    private void updateBeaconsList(Beacon updatedBeacon) {
        for (Beacon beacon : beacons) {
            if (Arrays.equals(beacon.getId(), updatedBeacon.getId())) {
                beacons.set(beacons.indexOf(beacon), updatedBeacon);
            }
        }
    }

    public void clear() {
        beacons.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView registrationStatus;
        public TextView beaconId;
        public TextView beaconStatus;

        public ViewHolder(View view) {
            super(view);
            registrationStatus = Views.findById(view, R.id.beacon_item_state);
            beaconId = Views.findById(view, R.id.beacon_item_id);
            beaconStatus = Views.findById(view, R.id.beacon_item_status);
        }
    }
}
