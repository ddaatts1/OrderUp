package com.FoodOrdering.OrderUp.Repository;


import com.FoodOrdering.OrderUp.Model.Item;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item,ObjectId> {

    Page<Item> findAll(Pageable pageable);
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.media m LEFT JOIN FETCH i.averageRating ar")
    Page<Item> findAllWithMediaAndAverageRating(Pageable pageable);
    Page<Item> findByRestaurantid(ObjectId restaurantId, Pageable pageable);

}
