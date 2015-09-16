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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.malmstein.eddystonesample.StringUtils;

import java.io.Serializable;

public class Beacon implements Serializable {

    public enum Status {
        UNSPECIFIED,
        ACTIVE,
        INACTIVE,
        DECOMMISSIONED,
        STABILITY_UNSPECIFIED,
        UNREGISTERED,
        NOT_AUTHORIZED
    }

    private String type;
    private byte[] id;
    private Status status;
    private String placeId;
    private Double latitude;
    private Double longitude;
    private String expectedStability;
    private String description;

    public Beacon(String type, byte[] id, Status status, String placeId, Double latitude, Double longitude, String stability, String description) {
        this.type = type;
        this.id = id;
        this.status = status;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expectedStability = stability;
        this.description = description;
    }

    public Beacon(String type, byte[] id, Status status) {
        this.type = type;
        this.id = id;
        this.status = status;
        this.placeId = null;
        this.latitude = null;
        this.longitude = null;
        this.expectedStability = null;
        this.description = null;
    }

    public Beacon(String type, byte[] id, Status status, Place place) {
        this.type = type;
        this.id = id;
        this.status = status;
        this.placeId = place.getId();
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
        this.expectedStability = null;
        this.description = null;
    }

    public String getHexId() {
        if (id != null) {
            return StringUtils.toHexString(id);
        } else {
            return "";
        }
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

    public String getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public static Beacon from(byte[] id) {
        return new Beacon("EDDYSTONE", id, Beacon.Status.UNSPECIFIED);
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedStability() {
        return expectedStability;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlace(Place place) {
        placeId = place.getId();
        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
    }

    public void setExpectedStability(String newStability) {
        expectedStability = newStability;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
