package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Model.Media;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends MongoRepository<Media, ObjectId> {

}
