package com.FoodOrdering.OrderUp.Model.payload.request;


import com.FoodOrdering.OrderUp.Model.payload.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    String name;
    String phone;
    String method;
    String note;
    Double address_lat;
    Double address_long;
    String address_detail;
    List<OrderItem> items;

}
