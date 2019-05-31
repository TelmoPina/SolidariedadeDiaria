package com.telmopina.solidariedadediaria.webservice;

import com.google.gson.JsonObject;

/**
 * Created by Pranay Sancheti.
 */

public interface ApiCallbacks {
    /**
     * callback when api result is success
     * @param jsonObject
     * @param anEnum
     */
    void onSuccess(JsonObject jsonObject, Enum anEnum);

    /**
     * callback, when api result is failure
     * @param jsonObject
     * @param anEnum
     */
    void onError(JsonObject jsonObject, Enum anEnum);
}
