package com.telmopina.solidariedadediaria.base;


import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    public BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }
}
