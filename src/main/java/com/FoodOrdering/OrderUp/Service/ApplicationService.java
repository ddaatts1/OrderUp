package com.FoodOrdering.OrderUp.Service;

import com.FoodOrdering.OrderUp.Controller.AdminController;
import com.FoodOrdering.OrderUp.Model.AverageRating;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.Restaurant;
import com.FoodOrdering.OrderUp.Model.payload.RelateItem;
import com.FoodOrdering.OrderUp.Model.payload.request.*;
import com.FoodOrdering.OrderUp.Model.payload.response.*;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.FoodOrdering.OrderUp.Repository.RestaurantRepository;
import com.FoodOrdering.OrderUp.googleMapService.DistanceCalculator;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.config.ldap.LdapUserServiceBeanDefinitionParser;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    MongoRepo mongoRepo;
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    GoogleApiService googleApiService;
    @Autowired
    RestaurantRepository restaurantRepository;


    Logger log = LoggerFactory.getLogger(ApplicationService.class);


//    public List<GetItemDTO> getListItem(GetItemsRequest getItemsRequest){
//        List<GetItemDTO> list = new ArrayList<>();
//
//        // get list item
//        Page<Item> items = itemRepository.findAll(PageRequest.of(getItemsRequest.getPage(), getItemsRequest.getSize()));
//        List<Item> listItem = items.getContent();
//
//        // get list media
////        List<Media> mediaList = mongoRepo.geListMediaByListItem(listItem);
////        Map<ObjectId,String> mapItemMedia = new HashMap<>();
////        // set map
////        for (Media m:mediaList
////             ) {
////            if(m.getSeq() == 1){
////                mapItemMedia.put(m.getReferenceId(),m.getMediaUrl());
////            }
////        }
//        //get list average rating
//        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(listItem);
//        Map<ObjectId,Double> mapItemAverageRating = new HashMap<>();
//        //set map
//        for (AverageRating a:averageRatingList
//             ) {
//            mapItemAverageRating.put(a.getReferenceId(),a.getAverage_rating());
//        }
//
//        // set list GetItemDTO
//        for (Item i:listItem
//             ) {
//            GetItemDTO getItemDTO = new GetItemDTO();
//            getItemDTO.setRate_average(mapItemAverageRating.get(i.get_id()));
//            list.add(getItemDTO);
//        }
//
//        return list;
//    }


    public CommonResponse<Object> addItem(AddItemRequest item) {
        CommonResponse<Object> insertResult = new CommonResponse<>();

        boolean check = mongoRepo.addItem(item);
        if (check) {
            insertResult.setCode(1);
            insertResult.setMessage("Thêm thành công");
        } else {
            insertResult.setCode(0);
            insertResult.setMessage("insert thất bại");
        }

        return insertResult;
    }

    public CommonResponse<Object> onnoffitem(OnOffItemRequest onOffItemRequest) {
        CommonResponse<Object> response = new CommonResponse<>();

        List<String> status = Arrays.asList("ON", "OFF");
        if (!status.contains(onOffItemRequest.getStatus())) {
            response.setCode(0);
            response.setMessage("status phai la on hoc off");
            return response;
        }
        boolean check = mongoRepo.onoffitem(onOffItemRequest);
        if (check) {
            response.setCode(1);
            response.setMessage(onOffItemRequest.getStatus() + " thanh cong");
        }

        return response;
    }

    public CommonResponse<Object> editItem(EditItemRequest item) {
        CommonResponse<Object> response = new CommonResponse<>();

        boolean check = mongoRepo.editItem(item);
        if (check) {
            response.setCode(1);
            response.setMessage("Sửa thành công");
            return response;
        } else {
            response.setCode(0);
            response.setMessage("Sửa thất bại");
        }

        return response;
    }

    public CommonResponse<Object> findNearby(FindNearbyRequest findNearbyRequest) {
        CommonResponse<Object> response = new CommonResponse<>();


        return response;
    }

