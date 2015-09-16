package com.malmstein.eddystonesample.model;

import com.malmstein.eddystonesample.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BeaconConverter {

    public Beacon from(JSONObject response) {
        String type = null;
        byte[] id = new byte[0];
        Beacon.Status status;
        String placeId = null;
        Double longitude;
        Double latitude;
        String expectedStability = null;
        String description = null;

        try {
            JSONObject json = response.getJSONObject("advertisedId");
            type = json.getString("type");
            id = StringUtils.base64Decode(json.getString("id"));
            expectedStability = response.getString("expectedStability");
            description = response.getString("description");
        } catch (JSONException e) {
            // NOP
        }

        try {
            status = Beacon.Status.valueOf(response.getString("status"));
        } catch (JSONException e) {
            status = Beacon.Status.UNSPECIFIED;
        }

        try {
            placeId = response.getString("placeId");
            JSONObject latLngJson = response.getJSONObject("latLng");
            latitude = latLngJson.getDouble("latitude");
            longitude = latLngJson.getDouble("longitude");
        } catch (JSONException e) {
            latitude = null;
            longitude = null;
        }

        return new Beacon(type, id, status, placeId, latitude, longitude, expectedStability, description);

    }

    public JSONObject toJson(Beacon beacon) throws JSONException {
        JSONObject json = new JSONObject();
        JSONObject advertisedId = new JSONObject()
                .put("type", beacon.getType())
                .put("id", StringUtils.base64Encode(beacon.getId()));
        json.put("advertisedId", advertisedId);
        if (!beacon.getStatus().equals(Beacon.Status.UNSPECIFIED)) {
            json.put("status", beacon.getStatus());
        }
        if (beacon.getPlaceId() != null) {
            json.put("placeId", beacon.getPlaceId());
        }
        if (beacon.getLatitude() != null && beacon.getLongitude() != null) {
            JSONObject latLng = new JSONObject();
            latLng.put("latitude", beacon.getLatitude());
            latLng.put("longitude", beacon.getLongitude());
            json.put("latLng", latLng);
        }
        if (beacon.getExpectedStability() != null && !beacon.getExpectedStability().equals(Beacon.Status.STABILITY_UNSPECIFIED)) {
            json.put("expectedStability", beacon.getExpectedStability());
        }
        if (beacon.getDescription() != null) {
            json.put("description", beacon.getDescription());
        }
        return json;
    }

}
