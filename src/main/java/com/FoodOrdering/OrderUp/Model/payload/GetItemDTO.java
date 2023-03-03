package com.FoodOrdering.OrderUp.Model.payload;

import com.FoodOrdering.OrderUp.Model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetItemDTO {
    Item item ;
    String image_url ;
    Double rate_average;
}
