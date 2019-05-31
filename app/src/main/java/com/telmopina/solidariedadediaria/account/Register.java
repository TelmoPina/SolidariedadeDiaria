package com.telmopina.solidariedadediaria.account;

import android.content.Intent;
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

import com.byteshaft.solidariedadediria.MainActivity;
import com.byteshaft.solidariedadediria.R;
import com.byteshaft.solidariedadediria.base.BaseFragment;
import com.byteshaft.solidariedadediria.dao.UserRequest;
import com.byteshaft.solidariedadediria.utils.AppGlobals;
import com.byteshaft.solidariedadediria.webservice.ApiCallbacks;
import com.byteshaft.solidariedadediria.webservice.ApiClient;
import com.byteshaft.solidariedadediria.webservice.ApiInterface;
import com.byteshaft.solidariedadediria.webservice.WebApiConstants;
import com.byteshaft.solidariedadediria.webservice.WebServiceCaller;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class Register extends BaseFragment implements View.OnClickListener, ApiCallbacks {

    private View mBaseView;
    private Button mRegisterButton;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;

    private String mEmailString;
    private String mPasswordString;
    private String mNameString;
    private float initialAmmount = 10.00f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mBaseView = inflater.inflate(R.layout.fragment_register, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRegisterButton = mBaseView.findViewById(R.id.button_register);
        mName = mBaseView.findViewById(R.id.name_edit_text);
        mEmail = mBaseView.findViewById(R.id.email_edit_text);
        mPassword = mBaseView.findViewById(R.id.password_edit_text);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_register) {
            if (validate()) {
                //api call to register user if data is valid
                mActivity.showProgress();
                UserRequest user = new UserRequest();
                user.setUsername(mNameString);
                user.setEmail(mEmailString);
                user.setPassword(mPasswordString);
                user.setAmount(initialAmmount);
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> call = apiService.register(user);
                WebServiceCaller.CallWebApi(call, WebApiConstants.SIGN_UP, mActivity, this);
            }
        }
    }


    private boolean validate() {
        boolean valid = true;
        mEmailString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();
        mNameString = mName.getText().toString();

        if (mEmailString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmail.setError("please provide a valid email");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mPasswordString.isEmpty() || mPassword.length() < 4) {
            mPassword.setError("Enter minimum 4 alphanumeric characters");
            valid = false;
        } else {
            mPassword.setError(null);
        }

        if (mNameString.isEmpty()) {
            mName.setError("Required");
            valid = false;
        } else {
            mName.setError(null);
        }
        return valid;
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        if (anEnum == WebApiConstants.SIGN_UP) {
            //api response for sign_up, and saving data into shared preferences
            Toast.makeText(getContext(), "Registered", Toast.LENGTH_LONG).show();
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_ID, String.valueOf(WebServiceCaller.getResponsePacket(jsonObject).get("id")));
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, mNameString);
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, mEmailString);
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, mPasswordString);
            AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, initialAmmount);
            startActivity(new Intent(getActivity(), MainActivity.class));
            AppGlobals.loginState(true);
            AccountManager.getInstance().finish();
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
