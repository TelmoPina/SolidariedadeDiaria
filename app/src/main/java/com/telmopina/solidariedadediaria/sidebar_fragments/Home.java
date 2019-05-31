package com.telmopina.solidariedadediaria.sidebar_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.telmopina.solidariedadediaria.DialogActivity;
import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.utils.AppGlobals;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Home extends Fragment {

    private View mBaseView;
    private Button mSendButton;
    private TextView mMoneyText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_home));
        mBaseView = inflater.inflate(R.layout.fragment_home, container, false);
        mSendButton = mBaseView.findViewById(R.id.button_send);
        mMoneyText = mBaseView.findViewById(R.id.tv_money);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DialogActivity.class));
            }
        });

        return mBaseView;
    }


    @Override
    public void onResume() {
        super.onResume();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        float money = AppGlobals.getMoneyFromSharedPreferences(AppGlobals.KEY_AMOUNT);
        mMoneyText.setText(df.format(money));
    }
}
