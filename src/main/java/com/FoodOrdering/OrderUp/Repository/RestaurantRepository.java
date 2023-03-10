package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Model.Restaurant;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
}
