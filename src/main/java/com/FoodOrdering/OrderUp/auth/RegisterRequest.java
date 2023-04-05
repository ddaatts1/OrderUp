package com.FoodOrdering.OrderUp.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String name;
  private String email;
  private String password;
  private String address_detail;
  private Double address_long;
  private Double address_lat;
  private String phone;

}
