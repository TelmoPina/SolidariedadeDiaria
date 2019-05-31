package com.telmopina.solidariedadediaria.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.telmopina.solidariedadediaria.R;

public class BaseActivity extends AppCompatActivity {

    private View progressBar;
    public BaseActivity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        init();
    }

    /**
     * method call to initialize progress bar dialog
     */
    public void init() {
        progressBar = (View) getLayoutInflater().inflate(
                R.layout.layout_progress, null);
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.addView(progressBar);

    }

    /**
     * method call to show progress
     */
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * method call to hide progress
     */
    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method to show toast
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * set toolbar in activity
     *
     * @param toolbar        view of toolbar
     * @param action_Bar     view
     * @param back           should show back button
     * @param backBtn        imageview for back button
     * @param textViewAction textview inside action bar
     * @param title          title for the view
     */
    public void setToolbar(Toolbar toolbar, View action_Bar, Boolean back, ImageView backBtn, TextView textViewAction, String title) {
        setSupportActionBar(toolbar);
        if (back) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        action_Bar.bringToFront();
        if (textViewAction != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            textViewAction.setText(title);
        }
    }

}
