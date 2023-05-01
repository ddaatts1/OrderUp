package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.Data;

@Data
public class RestaurantDTO {


    private String _id;
    private String name ;
    private String phone ;
    private String email;
    private String address;
    private String uid;

}
