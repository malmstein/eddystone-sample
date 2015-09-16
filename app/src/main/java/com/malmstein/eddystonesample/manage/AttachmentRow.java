package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.StringUtils;
import com.malmstein.eddystonesample.proximitybeacon.ProximityBeacon;
import com.novoda.notils.caster.Views;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class AttachmentRow extends LinearLayout {

    private TextView attachmentData;
    private TextView attachmentType;
    private View attachmentRemove;

    public AttachmentRow(Context context) {
        super(context);
    }

    public AttachmentRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AttachmentRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_beacon_attachments_item, this, true);

        attachmentData = Views.findById(this, R.id.beacon_attachment_item_data);
        attachmentType = Views.findById(this, R.id.beacon_attachment_item_type);
        attachmentRemove = Views.findById(this, R.id.beacon_attachment_item_remove);
    }

    public void updateWith(final JSONObject attachment, final ProximityBeacon proximityBeacon) {
        LinearLayout row = new LinearLayout(getContext());
        int id = View.generateViewId();
        row.setId(id);

        try {
            String[] namespacedType;
            namespacedType = attachment.getString("namespacedType").split("/");
            attachmentType.setText(namespacedType[1]);
            String dataStr = attachment.getString("data");
            String base64Decoded = new String(StringUtils.base64Decode(dataStr));
            attachmentData.setText(base64Decoded);
            attachmentRemove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Callback deleteAttachmentCallback = new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            logErrorAndToast("Failed request: " + request, e);
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (response.isSuccessful()) {
                                attachmentsTable.removeView(attachmentsTable.findViewById(viewId));
                            } else {
                                String body = response.body().string();
                                logErrorAndToast("Unsuccessful deleteAttachment request: " + body);
                            }
                        }
                    };
                    proximityBeacon.deleteAttachment(deleteAttachmentCallback, attachment.getString("attachmentName"));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
