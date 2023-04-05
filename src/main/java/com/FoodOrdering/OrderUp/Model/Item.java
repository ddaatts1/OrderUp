package com.FoodOrdering.OrderUp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "items")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @Id
    private ObjectId _id;
    private String name;
    private String detail;
    private int price;
    private String status;
    private int ordered;
    @Field(name = "restaurant_id")
    private  ObjectId restaurantid;

}
