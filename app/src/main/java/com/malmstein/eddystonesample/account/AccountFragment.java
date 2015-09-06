package com.malmstein.eddystonesample.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

public class AccountFragment extends Fragment {

    private SignInButton signIn;
    private Button signOut;
    private Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        signIn = Views.findById(rootView, R.id.sign_in_button);
        signOut = Views.findById(rootView, R.id.sign_out_button);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = Classes.from(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignInClicked();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignOutClicked();
            }
        });
    }

    public interface Listener {
        void onSignInClicked();
        void onSignOutClicked();
    }
}
