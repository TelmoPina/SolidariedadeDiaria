package com.telmopina.solidariedadediaria;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.telmopina.solidariedadediaria.R;
import com.telmopina.solidariedadediaria.base.BaseActivity;
import com.telmopina.solidariedadediaria.dao.NewMovementRequest;
import com.telmopina.solidariedadediaria.utils.AppGlobals;
import com.telmopina.solidariedadediaria.webservice.ApiCallbacks;
import com.telmopina.solidariedadediaria.webservice.ApiClient;
import com.telmopina.solidariedadediaria.webservice.ApiInterface;
import com.telmopina.solidariedadediaria.webservice.WebApiConstants;
import com.telmopina.solidariedadediaria.webservice.WebServiceCaller;
import com.google.gson.JsonObject;

import retrofit2.Call;

public class DialogActivity extends BaseActivity implements View.OnClickListener, ApiCallbacks {

    private LinearLayout instituteOne;
    private LinearLayout instituteTwo;
    private LinearLayout instituteThree;
    private LinearLayout instituteFour;

    private EditText amountEditText;
    private String mInstituteString = "";
    private String mAmountString;
    private float availableBalance = AppGlobals.getMoneyFromSharedPreferences(AppGlobals.KEY_AMOUNT);
    private String email = AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_EMAIL);
    private float newBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        instituteOne = findViewById(R.id.institute_one);
        instituteTwo = findViewById(R.id.institute_two);
        instituteThree = findViewById(R.id.institute_three);
        instituteFour = findViewById(R.id.institute_four);
        amountEditText = findViewById(R.id.et_amount);

        instituteOne.setOnClickListener(this);
        instituteTwo.setOnClickListener(this);
        instituteThree.setOnClickListener(this);
        instituteFour.setOnClickListener(this);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().isEmpty()) {
                        if (availableBalance > Float.parseFloat(s.toString())) {
                            amountEditText.setTextColor(Color.BLACK);
                            System.out.println("pagar");
                            instituteOne.setClickable(true);
                            instituteTwo.setClickable(true);
                            instituteThree.setClickable(true);
                            instituteFour.setClickable(true);

                        } else {
                            amountEditText.setTextColor(Color.RED);
                            amountEditText.setError("Não tem dinheiro suficiente");
                            instituteOne.setClickable(false);
                            instituteTwo.setClickable(false);
                            instituteThree.setClickable(false);
                            instituteFour.setClickable(false);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Alguma coisa correu mal!");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean validate() {
        boolean valid = true;

        mAmountString = amountEditText.getText().toString();


        if (mAmountString.trim().isEmpty() || mAmountString.equals("0")) {
            amountEditText.setError("Por favor escreva um valor");
            valid = false;
        } else {
            amountEditText.setError(null);
        }
        return valid;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.institute_one:
                mInstituteString = getString(R.string.institution1);
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                }
                break;

            case R.id.institute_two:
                mInstituteString = getString(R.string.institution2);
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                }
                break;

            case R.id.institute_three:
                mInstituteString = getString(R.string.institution3);
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                }
                break;

            case R.id.institute_four:
                mInstituteString = getString(R.string.institution4);
                if (validate()) {
                    sendMoney(mInstituteString, Float.parseFloat(mAmountString));
                }
                break;
        }
    }

    /**
     * method call to send money to institutes
     *
     * @param name  name of the institute
     * @param money amount that is to be transferred
     */
    private void sendMoney(final String name, final float money) {
        mActivity.showProgress();
        NewMovementRequest movement = new NewMovementRequest();
        movement.setInstitute(name);
        movement.setMoney(money);
        movement.setUserId(Integer.parseInt(AppGlobals.getStringFromSharedPreferences(AppGlobals.KEY_ID)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<JsonObject> call = apiService.insertMovement(movement);
        WebServiceCaller.CallWebApi(call, WebApiConstants.NEW_MOVEMENT, mActivity, this);
    }

    @Override
    public void onSuccess(JsonObject jsonObject, Enum anEnum) {
        if (anEnum == WebApiConstants.NEW_MOVEMENT) {

            // api call to update user table for amount
            newBalance = availableBalance - Float.parseFloat(mAmountString);
            AppGlobals.saveMoneyToSharedPreferences(AppGlobals.KEY_AMOUNT, newBalance);

            JsonObject object = new JsonObject();
            object.addProperty("amount", newBalance);
            object.addProperty("email", email);

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<JsonObject> call = apiService.updateAmount(object);
            WebServiceCaller.CallWebApi(call, WebApiConstants.UPDATE_AMOUNT, mActivity, this);
        } else if (anEnum == WebApiConstants.UPDATE_AMOUNT) {
            mActivity.hideProgress();
            Toast.makeText(getApplicationContext(), "Pagamento Feito!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onError(JsonObject jsonObject, Enum anEnum) {
        mActivity.hideProgress();
        mActivity.showToast(WebServiceCaller.getResponseMessage(jsonObject));
    }
}
