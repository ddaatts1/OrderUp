package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.Data;

@Data
public class FoodOrderDTO {

    String id;
    String name;
    int price;
    int quantity;
    boolean isRated;

}
