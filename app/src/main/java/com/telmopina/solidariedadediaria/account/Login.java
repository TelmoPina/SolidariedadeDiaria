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
import android.widget.TextView;

import com.telmopina.solidariedadediaria.MainActivity;
import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.base.BaseFragment;
import com.telmopina.solidariedadediaria.dao.UserResponse;
import com.telmopina.solidariedadediaria.utils.AppGlobals;
import com.telmopina.solidariedadediaria.webservice.ApiCallbacks;
import com.telmopina.solidariedadediaria.webservice.ApiClient;
import com.telmopina.solidariedadediaria.webservice.ApiInterface;
import com.telmopina.solidariedadediaria.webservice.WebApiConstants;
import com.telmopina.solidariedadediaria.webservice.WebServiceCaller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import retrofit2.Call;

public class Login extends BaseFragment implements View.OnClickListener, ApiCallbacks {

    private View mBaseView;

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mSignUpTextView;
    private String mPasswordString;
    private String mEmailString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        mBaseView = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = mBaseView.findViewById(R.id.email_edit_text);
        mPassword = mBaseView.findViewById(R.id.password_edit_text);
        mLoginButton = mBaseView.findViewById(R.id.button_login);
        mSignUpTextView = mBaseView.findViewById(R.id.sign_up_text_view);

        mLoginButton.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);

        return mBaseView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                if (validate()) {
                    // api call to login user
                    mActivity.showProgress();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("email", mEmailString);
                    jsonObject.addProperty("password", mPasswordString);
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<JsonObject> call = apiService.getUser(jsonObject);
                    WebServiceCaller.CallWebApi(call, WebApiConstants.LOGIN, mActivity, this);
                }
                break;
            case R.id.sign_up_text_view:
                AccountManager.getInstance().loadFragment(new Register());
                break;
        }
    }

    private boolean validate() {
        boolean valid = true;

        mEmailString = mEmail.getText().toString();
        mPasswordString = mPassword.getText().toString();

        if (mEmailString.trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailString).matches()) {
            mEmail.setError("Insira um email válido");
            valid = false;
        } else {
            mEmail.setError(null);
        }
        if (mPasswordString.isEmpty() || mPassword.length() < 4) {
            mPassword.setError("Insira um mínimo de 4 caracteres");
            valid = false;
        } else {
            mPassword.setError(null);
        }
        return valid;
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        if (anEnum == WebApiConstants.LOGIN) {
            // login success, convert data to model and save it to shared preferences
            Type type = new TypeToken<UserResponse>() {
            }.getType();
            UserResponse response = new Gson().fromJson(WebServiceCaller.getResponsePacket(jsonObject), type);
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_NAME, response.getUsername());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_EMAIL, response.getEmail());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_PASSWORD, response.getPassword());
            AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, response.getAmount());
            AppGlobals.saveStringToSharedPreferences(AppGlobals.KEY_ID, String.valueOf(response.getId()));
            startActivity(new Intent(getActivity(), MainActivity.class));
            AccountManager.getInstance().finish();
            AppGlobals.loginState(true);
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
