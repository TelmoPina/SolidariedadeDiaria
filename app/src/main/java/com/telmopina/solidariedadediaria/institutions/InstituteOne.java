package com.telmopina.solidariedadediaria.institutions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.telmopina.solidariedadediaria.R;

public class InstituteOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_one);
        setTitle(getString(R.string.institution1));
    }
}
