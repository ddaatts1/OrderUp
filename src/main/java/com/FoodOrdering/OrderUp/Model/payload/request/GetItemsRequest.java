package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;

@Data
public class GetItemsRequest {

    private String restaurant_id="";
    private String category_id="";
    private Double address_long=-1d;
    private Double address_lat=-1d;
    private int page=-1;
    private int size=-1;
}
