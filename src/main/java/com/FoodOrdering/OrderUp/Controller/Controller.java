package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Email.CreateOTP;
import com.FoodOrdering.OrderUp.Email.EmailSenderService;
import com.FoodOrdering.OrderUp.Model.Category;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Restaurant;
import com.FoodOrdering.OrderUp.Model.payload.request.*;
import com.FoodOrdering.OrderUp.Model.payload.response.GetItemDTO;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Repository.*;
import com.FoodOrdering.OrderUp.Service.ApplicationService;

import com.FoodOrdering.OrderUp.config.JwtService;
import com.mongodb.client.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    CategoryRepository categoryRepository;

    @Autowired
    RestaurantRepository restaurantRepository;
    Logger log = LoggerFactory.getLogger(Controller.class);

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


//    @GetMapping("/GET_ITEMS")
//    public  List<GetItemDTO> getlist(@RequestBody GetItemsRequest getItemsRequest){
//
//        List<GetItemDTO> items = applicationService.getListItem(getItemsRequest);
//
//        return  items;
//    }


    @PostMapping("/USER_GET_ITEMS")
    public CommonResponse<Object> USER_GET_ITEMS(@RequestBody GetItemsRequest getItemsRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.userGetItems(getItemsRequest);
        return response;
    }


    @PostMapping("/ORDER")
    public CommonResponse<Object> ORDER(@RequestBody OrderRequest orderRequest){
        CommonResponse<Object> response= new CommonResponse<>();
        response = applicationService.order(orderRequest);

        return  response;
    }

    @GetMapping("/FIND_NEARBY")
    public CommonResponse<Object> FIND_NEARBY (@RequestBody FindNearbyRequest findNearbyRequest){
        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.findNearby(findNearbyRequest);

        return  response;
    }



    @GetMapping("/GET_ALL_CATEGORIES")
    public CommonResponse<Object> GET_ALL_CATEGORIES(){
        CommonResponse<Object> response =  new CommonResponse<>();


        List<Category> category = categoryRepository.findAll();

        response.setData(category);
        response.setCode(1);
        response.setMessage("truy van du lieu thanh cong ");
        return response;
    }


    @GetMapping("/GET_ITEM_DETAIL")
    public CommonResponse<Object> GET_ITEM_DETAIL(@RequestParam(name = "id") String id){

        CommonResponse<Object> response = new CommonResponse<>();

        response = applicationService.getItemDetail(id);

        return response;
    }




}
