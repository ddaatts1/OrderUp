package com.FoodOrdering.OrderUp.DeliverFee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetDistrictRes {

    private int DistrictID;
    private int ProvinceID;
    private List<String> NameExtension;
}
