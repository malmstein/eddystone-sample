package com.malmstein.eddystonesample.account;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;

public class AccountFragment extends Fragment {

    private View chooseAccount;
    private Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        chooseAccount = Views.findById(rootView, R.id.choose_account);
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
        chooseAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountSelectorClicked();
            }
        });
    }

    public interface Listener {
        void onAccountSelectorClicked();
    }
}
