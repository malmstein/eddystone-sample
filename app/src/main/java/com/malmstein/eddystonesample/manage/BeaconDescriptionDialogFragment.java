package com.malmstein.eddystonesample.manage;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

public class BeaconDescriptionDialogFragment extends DialogFragment {

    private Listener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = Classes.from(activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BeaconDescriptionFragment.Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_description, null);
        final EditText descriptionView = Views.findById(view, R.id.beacon_dialog_description);

        builder.setView(view)
                .setPositiveButton(R.string.dialog_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String description = descriptionView.getText().toString();
                        mListener.onBeaconDescriptionChanged(description);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface Listener {
        void onBeaconDescriptionChanged(String description);
    }

}
