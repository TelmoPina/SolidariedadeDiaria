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
import android.widget.LinearLayout;

import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.institutions.InstituteFour;
import com.telmopina.solidariedadediaria.institutions.InstituteOne;
import com.telmopina.solidariedadediaria.institutions.InstituteThree;
import com.telmopina.solidariedadediaria.institutions.InstituteTwo;

public class Institution extends Fragment implements View.OnClickListener {


    private LinearLayout instituteOne;
    private LinearLayout instituteTwo;
    private LinearLayout instituteThree;
    private LinearLayout instituteFour;

    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.institution);
        mBaseView = inflater.inflate(R.layout.fragment_institution, container, false);

        instituteOne = mBaseView.findViewById(R.id.institute_one);
        instituteTwo = mBaseView.findViewById(R.id.institute_two);
        instituteThree = mBaseView.findViewById(R.id.institute_three);
        instituteFour = mBaseView.findViewById(R.id.institute_four);

        instituteOne.setOnClickListener(this);
        instituteTwo.setOnClickListener(this);
        instituteThree.setOnClickListener(this);
        instituteFour.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.institute_one:
                startActivity(new Intent(getActivity(), InstituteOne.class));
                break;

            case R.id.institute_two:
                startActivity(new Intent(getActivity(), InstituteTwo.class));
                break;

            case R.id.institute_three:
                startActivity(new Intent(getActivity(), InstituteThree.class));
                break;

            case R.id.institute_four:
                startActivity(new Intent(getActivity(), InstituteFour.class));
                break;
        }
    }
}
