package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDTO {


    private String _id;
    private String name ;
    private String phone ;
    private String email;
    private String address;
    private boolean status;
    private String uid;

    public RestaurantDTO(String id,String name, String phone, String email, String address,boolean status){
        _id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
    }

}
