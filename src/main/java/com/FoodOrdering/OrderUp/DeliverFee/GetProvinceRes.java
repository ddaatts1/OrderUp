package com.FoodOrdering.OrderUp.DeliverFee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProvinceRes {
    private int ProvinceID;
    private List<String> NameExtension;
}
