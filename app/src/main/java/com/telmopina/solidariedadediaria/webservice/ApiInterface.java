package com.telmopina.solidariedadediaria.webservice;

import com.byteshaft.solidariedadediria.dao.NewMovementRequest;
import com.byteshaft.solidariedadediria.dao.UserRequest;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Pranay Sancheti.
 */
public interface ApiInterface {
    /**
     * api call to register user
     * @param registerRequestModel
     * @return
     */
    @POST("solidariedadediria/SignUp.php")
    Call<JsonObject> register(@Body UserRequest registerRequestModel);

    /**
     * api call to register a movement/transaction
     * @param movementRequestModel
     * @return
     */
    @POST("solidariedadediria/NewMovement.php")
    Call<JsonObject> insertMovement(@Body NewMovementRequest movementRequestModel);

    /**
     * api call to retrieve all the movements of a particulas user
     * @param jsonObject
     * @return
     */
    @POST("solidariedadediria/GetAllMovements.php")
    Call<JsonObject> getAllMovements(@Body JsonObject jsonObject);

    /**
     * api call to retrieve data of a user with email and password
     * @param jsonObject
     * @return
     */
    @POST("solidariedadediria/GetUser.php")
    Call<JsonObject> getUser(@Body JsonObject jsonObject);

    /**
     * api call to update amount
     * @param jsonObject
     * @return
     */
    @POST("solidariedadediria/UpdateAmount.php")
    Call<JsonObject> updateAmount(@Body JsonObject jsonObject);

    /**
     * api call to update info of user
     * @param jsonObject
     * @return
     */
    @POST("solidariedadediria/UpdateUser.php")
    Call<JsonObject> updateUser(@Body JsonObject jsonObject);


}
