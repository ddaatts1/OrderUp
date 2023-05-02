package com.FoodOrdering.OrderUp.Model.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllStatDTO {

    private long resCount;
    private long itemCount;
    private  long newResCount;

}
