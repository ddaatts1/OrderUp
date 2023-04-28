package com.FoodOrdering.OrderUp.DeliverFee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetServiceResData {
   int  service_id;
         String   short_name;
    int service_type_id;


}
