package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.payload.GetItemDTO;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MediaRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.FoodOrdering.OrderUp.Service.ApplicationService;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/OrderUp")
public class Controller {

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


    @GetMapping("/getItem")
    public Page<Item> getItem(@RequestParam(name = "page", required = true) int page){
        return itemRepository.findAllWithMediaAndAverageRating(PageRequest.of(page,2));
    }


    @GetMapping("/getlist")
    public  List<GetItemDTO> getlist(){

        List<GetItemDTO> items = applicationService.getListItem();

        return  items;
    }


}
