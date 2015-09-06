package com.malmstein.eddystonesample.account;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.malmstein.eddystonesample.R;
import com.novoda.notils.caster.Views;

public class AccountView extends FrameLayout {

    public AccountView(Context context) {
        super(context);
    }

    public AccountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.view_accounts, this, true);
    }

    public void setup(final Listener listener) {
        View chooseAccount = Views.findById(this, R.id.choose_account);
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
