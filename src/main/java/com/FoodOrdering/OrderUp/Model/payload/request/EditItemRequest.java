package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class EditItemRequest {
    private String _id;
    private String name;
    private String detail;
    private int price;
    private List<String> categories;
    private List<String> images;

}
