package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;

@Data
public class GetItemsRequest {

    private String restaurant_id;
    private String category_id;
    private Double address_long;
    private Double address_lat;
    private int page;
    private int size;

}
