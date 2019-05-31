package com.telmopina.solidariedadediaria;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.telmopina.solidariedadediaria.base.BaseActivity;
import com.telmopina.solidariedadediaria.utils.AppGlobals;
import com.telmopina.solidariedadediaria.webservice.ApiCallbacks;
import com.telmopina.solidariedadediaria.webservice.ApiClient;
import com.telmopina.solidariedadediaria.webservice.ApiInterface;
import com.telmopina.solidariedadediaria.webservice.WebApiConstants;
import com.telmopina.solidariedadediaria.webservice.WebServiceCaller;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class AdActivity extends BaseActivity implements View.OnClickListener, ApiCallbacks {

    private Button dismissButton;
    private ImageView adImage;

    private static AdActivity sInstance;
    private float availableBalance = AppGlobals.getMoneyFromSharedPreferences(AppGlobals.KEY_AMOUNT);
    private String email = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
    private float rewardedMoney = 0.10f;
    private float newBalance;

    public static AdActivity getInstance() {
        return sInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sInstance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_ad);
        adImage = findViewById(R.id.ad_image);
        dismissButton = findViewById(R.id.button_dismiss);
        adImage.setOnClickListener(this);
        dismissButton.setOnClickListener(this);

        /// adding and updating balance
        newBalance = availableBalance + rewardedMoney;


        //api call to update amount
        mActivity.showProgress();
        JsonObject object = new JsonObject();
        object.addProperty("amount", newBalance);
        object.addProperty("email", email);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.updateAmount(object);
        WebServiceCaller.CallWebApi(call, WebApiConstants.UPDATE_AMOUNT, mActivity, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dismiss:
                finish();
                break;
            case R.id.ad_image:
                openLink();
                finish();
        }
    }

    private void openLink() {
        Uri uri = Uri.parse("http://www.google.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        if (anEnum == WebApiConstants.UPDATE_AMOUNT) {
            mActivity.hideProgress();
            AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
