package com.malmstein.eddystonesample.manage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Classes;

public class BeaconStabilityDialogFragment extends DialogFragment {

    private Listener mListener;
    public CharSequence[] mItems;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = Classes.from(activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BeaconStabilityDialogFragment.Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mItems = getContext().getResources().getTextArray(R.array.beacon_stability_enums);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.beacon_dialog_title)
                .setItems(mItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       mListener.onBeaconStabilityChange(mItems[which].toString());
                    }
                });

        return builder.create();
    }

    public interface Listener {
        void onBeaconStabilityChange(String selectedStability);
    }
} 
