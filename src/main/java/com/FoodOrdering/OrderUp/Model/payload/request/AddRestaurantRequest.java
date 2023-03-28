package com.FoodOrdering.OrderUp.Model.payload.request;


import lombok.Data;

@Data
public class AddRestaurantRequest {

    private String name;
    private String detail;
    private String address_detail;
    private String address_id;
    private String email;
    private String phone;
    private  String password;
}
