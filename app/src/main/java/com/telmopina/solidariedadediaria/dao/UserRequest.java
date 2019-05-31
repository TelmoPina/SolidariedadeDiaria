package com.telmopina.solidariedadediaria.dao;

import com.google.gson.annotations.SerializedName;

public class UserRequest {

    @SerializedName("password")
    private String password;

    @SerializedName("amount")
    private Float amount;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return
                "UserRequest{" +
                        "password = '" + password + '\'' +
                        ",amount = '" + amount + '\'' +
                        ",email = '" + email + '\'' +
                        ",username = '" + username + '\'' +
                        "}";
    }
}