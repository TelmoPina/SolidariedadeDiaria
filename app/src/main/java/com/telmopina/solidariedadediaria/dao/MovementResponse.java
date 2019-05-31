package com.telmopina.solidariedadediaria.dao;

import com.google.gson.annotations.SerializedName;

public class MovementResponse {

    @SerializedName("money")
    private Float money;

    @SerializedName("id")
    private int id;

    @SerializedName("institute")
    private String institute;

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "NewMovementResponse{" +
                        "money = '" + money + '\'' +
                        ",id = '" + id + '\'' +
                        ",institute = '" + institute + '\'' +
                        "}";
    }
}