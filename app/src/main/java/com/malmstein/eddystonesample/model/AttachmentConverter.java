package com.malmstein.eddystonesample.model;

import android.util.Log;

import com.malmstein.eddystonesample.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AttachmentConverter {

    private static final String TAG = "AttachmentConverter";

    public JSONObject buildCreateAttachmentJsonBody(String namespace, String type, String data) {
        try {
            return new JSONObject().put("namespacedType", namespace + "/" + type)
                    .put("data", StringUtils.base64Encode(data.getBytes()));
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

}
