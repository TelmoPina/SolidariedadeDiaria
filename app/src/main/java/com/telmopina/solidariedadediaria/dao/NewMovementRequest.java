package com.telmopina.solidariedadediaria.dao;

import com.google.gson.annotations.SerializedName;

public class NewMovementRequest {

    @SerializedName("money")
    private Float money;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("institute")
    private String institute;

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getMoney() {
        return money;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getInstitute() {
        return institute;
    }

    @Override
    public String toString() {
        return
                "MovementRequest{" +
                        "money = '" + money + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",institute = '" + institute + '\'' +
                        "}";
    }
}