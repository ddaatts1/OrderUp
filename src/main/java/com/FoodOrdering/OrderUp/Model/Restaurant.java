package com.FoodOrdering.OrderUp.Model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "restaurants")
public class Restaurant {

    @Id
    private ObjectId _id;
    private String name;
    private String email;
    private String password;
    private String status;
    private String address_detail;
    private Double address_long;
    private Double address_lat;
    private String phone;

}
