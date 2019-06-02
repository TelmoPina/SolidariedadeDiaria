package com.telmopina.solidariedadediaria.webservice;

import android.widget.Toast;

import com.telmopina.solidariedadediaria.base.BaseActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceCaller {

    /**
     * method call to call api
     *
     * @param call         request call
     * @param anEnum       request enum
     * @param mActivity    base activity context
     * @param apiCallbacks interface callback for api response
     */
    public static void CallWebApi(Call<JsonObject> call, final Enum anEnum, final BaseActivity mActivity, final ApiCallbacks apiCallbacks) {

        if (call == null) {
            Toast.makeText(mActivity, "Sem conexão de Internet", Toast.LENGTH_SHORT).show();
            mActivity.hideProgress();
            return;
        }
        call.enqueue(new Callback<JsonObject>() {
                         @Override
                         public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                             if (response.body() != null && isSuccess(response.body())) {
                                 apiCallbacks.onSuccess(response.body(), anEnum);
                             } else {
                                 apiCallbacks.onError(response.body(), anEnum);
                             }
                         }

                         @Override
                         public void onFailure(Call<JsonObject> call, Throwable t) {
                             mActivity.showToast(t.getMessage());
                             mActivity.hideProgress();
                         }
                     }

        );
    }

    /**
     * method to check response success
     *
     * @param jsonObject response json object
     * @return true if success
     */
    public static boolean isSuccess(JsonObject jsonObject) {
        if (jsonObject.get("status").getAsInt() == 200) {
            return true;
        } else return false;
    }

    /**
     * method call to get response message from response json
     *
     * @param jsonObject response json
     * @return message as string
     */
    public static String getResponseMessage(JsonObject jsonObject) {
        return jsonObject.get("ResponseMessage").getAsString();
    }

    /**
     * method call to get response packet as json object
     *
     * @param jsonObject json response
     * @return response packet as json object
     */
    public static JsonObject getResponsePacket(JsonObject jsonObject) {
        return jsonObject.getAsJsonObject("ResponsePacket");
    }

    /**
     * method call to get response packet as json array
     *
     * @param jsonObject json response
     * @return response packet as json array
     */
    public static JsonArray getResponsePacketArray(JsonObject jsonObject) {
        return jsonObject.getAsJsonArray("ResponsePacket");
    }
}
