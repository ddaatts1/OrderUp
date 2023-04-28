package com.FoodOrdering.OrderUp.DeliverFee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaculateFeeResData {

    int total;
    int service_fee;
    int insurance_fee;
    int pick_station_fee;
    int coupon_value;
    int r2s_fee;
    int document_return;
    int double_check;
    int cod_fee;
    int pick_remote_areas_fee;
    int deliver_remote_areas_fee;
    int cod_failed_fee;


}
