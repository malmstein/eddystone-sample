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

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.model.Beacon;

import java.util.List;

public class BeaconArrayAdapter extends ArrayAdapter<Beacon> {

    private static final int BLACK = Color.rgb(0, 0, 0);
    private static final int GREEN = Color.rgb(0, 142, 9);
    private static final int ORANGE = Color.rgb(255, 165, 0);
    private static final int RED = Color.rgb(255, 5, 5);
    private static final int GREY = Color.rgb(150, 150, 150);

    public BeaconArrayAdapter(Context context, int resource, List<Beacon> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Beacon beacon = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_item, parent, false);
        }
        ImageView registrationStatus = (ImageView) convertView.findViewById(R.id.beacon_item_status);
        TextView beaconId = (TextView) convertView.findViewById(R.id.beacon_item_id);
        TextView beaconStatus = (TextView) convertView.findViewById(R.id.beacon_item_status);

        beaconId.setText(beacon.getHexId());
        beaconStatus.setText(beacon.getStatus().name());

        switch (beacon.getStatus()) {
            case UNREGISTERED:
                registrationStatus.setImageResource(R.drawable.ic_action_lock_open);
                registrationStatus.setColorFilter(BLACK);
                beaconId.setTextColor(BLACK);
                break;
            case STATUS_ACTIVE:
                registrationStatus.setImageResource(R.drawable.ic_action_check_circle);
                registrationStatus.setColorFilter(GREEN);
                beaconId.setTextColor(BLACK);
                break;
            case STATUS_INACTIVE:
                registrationStatus.setImageResource(R.drawable.ic_action_check_circle);
                registrationStatus.setColorFilter(ORANGE);
                beaconId.setTextColor(BLACK);
                break;
            case STATUS_DECOMMISSIONED:
                registrationStatus.setImageResource(R.drawable.ic_action_highlight_off);
                registrationStatus.setColorFilter(RED);
                beaconId.setTextColor(GREY);
                break;
            case NOT_AUTHORIZED:
                registrationStatus.setImageResource(R.drawable.ic_action_lock);
                registrationStatus.setColorFilter(GREY);
                beaconId.setTextColor(GREY);
                break;
            case STATUS_UNSPECIFIED:
                registrationStatus.setImageResource(R.drawable.ic_action_help);
                registrationStatus.setColorFilter(GREY);
                beaconId.setTextColor(GREY);
                break;
            default:
                registrationStatus.setImageResource(R.drawable.ic_action_help);
                registrationStatus.setColorFilter(BLACK);
                beaconId.setTextColor(BLACK);
                break;
        }

        return convertView;
    }
}
