package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Model.AverageRating;
import com.FoodOrdering.OrderUp.Model.Item;

import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.payload.request.AddItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.EditItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OnOffItemRequest;
import com.FoodOrdering.OrderUp.MongoConfig.MongoConfig;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MongoRepo {


    @Autowired
    MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    String db;

    public MongoRepo(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public List<Item> getItem() {
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("items");

        FindIterable<Document> iterable = collection.find();

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

        MongoDatabase mongoDatabase = mongoClient.getDatabase(db);
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

        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("average_rating");

        Bson filter = Filters.in("referenceId",listItemId);

        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor= findIterable.cursor();

        while (cursor.hasNext()){
            Document document = cursor.next();

            String id = document.get("_id").toString();
            int numrating = document.get("num_rating",Integer.class);
            Double averagerating = document.get("average_rating",Double.class);
            String referenceID  = document.get("referenceId").toString();
            AverageRating averageRating = new AverageRating(new ObjectId(id),numrating,averagerating,new ObjectId(referenceID));
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


    public boolean addItem(AddItemRequest item) {
        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("items");

        // init
        Document newitem = new Document();
        newitem.append("name",item.getName());
        newitem.append("detail",item.getDetail());
        newitem.append("price",item.getPrice());
        newitem.append("status","ON");
        newitem.append("restaurant_id", new ObjectId(item.getRestaurant_id()));
        newitem.append("images", item.getImages());
        newitem.append("ordered",0);

        InsertOneResult insertOneResult = collection.insertOne(newitem);
        if(insertOneResult.getInsertedId()!= null){
            return true;
        }

        return false;

    }

    public boolean onoffitem(OnOffItemRequest onOffItemRequest) {

        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("items");

        Bson filter = Filters.eq("_id",new ObjectId(onOffItemRequest.get_id()));
        Bson update = Updates.set("status",onOffItemRequest.getStatus());

        UpdateResult updateResult= collection.updateOne(filter,update);

        if(updateResult.getMatchedCount() == 1){
            return true;
        }
        return  false;
    }

    public boolean editItem(EditItemRequest item) {


        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection  = database.getCollection("items");


        Bson filter = Filters.eq("_id",new ObjectId(item.get_id()));
        Bson update = Updates.combine(
                Updates.set("name",item.getName()),
                Updates.set("detail",item.getDetail()),
                Updates.set("price",item.getPrice())
        );

        UpdateResult updateResult= collection.updateOne(filter,update);
            if(updateResult.getMatchedCount() == 1)
                return true;

        return false;

}}



