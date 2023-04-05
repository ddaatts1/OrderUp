package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private String _id;
    private String name;
    private String detail;
    private int price;
    private String status;
    private int ordered;
    private  String restaurant_id;

}
