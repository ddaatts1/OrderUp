package com.FoodOrdering.OrderUp.Repository;

import com.FoodOrdering.OrderUp.Constant.OrderStatus;
import com.FoodOrdering.OrderUp.Model.AverageRating;

import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.payload.OrderItem;
import com.FoodOrdering.OrderUp.Model.payload.request.AddItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.EditItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OnOffItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OrderRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.*;
import com.FoodOrdering.OrderUp.MongoConfig.MongoConfig;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
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

//    public List<Item> getItem() {
//        MongoDatabase database = mongoClient.getDatabase(db);
//        MongoCollection<Document> collection = database.getCollection("items");
//
//        FindIterable<Document> iterable = collection.find();
//
//        MongoCursor<Document> cursor = iterable.cursor();
//
//
//        List<Item> list = iterable.map(i -> {
//
//            String id = i.get("_id").toString();
//            String name = i.get("name",String.class);
//            String detail = i.get("detail",String.class);
//            int price = i.get("price",Integer.class);
//            String status = i.get("status",String.class);
//            int ordered = i.get("ordered",Integer.class);
//            String restaurant_id  = i.get("restaurant_id").toString();
//
//            Item item = new Item(new ObjectId(id),name,detail,Integer.valueOf(price),status,Integer.valueOf(ordered),new ObjectId(restaurant_id));
//
//            return item;
//        }).into(new ArrayList<>());
//
//return list;
//    }



    public List<Media> geListMediaByListItem(List<GetItemDTO> items){
        List<Media> listMedia = new ArrayList<>();

        // get list item id
        List<String> listItemId = items.stream().map(i->i.get_id()).collect(Collectors.toList());

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

    public List<AverageRating> getListAverageRatingByListItem(List<GetItemDTO> items){
        List<AverageRating> averageRatingList = new ArrayList<>();

        //get list item id
        List<ObjectId> listItemId = items.stream().map(i->{
            String id = i.get_id();
          return  new ObjectId(id) ;
        }).collect(Collectors.toList());

        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("average_rating");

        Bson filter = Filters.in("referenceId",listItemId);

        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor= findIterable.cursor();

        while (cursor.hasNext()){
            Document document = cursor.next();
            String id = document.get("_id").toString();
//            int numrating = document.get("num_rating",Integer.class);
            Double averagerating = document.get("average_rating",Double.class);
            String referenceID  = document.get("referenceId").toString();
            AverageRating averageRating = new AverageRating(new ObjectId(id),averagerating,new ObjectId(referenceID));
            averageRatingList.add(averageRating);
        }

        return averageRatingList;
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
        newitem.append("categories",item.getCategories());
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
                Updates.set("price",item.getPrice()),
                Updates.set("images",item.getImages()),
                Updates.set("categories",item.getCategories())
        );

        UpdateResult updateResult= collection.updateOne(filter,update);
            if(updateResult.getMatchedCount() == 1)
                return true;

        return false;

}

public List<GetItemDTO> getItemByCategory(String category){
        List<GetItemDTO> list = new ArrayList<>();
    MongoDatabase database = mongoClient.getDatabase(db);
    MongoCollection<Document> collection = database.getCollection("items");

    Bson filter = Filters.in("categories", Arrays.asList(category));
    FindIterable<Document> findIterable =  collection.find(filter);
    MongoCursor<Document> cursor = findIterable.cursor();

    Document document = new Document();
    while (cursor.hasNext()){
        document = cursor.next();
        ObjectId id = document.get("_id",ObjectId.class);
        String _id = id.toString();
        String name = document.get("name",String.class);
        ObjectId resId = document.get("restaurant_id",ObjectId.class);
        String restaurantId =  resId.toString();
        int price = document.get("price",Integer.class);
        List<String> images = document.get("images",ArrayList.class);
        String image=null;
        if(images != null)
        if(images.size()>0){
            image = images.get(0);
        }
        int ordered = document.get("ordered",Integer.class);


        GetItemDTO getItemDTO= new GetItemDTO();
        getItemDTO.set_id(_id);
        getItemDTO.setImage_url(image);
        getItemDTO.setName(name);
        getItemDTO.setOrdered(ordered);
        getItemDTO.setPrice(price);
        getItemDTO.setRestaurantId(restaurantId);
        list.add(getItemDTO);
    }
        return list;
}

