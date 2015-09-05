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

package com.malmstein.eddystonesample.model;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.malmstein.eddystonesample.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Beacon {

    public enum Status {
        STATUS_UNSPECIFIED,
        STATUS_ACTIVE,
        STATUS_INACTIVE,
        STATUS_DECOMMISSIONED,
        STABILITY_UNSPECIFIED,
        UNREGISTERED,
        NOT_AUTHORIZED
    }

    String type;
    byte[] id;
    Status status;
    String placeId;
    Double latitude;
    Double longitude;
    String expectedStability;
    String description;
    int rssi;

    public Beacon(String type, byte[] id, Status status, int rssi) {
        this.type = type;
        this.id = id;
        this.status = status;
        this.placeId = null;
        this.latitude = null;
        this.longitude = null;
        this.expectedStability = null;
        this.description = null;
        this.rssi = rssi;
    }

    public Beacon(JSONObject response) {
        try {
            JSONObject json = response.getJSONObject("advertisedId");
            type = json.getString("type");
            id = StringUtils.base64Decode(json.getString("id"));
        } catch (JSONException e) {
            // NOP
        }

        try {
            status = Status.valueOf(response.getString("status"));
        } catch (JSONException e) {
            status = Status.STATUS_UNSPECIFIED;
        }

        try {
            placeId = response.getString("placeId");
        } catch (JSONException e) {
            // NOP
        }

        try {
            JSONObject latLngJson = response.getJSONObject("latLng");
            latitude = latLngJson.getDouble("latitude");
            longitude = latLngJson.getDouble("longitude");
        } catch (JSONException e) {
            latitude = null;
            longitude = null;
        }

        try {
            expectedStability = response.getString("expectedStability");
        } catch (JSONException e) {
            // NOP
        }

        try {
            description = response.getString("description");
        } catch (JSONException e) {
            // NOP
        }

    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject advertisedId = new JSONObject()
                .put("type", type)
                .put("id", StringUtils.base64Encode(id));
        json.put("advertisedId", advertisedId);
        if (!status.equals(Status.STATUS_UNSPECIFIED)) {
            json.put("status", status);
        }
        if (placeId != null) {
            json.put("placeId", placeId);
        }
        if (latitude != null && longitude != null) {
            JSONObject latLng = new JSONObject();
            latLng.put("latitude", latitude);
            latLng.put("longitude", longitude);
            json.put("latLng", latLng);
        }
        if (expectedStability != null && !expectedStability.equals(Status.STABILITY_UNSPECIFIED)) {
            json.put("expectedStability", expectedStability);
        }
        if (description != null) {
            json.put("description", description);
        }
        // TODO: beacon properties
        return json;
    }

    public String getHexId() {
        return StringUtils.toHexString(id);
    }

    /**
     * The beaconName is formatted as "beacons/%d!%s" where %d is an integer representing the
     * beacon ID type. For Eddystone this is 3. The %s is the base16 (hex) ASCII for the ID bytes.
     */
    public String getBeaconName() {
        return String.format("beacons/3!%s", getHexId());
    }

    @Nullable
    public LatLng getLatLng() {
        if (latitude == null || longitude == null) {
            return null;
        }
        return new LatLng(latitude, longitude);
    }

    public byte[] getId() {
        return id;
    }

}
