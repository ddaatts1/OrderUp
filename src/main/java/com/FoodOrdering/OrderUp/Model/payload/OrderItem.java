package com.FoodOrdering.OrderUp.Model.payload;

import lombok.Data;

@Data
public class OrderItem {

    String _id;
    String restaurantId;
    int quantity;

}
