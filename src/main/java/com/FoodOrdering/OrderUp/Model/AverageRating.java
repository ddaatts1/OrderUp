package com.FoodOrdering.OrderUp.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "average_rating")
public class AverageRating {

    @Id
    private ObjectId _id;
//    private int num_rating;
    private Double average_rating;
    private ObjectId referenceId;

}
