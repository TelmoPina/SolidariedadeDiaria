package com.telmopina.solidariedadediaria.sidebar_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.base.BaseFragment;
import com.telmopina.solidariedadediaria.utils.AppGlobals;
import com.telmopina.solidariedadediaria.webservice.ApiCallbacks;
import com.telmopina.solidariedadediaria.webservice.ApiClient;
import com.telmopina.solidariedadediaria.webservice.ApiInterface;
import com.telmopina.solidariedadediaria.webservice.WebApiConstants;
import com.telmopina.solidariedadediaria.webservice.WebServiceCaller;


import retrofit2.Call;

public class UserInfo extends BaseFragment implements ApiCallbacks {

    private View mBaseView;
    private Button mChangeButton;
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private EditText mEmailEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.user_info));
        mBaseView = inflater.inflate(R.layout.fragment_user_info, container, false);
        mNameEditText = mBaseView.findViewById(R.id.et_name);
        mPasswordEditText = mBaseView.findViewById(R.id.et_password);
        mEmailEditText = mBaseView.findViewById(R.id.et_email);
        mEmailEditText.setEnabled(false);

        mChangeButton = mBaseView.findViewById(R.id.button_change);
        mNameEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_NAME));
        mPasswordEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_PASSWORD));
        mEmailEditText.setText(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL));

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    update(
                            mNameEditText.getText().toString(),
                            mPasswordEditText.getText().toString(),
                            mEmailEditText.getText().toString());

                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, mNameEditText.getText().toString());
                    AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, mPasswordEditText.getText().toString());
                }
            }
        });
        return mBaseView;
    }

    private boolean validate() {
        boolean valid = true;

        if (mEmailEditText.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches()) {
            mEmailEditText.setError("Insira um mail válido");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }
        if (mPasswordEditText.getText().toString().isEmpty() || mPasswordEditText.getText().length() < 8 || !mActivity.validatePassword(mPasswordEditText.getText().toString())) {
            mPasswordEditText.setError(getString(R.string.password_validation));
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }
        if (mNameEditText.getText().toString().isEmpty()) {
            mNameEditText.setError("Necessário preencher");
            valid = false;
        } else {
            mNameEditText.setError(null);
        }
        return valid;
    }

    private void update(final String name, final String password, final String email) {
        //api call to update user info
        mActivity.showProgress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", mNameEditText.getText().toString());
        jsonObject.addProperty("password", mPasswordEditText.getText().toString());
        jsonObject.addProperty("email", mEmailEditText.getText().toString());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.updateUser(jsonObject);
        WebServiceCaller.CallWebApi(call, WebApiConstants.UPDATE_USER, mActivity, this);

    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        if (anEnum == WebApiConstants.UPDATE_USER)
            Toast.makeText(getContext(), "Atualizado", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