//    public Page<Item> adminGetListItems(String itemId,int page, int size) {
//
//        Page<Item> item = null ;
//
//       item =  itemRepository.findByRestaurantid(new ObjectId(itemId),PageRequest.of(page,size));
//       item.getContent().stream().map((i)->{
//           return new Document("_id",i.get_id().toString()).append("name",i.getName());
//       });
//        return item;
//    }

    public Page<Document> adminGetListItems(String itemId, int page, int size) {
        Page<Item> items = itemRepository.findByRestaurantid(new ObjectId(itemId), PageRequest.of(page, size));
        List<Document> documents = items.getContent().stream()
                .map(i -> new Document("_id", i.get_id().toString())
                        .append("name", i.getName())
                        .append("price", i.getPrice())
                        .append("status", i.getStatus())
                )
                .collect(Collectors.toList());
        return new PageImpl<>(documents, items.getPageable(), items.getTotalElements());
    }

    public CommonResponse<Object> deleteItem(String id) {
        CommonResponse<Object> response = new CommonResponse<>();

        try {
            Optional<Item> deleteItem = itemRepository.findById(new ObjectId(id));

            if (deleteItem.isPresent()) {
                itemRepository.delete(deleteItem.get());
            } else {
                response.setData(id);
                response.setCode(0);
                response.setMessage("that bai");
                return response;
            }


        } catch (Exception e) {
            response.setData(id);
            response.setCode(0);
            response.setMessage("that bai");
        }
        response.setData(id);
        response.setCode(1);
        response.setMessage("xoa thanh cong ");

        return response;

    }



    public CommonResponse<Object> userGetItems(GetItemsRequest getItemsRequest) {
        CommonResponse<Object> response = new CommonResponse<>();
        List<GetItemDTO> list = new ArrayList<>();

        if (getItemsRequest.getAddress_lat() == null || getItemsRequest.getAddress_long() == null) {
            log.info("lat long == null");
        }

        if (getItemsRequest.getRestaurant_id() == null || getItemsRequest.getRestaurant_id().isEmpty()) {
            log.info("restaurant id = null");
        }

        if (getItemsRequest.getCategory_id() == null || getItemsRequest.getCategory_id().isEmpty()) {
            log.info("category is null");
        }

        List<Item> itemList = new ArrayList<>();
        Page<Item> itemPage = null;
        List<GetItemDTO> listItem = new ArrayList<>();

        if (getItemsRequest.getRestaurant_id() != "") {
            log.info("find item by restaurant_id");
            itemPage = itemRepository.findByRestaurantid(new ObjectId(getItemsRequest.getRestaurant_id()),
                    PageRequest.of(getItemsRequest.getPage(), getItemsRequest.getSize()));
            listItem = itemPage.getContent().stream().map(i->{
                GetItemDTO getItemDTO = new GetItemDTO();
                getItemDTO.set_id(i.get_id().toString());
                getItemDTO.setName(i.getName());
                getItemDTO.setRestaurantId(i.getRestaurantid().toString());
                List<String> images = i.getImages();
                String image = null;
                if(images != null ){
                    if(images.size() > 0){
                        image = images.get(0);
                    }
                }
                getItemDTO.setImage(image);
                getItemDTO.setPrice(i.getPrice());
                getItemDTO.setImage_url(image);
                getItemDTO.setOrdered(i.getOrdered());
                return getItemDTO;
            }).collect(Collectors.toList());
        } else if (getItemsRequest.getCategory_id() != "") {
            log.info("find item by category_id");
            listItem = mongoRepo.getItemByCategory(getItemsRequest.getCategory_id());
        }
        else{
        }

        //get list average rating
        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(listItem);
        Map<ObjectId, Double> mapItemAverageRating = new HashMap<>();
        //set map
        for (AverageRating a : averageRatingList
        ) {
            mapItemAverageRating.put(a.getReferenceId(), a.getAverage_rating());
        }

        // get restaurant by list item

        List<ObjectId> listRestaurantId = listItem.stream().map(i->{
            return new ObjectId(i.getRestaurantId());
        }).collect(Collectors.toList());

        List<Restaurant> restaurantList = restaurantRepository.findAllBy_idIn(listRestaurantId);

        Map<String,Double> resId_Distance = new HashMap<>();

        Set<ObjectId> uniqueIds = new HashSet<>();
        List<Restaurant> uniqueRestaurants = restaurantList.stream()
                .filter(r -> uniqueIds.add(r.get_id()))
                .collect(Collectors.toList());

        System.out.println("=====================>");
        uniqueRestaurants.forEach(u->{
            System.out.println(u);
        });

        Map<String,Restaurant> resId_Res = new HashMap<>();
        uniqueRestaurants.forEach(r->{
            Double distance = DistanceCalculator.distance(r.getAddress_lat(),r.getAddress_long(),
                    getItemsRequest.getAddress_lat(),getItemsRequest.getAddress_long());
            resId_Distance.put(r.get_id().toString(),distance);
            resId_Res.put(r.get_id().toString(),r);

        });

        // set list GetItemDTO
        for (GetItemDTO i : listItem
        ) {

//            DecimalFormat df = new DecimalFormat("#.##");
//            String formatted = df.format(resId_Distance.get(i.getRestaurantId()));
            i.setResAddress(resId_Res.get(i.getRestaurantId()) == null? "Trần Thái Tông, Dịch Vọng Hậu, Cầu Giấy, Hà Nội, Việt Nam":resId_Res.get(i.getRestaurantId()).getAddress_detail());
            i.setRate_average(mapItemAverageRating.get(new ObjectId(i.get_id())));
//            i.setDistance(Double.parseDouble(formatted));
            list.add(i);
        }

        log.info("getItemsRequest: " + getItemsRequest);
        response.setCode(1);
        response.setData(list);

        return response;


    }

    public CommonResponse<Object> order(OrderRequest orderRequest) {
        CommonResponse<Object> response = new CommonResponse<>();


        mongoRepo.order(orderRequest);

        response.setCode(1);
        response.setMessage("order thanh cong");
        response.setData(orderRequest.getPhone());

        return response;

    }

    public CommonResponse<Object> getOrder(String restaurantId, String status) {

        CommonResponse<Object> response= new CommonResponse<>();

      List<GetOrderDTO> list =   mongoRepo.getOrder(restaurantId,status);

      response.setData(new Document().append("listOrder",list));
      response.setMessage("truy van thanh cong");
      response.setCode(1);

        return response;
    }

    public CommonResponse<Object> changeOrderStatus(OnOffItemRequest changeOrderStatusRequest) {

        CommonResponse<Object> response= new CommonResponse<>();

        mongoRepo.changeOrderStatus(changeOrderStatusRequest);

        response.setCode(1);
        response.setMessage("thanh cong");
        response.setData(changeOrderStatusRequest.get_id());

        return response;

    }

    public CommonResponse<Object> getRestaurantStat(ObjectId id) {

        CommonResponse<Object> response =  new CommonResponse<>();


        Document result = mongoRepo.getRestaurantStat(id);

        response.setData(new Document().append("stats",result));
        response.setMessage("thanh cong");
        response.setCode(1);
        return response;
    }

    public CommonResponse<Object> getItemDetail(String id) {
        CommonResponse<Object> response= new CommonResponse<>();

        ItemDetailDTO itemDetailDTO= new ItemDetailDTO();
        Optional<Item> item= itemRepository.findById(new ObjectId(id));

        if(item.isEmpty()){
            response.setCode(0);
            response.setMessage("khong tim duoc du lieu");
            return response;
        }
        Item itemDetail = item.get();
        Restaurant restaurant= restaurantRepository.findById(itemDetail.getRestaurantid()).get();
        List<String> categories = itemDetail.getCategories();

        List<GetItemDTO> relateItem =null;
if(itemDetail.getCategories() == null){
    relateItem = mongoRepo.getItemByCategory("BURGER");
}else
        for(int i=0;i<categories.size();i++){
            relateItem = mongoRepo.getItemByCategory(categories.get(0));
        }

        // relate item by category
        List<RelateItem> relateItemList = relateItem.stream().map(r->{
            RelateItem relateItem1 = new RelateItem();
            relateItem1.set_id(r.get_id());
            relateItem1.setName(r.getName());
            relateItem1.setPrice(r.getPrice());
            relateItem1.setImage(r.getImage_url());
            return relateItem1;
        }).collect(Collectors.toList());

        // relate item by restaurant
        Page<Item> relateItemByResPage = itemRepository.findByRestaurantid(itemDetail.getRestaurantid(),PageRequest.of(0,4));
        List<Item>relateItemByRes = relateItemByResPage.getContent();
        List<RelateItem> relateItemByRestaurant = relateItemByRes.stream().map((r->{
            RelateItem relateItem1 = new RelateItem();
            relateItem1.set_id(r.get_id().toString());
            relateItem1.setName(r.getName());
            relateItem1.setPrice(r.getPrice());
            String image= null;
            if (r.getImages() != null){
                if(r.getImages().size()> 0){
                    image = r.getImages().get(0);
                }
            }
            relateItem1.setImage(image);
            return relateItem1;
        })).collect(Collectors.toList());


        itemDetailDTO.set_id(itemDetail.get_id().toString());
        itemDetailDTO.setName(itemDetail.getName());
        itemDetailDTO.setPrice(itemDetail.getPrice());
        itemDetailDTO.setDetail(itemDetail.getDetail());
        String image= null;
        if (itemDetail.getImages() != null){
            if(itemDetail.getImages().size()> 0){
                image = itemDetail.getImages().get(0);
            }
        }
        itemDetailDTO.setImage(image);
        itemDetailDTO.setCategories(itemDetail.getCategories());

        RestaurantDTO restaurantDTO= new RestaurantDTO();
        restaurantDTO.setAddress(restaurant.getAddress_detail());
        restaurantDTO.set_id(restaurant.get_id().toString());
        restaurantDTO.setName(restaurant.getName());
        restaurantDTO.setPhone(restaurant.getPhone());
        restaurantDTO.setEmail(restaurant.getEmail());
        restaurantDTO.setUid(restaurant.getUid());
        itemDetailDTO.setRestaurant(restaurantDTO);
        itemDetailDTO.setRelateItemList(relateItemList.size() > 4? relateItemList.subList(0,4): relateItemList);
        itemDetailDTO.setRestaurantRelateItemList(relateItemByRestaurant);
        itemDetailDTO.setRating(mongoRepo.getAverageRating(itemDetail.get_id()));


        response.setData(itemDetailDTO);
        response.setCode(1);
        response.setMessage("thanh cong");

        return response;
    }

    public void addGoogleUid(ObjectId id, String uid) {

        mongoRepo.addGoogleUid(id,uid);
    }

    public CommonResponse<Object> userGetOrder(String phone,String status) {

        CommonResponse<Object> response= new CommonResponse<>();

        List<GetOrderDTO> list =   mongoRepo.userGetOrder(phone,status);

        response.setData(new Document().append("listOrder",list));
        response.setMessage("truy van thanh cong");
        response.setCode(1);


        return response;
    }

    public CommonResponse<Object> rate(String itemId, String orderItemId, double rattingValue) {

        CommonResponse<Object> response =  new CommonResponse<>();

        mongoRepo.rate(itemId,orderItemId,rattingValue);

        response.setMessage("thanh cong");
        response.setCode(1);

        return response;
    }

    public void dailyCalculateRating() {

        mongoRepo.dailyCalculateRating();
    }

    public CommonResponse<Object> getAllStat() {

        CommonResponse<Object> response= new CommonResponse<>();

        GetAllStatDTO getAllStatDTO = mongoRepo.getAllStat();
        response.setData(getAllStatDTO);
        response.setMessage("thanh cong");
        response.setCode(1);

        return response;
    }

    public CommonResponse<Object> getAllRes() {
        CommonResponse<Object> response= new CommonResponse<>();


        List<RestaurantDTO> restaurantDTOS = mongoRepo.getAllRes();
        response.setData(restaurantDTOS);
        response.setCode(1);
        response.setMessage("thanh cong");

        return  response;
    }

    public CommonResponse<Object> onoffRes(OnOffItemRequest onOffItemRequest) {

        CommonResponse<Object> response = new CommonResponse<>();

        List<String> status = Arrays.asList("ON", "OFF");
        if (!status.contains(onOffItemRequest.getStatus())) {
            response.setCode(0);
            response.setMessage("status phai la on hoc off");
            return response;
        }
        boolean check = mongoRepo.onoffRes(onOffItemRequest);
        if (check) {
            response.setCode(1);
            response.setMessage(onOffItemRequest.getStatus() + " thanh cong");
        }

        return response;

    }
}
