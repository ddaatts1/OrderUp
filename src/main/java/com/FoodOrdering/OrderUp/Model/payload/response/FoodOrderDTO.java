package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.Data;

@Data
public class FoodOrderDTO {

    String name;
    int price;
    int quantity;

}
