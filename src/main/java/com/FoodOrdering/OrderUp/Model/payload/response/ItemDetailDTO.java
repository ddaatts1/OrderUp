package com.FoodOrdering.OrderUp.Model.payload.response;

import com.FoodOrdering.OrderUp.Model.payload.RelateItem;
import lombok.Data;

import java.util.List;

@Data
public class ItemDetailDTO {


    private String _id;
    private String name;
    private  String detail;
    private String image;
    private int price;
    private List<String> categories;
    private RestaurantDTO restaurant;
    private Double rating;
    private List<RelateItem> restaurantRelateItemList;
    private List<RelateItem> relateItemList;

}
