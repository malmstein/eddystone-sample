package com.malmstein.eddystonesample.manage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malmstein.eddystonesample.R;
import com.malmstein.eddystonesample.StringUtils;
import com.novoda.notils.caster.Views;

import org.json.JSONException;
import org.json.JSONObject;

public class AttachmentRow extends LinearLayout {

    private static final String TAG = "AttachmentRow";

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

        attachmentData = Views.findById(this, R.id.beacon_attachment_item_data);
        attachmentType = Views.findById(this, R.id.beacon_attachment_item_type);
        attachmentRemove = Views.findById(this, R.id.beacon_attachment_item_remove);
    }

    public void updateWith(final JSONObject attachment, final Listener listener) {

        final int id = View.generateViewId();
        setId(id);

        try {
            String[] namespacedType;
            namespacedType = attachment.getString("namespacedType").split("/");
            String dataStr = attachment.getString("data");
            final String attachmentName = attachment.getString("attachmentName");
            attachmentType.setText(namespacedType[1]);
            String base64Decoded = new String(StringUtils.base64Decode(dataStr));
            attachmentData.setText(base64Decoded);
            attachmentRemove.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveAttachment(attachmentName, id);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface Listener {
        void onRemoveAttachment(String attachmentName, int id);
    }

}
