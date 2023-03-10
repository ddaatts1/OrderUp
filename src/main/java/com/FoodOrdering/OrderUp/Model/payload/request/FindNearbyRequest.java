package com.FoodOrdering.OrderUp.Model.payload.request;

import lombok.Data;

@Data
public class FindNearbyRequest {

    private Double userLat;
    private Double userLong;
    private Double searchRadius;

}
