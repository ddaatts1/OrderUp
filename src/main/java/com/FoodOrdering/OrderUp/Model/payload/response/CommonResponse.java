package com.FoodOrdering.OrderUp.Model.payload.response;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private int code;
    private String message;
    private T data;
}
