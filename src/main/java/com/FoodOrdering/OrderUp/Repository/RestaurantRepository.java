package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Model.Restaurant;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
    Optional<Restaurant> findByEmail(String email);
    List<Restaurant> findAllBy_idIn(List<ObjectId> ids);
    Optional<Restaurant> findBy_id(ObjectId id);
    Optional<Restaurant> findByUid(String uid);

    Page<Restaurant> findAllByRole(String role, Pageable pageable);
}