public void test(){
        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("items");

        Bson filter = Filters.in("categories", Arrays.asList("CHAO","BUN"));
        FindIterable<Document> findIterable =  collection.find(filter);
        MongoCursor<Document> cursor = findIterable.cursor();

        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
}

    public static void main(String[] args) {

        MongoConfig mongoConfig = new MongoConfig();
        MongoRepo mongoRepo= new MongoRepo(mongoConfig.mongoClient());
//        List<Item> item = mongoRepo.getItem();
//
//        List<Media> media = mongoRepo.geListMediaByListItem(item);
//        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(item);
//
//
//        System.out.println(averageRatingList);
        mongoRepo.test();


    }

    public boolean order(OrderRequest orderRequest) {

        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("food_order");


        Set<String> resId = new HashSet<>();
        orderRequest.getItems().stream().forEach(i->{
            resId.add(i.getRestaurantId());
        });

        resId.stream().forEach(r->{
            MongoCollection<Document> finalCollection = database.getCollection("food_order");
            Document foodOrder = new Document();
            foodOrder.append("customerPhone",orderRequest.getPhone());
            foodOrder.append("customerAddress",orderRequest.getAddress_detail());
            foodOrder.append("orderDatetime",new Date());
            foodOrder.append("restaurant_id",new ObjectId(r));
            foodOrder.append("note",orderRequest.getNote());
            foodOrder.append("method",orderRequest.getMethod());
            foodOrder.append("orderStatus", OrderStatus.PENDING);
            foodOrder.append("customerName",orderRequest.getName());
            InsertOneResult insertFoodOrder = finalCollection.insertOne(foodOrder);
            finalCollection = database.getCollection("order_item");
            BsonValue foodOrderId = insertFoodOrder.getInsertedId();
            List<OrderItem> list = orderRequest.getItems();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getRestaurantId().equalsIgnoreCase(r)){
                    Document orderItem = new Document();
                    orderItem.append("orderId",foodOrderId.asObjectId().getValue());
                    orderItem.append("itemId",new ObjectId(list.get(i).get_id()));
                    orderItem.append("quantity",list.get(i).getQuantity());
                    orderItem.append("isRated",false);
                    orderItem.append("rating",0d);
                    InsertOneResult insertOneResult = finalCollection.insertOne(orderItem);

                    MongoCollection itemCOlelction =database.getCollection("items");
                    Bson filter = Filters.eq("_id", new ObjectId(list.get(i).get_id()));
                    Bson update = Updates.inc("ordered",list.get(i).getQuantity());
                    itemCOlelction.updateOne(filter,update);
                }

            }

        });






        return  true;
    }

    public List<GetOrderDTO> getOrder(String restaurantId, String status) {
        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("food_order");

        List<GetOrderDTO> getOrderDTOList = new ArrayList<>();
        Bson filter = Filters.and(Filters.eq("restaurant_id",new ObjectId(restaurantId)),Filters.eq("orderStatus",status));

        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor = findIterable.cursor();

        while (cursor.hasNext()){
            GetOrderDTO getOrderDTO = new GetOrderDTO();

            Document document= cursor.next();
            System.out.println("===============food order: "+ document);
            getOrderDTO.setRestaurant_id(document.get("restaurant_id",ObjectId.class).toString());
            getOrderDTO.setNote(document.get("note",String.class));
            getOrderDTO.setMethod(document.get("method",String.class));
            getOrderDTO.setCustomerPhone(document.get("customerPhone",String.class));
            getOrderDTO.setCustomerAddress(document.get("customerAddress",String.class));
            getOrderDTO.setOrderStatus(document.get("orderStatus",String.class));
            getOrderDTO.setOrderDatetime(document.get("orderDatetime",Date.class));
            getOrderDTO.setFoodOrderId(document.get("_id",ObjectId.class).toString());
            getOrderDTO.setCustomerName(document.get("customerName",String.class));

            collection = database.getCollection("order_item");
            Bson filter1 = Filters.eq("orderId",document.get("_id",ObjectId.class));

            FindIterable<Document> findIterable1 =  collection.find(filter1);
            MongoCursor<Document> cursor1 = findIterable1.cursor();
            List<FoodOrderDTO> foodOrderDTOList = new ArrayList<>();
            while (cursor1.hasNext()){
                Document orderItem = cursor1.next();

                MongoCollection<Document> itemCollection = database.getCollection("items");
                FindIterable<Document> findItemIterable = itemCollection.find(Filters.eq("_id",orderItem.get("itemId",ObjectId.class)));
                MongoCursor<Document> itemCursor  = findItemIterable.cursor();
                FoodOrderDTO foodOrderDTO= new FoodOrderDTO();
                if(itemCursor.hasNext()){
                    Document item = itemCursor.next();
                    foodOrderDTO.setName(item.get("name",String.class));
                    foodOrderDTO.setPrice(item.get("price",Integer.class));
                    foodOrderDTO.setQuantity(orderItem.get("quantity",Integer.class));

                }
                foodOrderDTOList.add(foodOrderDTO);

            }
            getOrderDTO.setFoodOrderList(foodOrderDTOList);
            getOrderDTOList.add(getOrderDTO);
        }

        return getOrderDTOList;

    }

    public void changeOrderStatus(OnOffItemRequest changeOrderStatusRequest) {

        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("food_order");

        Bson filter = Filters.eq("_id",new ObjectId(changeOrderStatusRequest.get_id()));
        Bson update = Updates.set("orderStatus",changeOrderStatusRequest.getStatus());
        collection.updateOne(filter,update);

    }

    public Document getRestaurantStat(ObjectId id) {

        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("items");

        int order = 0;
        int countProduct=0;
        Bson itemFilter = Filters.eq("restaurant_id",id);
        FindIterable<Document> itemIterable = collection.find(itemFilter);
        MongoCursor<Document> cursor =  itemIterable.cursor();

        while (cursor.hasNext()){
            Document document = cursor.next();
            int or = document.get("ordered",Integer.class);
            order+=or;
            countProduct++;
        }

        return new Document().append("order",order)
                .append("product",countProduct);
    }

    public Double getAverageRating(ObjectId id){
        MongoDatabase database =mongoClient.getDatabase(db);
        MongoCollection<Document> collection= database.getCollection("average_rating");

        FindIterable<Document> findIterable= collection.find(Filters.eq("referenceId",id));
    MongoCursor<Document> cursor= findIterable.cursor();
    if(cursor.hasNext()){
        return cursor.next().get("average_rating",Double.class);
    }

    return -1d;

    }

    public void addGoogleUid(ObjectId id, String uid) {

        MongoDatabase database =mongoClient.getDatabase(db);
        MongoCollection<Document> collection= database.getCollection("restaurants");

        Bson filter = Filters.eq("_id",id);
        Bson update = Updates.set("uid",uid);
        collection.updateOne(filter,update);
    }

    public List<GetOrderDTO> userGetOrder(String phone,String status) {

        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("food_order");

        List<GetOrderDTO> getOrderDTOList = new ArrayList<>();
        Bson filter = Filters.eq("customerPhone",phone);

        if(!status.equalsIgnoreCase("ALL")){
            filter = Filters.and(Filters.eq("customerPhone",phone), Filters.eq("orderStatus",status));
        }

        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor = findIterable.cursor();

        while (cursor.hasNext()){
            GetOrderDTO getOrderDTO = new GetOrderDTO();

            Document document= cursor.next();
            System.out.println("===============food order: "+ document);
            getOrderDTO.setRestaurant_id(document.get("restaurant_id",ObjectId.class).toString());
            getOrderDTO.setNote(document.get("note",String.class));
            getOrderDTO.setMethod(document.get("method",String.class));
            getOrderDTO.setCustomerPhone(document.get("customerPhone",String.class));
            getOrderDTO.setCustomerAddress(document.get("customerAddress",String.class));
            getOrderDTO.setOrderStatus(document.get("orderStatus",String.class));
            getOrderDTO.setOrderDatetime(document.get("orderDatetime",Date.class));
            getOrderDTO.setFoodOrderId(document.get("_id",ObjectId.class).toString());
            getOrderDTO.setCustomerName(document.get("customerName",String.class));

            collection = database.getCollection("order_item");
            Bson filter1 = Filters.eq("orderId",document.get("_id",ObjectId.class));

            FindIterable<Document> findIterable1 =  collection.find(filter1);
            MongoCursor<Document> cursor1 = findIterable1.cursor();
            List<FoodOrderDTO> foodOrderDTOList = new ArrayList<>();
            while (cursor1.hasNext()){
                Document orderItem = cursor1.next();

                MongoCollection<Document> itemCollection = database.getCollection("items");
                FindIterable<Document> findItemIterable = itemCollection.find(Filters.eq("_id",orderItem.get("itemId",ObjectId.class)));
                MongoCursor<Document> itemCursor  = findItemIterable.cursor();
                FoodOrderDTO foodOrderDTO= new FoodOrderDTO();
                if(itemCursor.hasNext()){
                    Document item = itemCursor.next();
                    foodOrderDTO.setId(item.get("_id",ObjectId.class).toString());
                    foodOrderDTO.setName(item.get("name",String.class));
                    foodOrderDTO.setPrice(item.get("price",Integer.class));
                    foodOrderDTO.setQuantity(orderItem.get("quantity",Integer.class));
                    foodOrderDTO.setRated(orderItem.get("isRated",Boolean.class));

                }
                foodOrderDTOList.add(foodOrderDTO);

            }
            getOrderDTO.setFoodOrderList(foodOrderDTOList);
            getOrderDTOList.add(getOrderDTO);
        }

        return getOrderDTOList;
    }

    public void rate(String itemId, String orderItemId, double rattingValue) {
        MongoDatabase database =  mongoClient.getDatabase(db);
        MongoCollection<Document> orderItemCollection = database.getCollection("order_item");

        Bson orderItemFilter = Filters.and(Filters.eq("orderId",new ObjectId(orderItemId)),
                                            Filters.eq("itemId", new ObjectId(itemId))
                );

        orderItemCollection.updateOne(orderItemFilter,Updates.set("isRated",true));


        MongoCollection<Document> ratingCollection = database.getCollection("ratings");
        Document rating= new Document();
        rating.append("referenceId",new ObjectId(itemId));
        rating.append("rating_value",rattingValue);
        rating.append("createDate",new Date());

        ratingCollection.insertOne(rating);

    }

    public void dailyCalculateRating() {
        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("ratings");
        MongoCollection<Document> avgRatingCollection = database.getCollection("average_rating");

        collection.aggregate(
                Collections.singletonList(Aggregates.group("$referenceId", Accumulators.avg("averageRating", "$rating_value")))
        ).forEach((Document doc) -> {
            try {
                ObjectId referenceId = doc.getObjectId("_id");
                double averageRating = doc.getDouble("averageRating");
                String formattedRating = String.format("%.2f", averageRating);

                Document avgRatingDoc = new Document("referenceId", referenceId)
                        .append("average_rating", Double.parseDouble(formattedRating));

                Document filter = new Document("referenceId", referenceId);
                ReplaceOptions options = new ReplaceOptions().upsert(true);

                avgRatingCollection.replaceOne(filter, avgRatingDoc,options);
            } catch (Exception e) {
                System.out.println("Error updating document: " + e.getMessage());
            }
        });
    }

    public List<GetOrderDTO> getOrderByDay(ObjectId resId) {
        MongoDatabase database = mongoClient.getDatabase("OrderUp");
        MongoCollection<Document> collection = database.getCollection("food_order");

        List<GetOrderDTO> getOrderDTOList = new ArrayList<>();
        Bson filter = Filters.eq("restaurant_id",resId);


        FindIterable<Document> findIterable = collection.find(filter);
        MongoCursor<Document> cursor = findIterable.cursor();

        while (cursor.hasNext()){
            GetOrderDTO getOrderDTO = new GetOrderDTO();
            Document document= cursor.next();
            System.out.println("===============food order: "+ document);
            getOrderDTO.setRestaurant_id(document.get("restaurant_id",ObjectId.class).toString());
            getOrderDTO.setNote(document.get("note",String.class));
            getOrderDTO.setMethod(document.get("method",String.class));
            getOrderDTO.setCustomerPhone(document.get("customerPhone",String.class));
            getOrderDTO.setCustomerAddress(document.get("customerAddress",String.class));
            getOrderDTO.setOrderStatus(document.get("orderStatus",String.class));
            getOrderDTO.setOrderDatetime(document.get("orderDatetime",Date.class));
            getOrderDTO.setFoodOrderId(document.get("_id",ObjectId.class).toString());
            getOrderDTO.setCustomerName(document.get("customerName",String.class));

            collection = database.getCollection("order_item");
            Bson filter1 = Filters.eq("orderId",document.get("_id",ObjectId.class));

            FindIterable<Document> findIterable1 =  collection.find(filter1);
            MongoCursor<Document> cursor1 = findIterable1.cursor();
            List<FoodOrderDTO> foodOrderDTOList = new ArrayList<>();
            while (cursor1.hasNext()){
                Document orderItem = cursor1.next();

                MongoCollection<Document> itemCollection = database.getCollection("items");
                FindIterable<Document> findItemIterable = itemCollection.find(Filters.eq("_id",orderItem.get("itemId",ObjectId.class)));
                MongoCursor<Document> itemCursor  = findItemIterable.cursor();
                FoodOrderDTO foodOrderDTO= new FoodOrderDTO();
                if(itemCursor.hasNext()){
                    Document item = itemCursor.next();
                    foodOrderDTO.setId(item.get("_id",ObjectId.class).toString());
                    foodOrderDTO.setName(item.get("name",String.class));
                    foodOrderDTO.setPrice(item.get("price",Integer.class));
                    foodOrderDTO.setQuantity(orderItem.get("quantity",Integer.class));
                    foodOrderDTO.setRated(orderItem.get("isRated",Boolean.class));

                }
                foodOrderDTOList.add(foodOrderDTO);

            }
            getOrderDTO.setFoodOrderList(foodOrderDTOList);
            getOrderDTOList.add(getOrderDTO);
        }

        return getOrderDTOList;

    }


    public GetAllStatDTO getAllStat() {

        long resCount=0;
        long itemCount = 0;
        long newResCount =0;
        GetAllStatDTO getAllStatDTO= new GetAllStatDTO();

        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("restaurants");
         resCount = collection.countDocuments();
        collection = database.getCollection("items");
        itemCount = collection.countDocuments();
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

// Construct a filter to match documents with a "createDate" field in the current month
//        Bson filter = Filters.and(
//                Filters.eq("createDate.year", now.getYear()),
//                Filters.eq("createDate.month", now.getMonthValue())
//        );
// Get the start of the current month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = cal.getTime();

// Get the start of the next month
        cal.add(Calendar.MONTH, 1);
        Date startOfNextMonth = cal.getTime();

// Construct a filter to match documents with a "createDate" field in the current month
        Bson filter = Filters.and(
                Filters.gte("createDate", startOfMonth),
                Filters.lt("createDate", startOfNextMonth)
        );


// Count the documents matching the filter
       collection = database.getCollection("restaurants");
        newResCount= collection.countDocuments(filter);

        getAllStatDTO.setItemCount(itemCount);
        getAllStatDTO.setNewResCount(newResCount);
        getAllStatDTO.setResCount(resCount);

        return getAllStatDTO;
    }

    public List<RestaurantDTO> getAllRes() {

        List<RestaurantDTO> restaurantDTOS = new ArrayList<>();

        MongoDatabase database= mongoClient.getDatabase(db);
        MongoCollection<Document> collection =  database.getCollection("restaurants");
        Bson filter = Filters.eq("role","MANAGER");
        FindIterable<Document> findIterable= collection.find(filter);
        MongoCursor<Document> cursor= findIterable.cursor();




        return  restaurantDTOS;
    }



    public RestaurantDTO docToRestaurantDTO(Document document){
        RestaurantDTO restaurantDTO= new RestaurantDTO();

        restaurantDTO.set_id(document.get("_id",ObjectId.class).toString());
        restaurantDTO.setAddress(document.get("address",String.class));
        restaurantDTO.setEmail(document.get("email",String.class));
        restaurantDTO.setPhone(document.get("phone",String.class));
        restaurantDTO.setName(document.get("name",String.class));

        return restaurantDTO;
    }

    public boolean onoffRes(OnOffItemRequest onOffItemRequest) {

        MongoDatabase database = mongoClient.getDatabase(db);
        MongoCollection<Document> collection = database.getCollection("restaurants");

        boolean status = onOffItemRequest.getStatus().equalsIgnoreCase("ON")? true: false;
        Bson filter = Filters.eq("_id",new ObjectId(onOffItemRequest.get_id()));
        Bson update = Updates.set("status",status);

        UpdateResult updateResult= collection.updateOne(filter,update);

        if(updateResult.getMatchedCount() == 1){
            return true;
        }
        return  false;
    }
}



