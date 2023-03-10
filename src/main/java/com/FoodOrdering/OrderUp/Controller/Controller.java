package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.payload.request.AddItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.FindNearbyRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.GetItemDTO;
import com.FoodOrdering.OrderUp.Model.payload.request.EditItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OnOffItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MediaRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.FoodOrdering.OrderUp.Service.ApplicationService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import com.mongodb.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/OrderUp")
public class Controller {

    @Value("${google.maps.api.key}")
    String key;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    MongoRepo mongoRepo;
    @GetMapping("/test")
    public void test(){

        List<Item> listItem = itemRepository.findAll();

    }


    @PostMapping("/ADD_ITEM")
    public CommonResponse<Object> addItem(@RequestBody(required = true) AddItemRequest item){
        CommonResponse<Object> check = applicationService.addItem(item);
        return check;
    }


    @PostMapping("/ON_OFF_ITEM")
    public CommonResponse<Object> OnOffItem(@RequestBody OnOffItemRequest onOffItemRequest){
        CommonResponse<Object> commonResponse = new CommonResponse<>();

        commonResponse = applicationService.onnoffitem(onOffItemRequest);

        return commonResponse;
    }

    @PostMapping("/EDIT_ITEM")
    public CommonResponse<Object> EDIT_ITEM(@RequestBody EditItemRequest item){
        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.editItem(item);

        return response;
    }


    @GetMapping("/GET_ITEMS")
    public  List<GetItemDTO> getlist(){

        List<GetItemDTO> items = applicationService.getListItem();

        return  items;
    }

    @GetMapping("/FIND_NEARBY")
    public CommonResponse<Object> FIND_NEARBY (@RequestBody FindNearbyRequest findNearbyRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.findNearby(findNearbyRequest);

        return  response;

    }



}
