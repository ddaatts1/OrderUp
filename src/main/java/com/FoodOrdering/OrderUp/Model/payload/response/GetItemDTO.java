package com.FoodOrdering.OrderUp.Model.payload.response;

import com.FoodOrdering.OrderUp.Model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetItemDTO {
    private Item item ;
    private String image_url ;
    private Double rate_average;
    private Double distance;
}
