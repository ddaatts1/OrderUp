package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Email.CreateOTP;
import com.FoodOrdering.OrderUp.Email.EmailSenderService;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Restaurant;
import com.FoodOrdering.OrderUp.Model.payload.request.*;
import com.FoodOrdering.OrderUp.Model.payload.response.GetItemDTO;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MediaRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.FoodOrdering.OrderUp.Repository.RestaurantRepository;
import com.FoodOrdering.OrderUp.Service.ApplicationService;

import com.FoodOrdering.OrderUp.config.JwtService;
import com.mongodb.client.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/OrderUp")
@CrossOrigin(origins = "http://localhost:3000")

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
    EmailSenderService emailSenderService;
    @Autowired
    MongoRepo mongoRepo;
    @Autowired
    JwtService jwtService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @GetMapping("/test")
    public void test() throws MessagingException {

        String otp = new String(CreateOTP.OTP(4));
        emailSenderService.sendSimpleEmail("dodat2632001@gmail.com",otp);

    }


    @PostMapping("/createOTP")
    public String createOTP(@RequestParam String email ) {

        String otp = new String(CreateOTP.OTP(4));

        try{
            emailSenderService.sendSimpleEmail(email,otp);

        }catch (MessagingException e){

        }
        return otp;

    }





    @GetMapping("/GET_ITEMS")
    public  List<GetItemDTO> getlist(@RequestBody GetItemsRequest getItemsRequest){

        List<GetItemDTO> items = applicationService.getListItem(getItemsRequest);

        return  items;
    }

    @GetMapping("/FIND_NEARBY")
    public CommonResponse<Object> FIND_NEARBY (@RequestBody FindNearbyRequest findNearbyRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.findNearby(findNearbyRequest);

        return  response;
    }





}
