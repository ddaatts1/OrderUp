package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class AddItemRequest {
    private String name;
    private String detail;
    private int price;
    private List<String> categories;
    private  String restaurant_id;
    private List<String> images;
}
