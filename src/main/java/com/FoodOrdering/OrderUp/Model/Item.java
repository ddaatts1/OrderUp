package com.FoodOrdering.OrderUp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private  ObjectId restaurant_id;



}
