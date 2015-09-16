package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.StringUtils;
import com.malmstein.eddystonesample.model.Beacon;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeacon;
import com.novoda.notils.caster.Views;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeaconAttachmentsView extends CardView implements AttachmentRow.Listener {

    private static final String TAG = "BeaconAttachmentsView";

    private TextView beaconAddAttachment;
    private TextView beaconNamespace;
    private LinearLayout beaconAttachments;

    private Beacon beacon;
    private Listener listener;
    private String namespace;
    private ProximityBeacon proximityBeacon;

    public BeaconAttachmentsView(Context context) {
        super(context);
    }

    public BeaconAttachmentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeaconAttachmentsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_attachments, this, true);

        beaconAddAttachment = Views.findById(this, R.id.beacon_attachments_add);
        beaconNamespace = Views.findById(this, R.id.beacon_attachments_namespace);
        beaconAttachments = Views.findById(this, R.id.beacon_attachments_root);
    }

    public void updateWith(Beacon beacon, String namespace, Listener listener, ProximityBeacon proximityBeacon) {
        this.beacon = beacon;
        this.namespace = namespace;
        this.listener = listener;
        this.proximityBeacon = proximityBeacon;

        updateVisibility(beacon);
        bindNamespace(namespace);
        bindClickListener();
        listAttachments();
    }

    private void bindNamespace(String namespace) {
        beaconNamespace.setText(getResources().getString(R.string.beacon_namespace, namespace));
    }

    private void bindClickListener() {
        beaconAddAttachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShowAttachmentDialog();
            }
        });
    }

    private void updateVisibility(Beacon beacon) {
        if (beacon.getStatus().equals(Beacon.Status.UNREGISTERED)) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    private void listAttachments() {
        Callback listAttachmentsCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "Failed request: " + request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                if (response.isSuccessful()) {
                    try {

                        JSONObject json = new JSONObject(body);
                        if (body.length() == 0) {  // No attachment data
                            return;
                        }
                        JSONArray attachments = json.getJSONArray("attachments");
                        for (int i = 0; i < attachments.length(); i++) {
                            JSONObject attachment = attachments.getJSONObject(i);
                            addAttachmentView(attachment);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException in fetching attachments", e);
                    }
                } else {
                    Log.d(TAG, "Unsuccessful listAttachments request: " + body);
                }
            }
        };
        proximityBeacon.listAttachments(listAttachmentsCallback, beacon.getBeaconName());
    }

    private void addAttachmentView(JSONObject json) throws JSONException {
        AttachmentRow row = (AttachmentRow) LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_attachments_item, beaconAttachments, false);
        row.updateWith(json, BeaconAttachmentsView.this);
        beaconAttachments.addView(row);
    }

    public void addAttachment(final ProximityBeacon proximityBeacon, String type, String data) {
        this.proximityBeacon = proximityBeacon;

        JSONObject body = buildCreateAttachmentJsonBody(namespace, type, data);

        Callback createAttachmentCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "Failed request: " + request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(body);
                        addAttachmentView(json);
                    } catch (JSONException e) {
                        Log.d(TAG, "JSONException in building attachment data", e);
                    }
                } else {
                    Log.d(TAG, "Unsuccessful createAttachment request: " + body);
                }
            }
        };

        proximityBeacon.createAttachment(createAttachmentCallback, beacon.getBeaconName(), body);
    }

    private JSONObject buildCreateAttachmentJsonBody(String namespace, String type, String data) {
        try {
            return new JSONObject()
                    .put("namespacedType", namespace + "/" + type)
                    .put("data", StringUtils.base64Encode(data.getBytes()));
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }
        return null;
    }

    @Override
    public void onRemoveAttachment(String attachmentName, final int id) {
        Callback deleteAttachmentCallback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "Failed request: " + request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    Snackbar.make(BeaconAttachmentsView.this, R.string.manage_beacon_update_complete, Snackbar.LENGTH_LONG).show();
                    beaconAttachments.removeView(findViewById(id));
                } else {
                    String body = response.body().string();
                    Snackbar.make(BeaconAttachmentsView.this, R.string.manage_beacon_update_failure, Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "Unsuccessful deleteAttachment request: " + body);
                }
            }
        };
        Snackbar.make(this, R.string.removing_attachment, Snackbar.LENGTH_LONG).show();
        proximityBeacon.deleteAttachment(deleteAttachmentCallback, attachmentName);
    }

    public interface Listener {
        void onShowAttachmentDialog();
    }
}
