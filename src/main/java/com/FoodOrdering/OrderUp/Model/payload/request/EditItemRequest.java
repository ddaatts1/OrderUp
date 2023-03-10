package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class EditItemRequest {
    private String _id;
    private String name;
    private String detail;
    private int price;
}
