package com.example.niluxer.prepvolleycustomers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by niluxer on 3/31/17.
 */

public class Customer implements Serializable{
    private int id;
    private String active;
    private String email;
    private String first_name;
    private String last_name;
    private String last_update;
    private Double latitude;
    private Double longitude;

    public Customer(String active, String email, String first_name, String last_name, String last_update, Double latitude, Double longitude) {
        this.active = active;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.last_update = last_update;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Customer() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("customer_id", getId());
            jsonObject.put("active", getActive());
            jsonObject.put("create_date", getLast_update());
            jsonObject.put("email", getEmail());
            jsonObject.put("first_name", getFirst_name());
            jsonObject.put("last_name", getLast_name());
            jsonObject.put("last_update", getLast_update());
            jsonObject.put("latitude", getLatitude());
            jsonObject.put("longitude", getLongitude());
            jsonObject.put("rfc", "AACJ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

}
