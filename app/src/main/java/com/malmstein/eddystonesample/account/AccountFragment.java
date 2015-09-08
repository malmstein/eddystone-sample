package com.malmstein.eddystonesample.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

public class AccountFragment extends Fragment {

    private SignInButton signIn;
    private Button signOut;
    private TextView signedInText;
    private TextView signedOutText;
    private Listener listener;

    private AccountSharedPreferences accountSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        signIn = Views.findById(rootView, R.id.sign_in_button);
        signOut = Views.findById(rootView, R.id.sign_out_button);
        signedOutText = Views.findById(rootView, R.id.choose_account);
        signedInText = Views.findById(rootView, R.id.using_account);
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

        accountSharedPreferences = AccountSharedPreferences.newInstance(getActivity());

        if (accountSharedPreferences.isLoggedIn()) {
            showLoggedInUI();
        } else {
            showLoggedOutUI();
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSignInClicked();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutUser();

                listener.onSignOutClicked();
            }
        });
    }

    private void signOutUser() {
        accountSharedPreferences.logout();
        showLoggedOutUI();
    }

    public void signInUser(String accountName) {
        new AccountAuthorizationTask(getActivity(), accountName).execute();
        accountSharedPreferences.saveAccount(accountName);
        showLoggedInUI();
    }

    private void showLoggedInUI() {
        signOut.setVisibility(View.VISIBLE);
        signedOutText.setVisibility(View.GONE);
        signIn.setVisibility(View.GONE);
        signedInText.setText(getString(R.string.use_account, accountSharedPreferences.getAccount()));
        signedInText.setVisibility(View.VISIBLE);
    }

    private void showLoggedOutUI() {
        signOut.setVisibility(View.GONE);
        signedOutText.setVisibility(View.VISIBLE);
        signIn.setVisibility(View.VISIBLE);
        signedInText.setVisibility(View.GONE);
    }

    public interface Listener {
        void onSignInClicked();

        void onSignOutClicked();
    }
}
