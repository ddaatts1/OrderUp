package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Model.AverageRating;
import com.FoodOrdering.OrderUp.Model.Item;

import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.MongoConfig.MongoConfig;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MongoRepo {


    @Autowired
    MongoClient mongoClient;

    public MongoRepo(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public List<Item> getItem() {
        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> colelction = database.getCollection("items");

        FindIterable<Document> iterable = colelction.find();

        MongoCursor<Document> cursor = iterable.cursor();


        List<Item> list = iterable.map(i -> {

            String id = i.get("_id").toString();
            String name = i.get("name",String.class);
            String detail = i.get("detail",String.class);
            int price = i.get("price",Integer.class);
            String status = i.get("status",String.class);
            int ordered = i.get("ordered",Integer.class);
            String restaurant_id  = i.get("restaurant_id").toString();

            Item item = new Item(new ObjectId(id),name,detail,Integer.valueOf(price),status,Integer.valueOf(ordered),new ObjectId(restaurant_id));

            return item;
        }).into(new ArrayList<>());

return list;
    }



    public List<Media> geListMediaByListItem(List<Item> items){
        List<Media> listMedia = new ArrayList<>();

        // get list item id
        List<ObjectId> listItemId = items.stream().map(i->i.get_id()).collect(Collectors.toList());

        MongoDatabase mongoDatabase = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = mongoDatabase.getCollection("media");

        Bson filters = Filters.in("referenceId",listItemId);

        FindIterable<Document> findIterable = collection.find(filters);
        MongoCursor<Document> cursor = findIterable.cursor();

        while (cursor.hasNext()){
            Document document = cursor.next();

            String id = document.get("_id").toString();
            String url = document.getString("media_url");
            String referenceId = document.get("referenceId").toString();
            String type =document.getString("media_url");
            String seq = document.get("seq").toString();
            Media media = new Media(new ObjectId(id),url,type,Integer.valueOf(seq),new ObjectId(referenceId));
            listMedia.add(media);
        }
        return listMedia;

    }

    public List<AverageRating> getListAverageRatingByListItem(List<Item> items){
        List<AverageRating> averageRatingList = new ArrayList<>();

        //get list item id
        List<ObjectId> listItemId = items.stream().map(i->i.get_id()).collect(Collectors.toList());

        MongoDatabase database= mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("average_rating");

        Bson filter = Filters.in("referenceId",listItemId);

        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor= findIterable.cursor();

        while (cursor.hasNext()){
            Document document = cursor.next();

            String id = document.get("_id").toString();
            int numrating = document.get("num_rating",Integer.class);
            Double averagerating = document.get("average_rating",Double.class);

            AverageRating averageRating = new AverageRating(new ObjectId(id),numrating,averagerating);
            averageRatingList.add(averageRating);
        }

        return averageRatingList;
    }





    public static void main(String[] args) {

        MongoConfig mongoConfig = new MongoConfig();
        MongoRepo mongoRepo= new MongoRepo(mongoConfig.mongoClient());
        List<Item> item = mongoRepo.getItem();

//        List<Media> media = mongoRepo.geListMediaByListItem(item);
        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(item);


        System.out.println(averageRatingList);

    }


}



