package com.FoodOrdering.OrderUp.DeliverFee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaculateFeeRes {

    int code;
    String message;
    CaculateFeeResData data;
}
