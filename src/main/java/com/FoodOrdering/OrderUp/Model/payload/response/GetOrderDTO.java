package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderDTO {
        String customerName;
        String foodOrderId;
        String customerPhone;
          String customerAddress;
          Date orderDatetime;
          String restaurant_id;
          String orderStatus;
          String note;
          String method;
          List<FoodOrderDTO> foodOrderList;


}
