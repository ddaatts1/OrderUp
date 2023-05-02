package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Email.EmailSenderService;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Restaurant;
import com.FoodOrdering.OrderUp.Model.payload.request.AddItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.EditItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OnOffItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Model.payload.response.RestaurantDTO;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MediaRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.FoodOrdering.OrderUp.Repository.RestaurantRepository;
import com.FoodOrdering.OrderUp.Service.ApplicationService;
import com.FoodOrdering.OrderUp.Service.DailyReportService;
import com.FoodOrdering.OrderUp.config.JwtService;
import com.mongodb.client.MongoClient;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/OrderUp")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
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
    DailyReportService dailyReportService;

    @Autowired
    RestaurantRepository restaurantRepository;

    Logger log = LoggerFactory.getLogger(AdminController.class);


    @PostMapping("/ADD_ITEM")
    public CommonResponse<Object> addItem(@RequestHeader Map<String,String> header, @RequestBody(required = true) AddItemRequest item){
        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);
        item.setRestaurant_id(restaurant.get_id().toString());
        log.info("====================> add item request :"+ item);
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

    @GetMapping("/GET_ITEMSS")
    public CommonResponse<Object> GET_ITEMSS( @RequestParam(name ="page") int page) {
        CommonResponse<Object> response = new CommonResponse<>();

        Page<Document> items =  applicationService.adminGetListItems("64055a970f600942fda69f0b",page,3);

        response.setData(items);
        return response;
    }

    @GetMapping("/GET_RESTAURANT_INF")
    public CommonResponse<Object> GET_RESTAURANT_INF(HttpServletRequest request){
        CommonResponse<Object> response = new CommonResponse<>();


        String authorizationHeader = request.getHeader("Authorization");
        String[] parts = authorizationHeader.split(" ");
        String token = parts[1];
        String name =jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findByEmail(name).get();

        Document rs= new Document().append("name",restaurant.getName())
                .append("email",restaurant.getEmail())
                .append("address_detail",restaurant.getAddress_detail())
                .append("address_long",restaurant.getAddress_long())
                .append("address_lat",restaurant.getAddress_lat())
                .append("phone",restaurant.getPhone());

        response.setData(rs);
        response.setMessage("Lấy thông tin thành công");
        return response;
    }

    @GetMapping("/test")
            public String test(){
        return "do tie n dat";
            }


    @GetMapping("/GET_ITEMS")
    public CommonResponse<Object> GET_ITEMS(@RequestHeader Map<String,String> header, @RequestParam(name ="page") int page) {
        CommonResponse<Object> response = new CommonResponse<>();
        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);
        response.setData(restaurant.get_id().toString());

       Page<Document> items =  applicationService.adminGetListItems(restaurant.get_id().toString(),page,3);

       response.setData(items);


        return response;
    }




    @PostMapping("/DELETE_ITEM")
    public CommonResponse<Object> DELETE_ITEM(@RequestParam(name = "id") String id){
        CommonResponse<Object> response = new CommonResponse<>();

        CommonResponse<Object> delete = applicationService.deleteItem( id);

        return delete;
    }


    @GetMapping("/GET_ITEM")
    public CommonResponse<Object> GET_ITEM (@RequestParam(name = "id" )String id){
        CommonResponse<Object> response = new CommonResponse<>();


        Optional<Item> item = itemRepository.findById(new ObjectId(id));


        if(item.isPresent()){
            response.setCode(1);
            Item  i = item.get();
            String image = null  ;

            if(i.getImages() != null){
                if(i.getImages().size()>=1){
                    image = i.getImages().get(0);
                }
            }

            Document document = new Document().append("_id",i.get_id().toString()).
                    append("name",i.getName()).append("detail",i.getDetail()).append("categories",i.getCategories()).
                    append("price",i.getPrice()).append("image",image);
            response.setData(document);
            response.setMessage("truy vấn dữ liệu thành công ");
        }
        else {
            response.setCode(0);
            response.setData(null);
            response.setMessage("truy vấn dữ liệu khong thành công ");
        }


        return response;
    }

    @GetMapping("/GET_ORDER")
    public CommonResponse<Object> GET_ORDER(@RequestHeader Map<String,String> header,@RequestParam(name = "status") String status){
        CommonResponse<Object> response= new CommonResponse<>();
        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);

        response = applicationService.getOrder(restaurant.get_id().toString(), status);

        return response;
    }


    @PostMapping("/CHANGE_ORDER_STATUS")
    public CommonResponse<Object> CHANGE_ORDER_STATUS(@RequestBody OnOffItemRequest changeOrderStatusRequest){

        CommonResponse<Object> response = new CommonResponse<>();


        response = applicationService.changeOrderStatus(changeOrderStatusRequest);

        return  response;
    }

    @GetMapping("/GET_RESTAURANT_STAT")
    public CommonResponse<Object> GET_RESTAURANT_STAT (@RequestHeader Map<String,String> header){
        CommonResponse<Object> response =  new CommonResponse<>();

        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);

         response  = applicationService.getRestaurantStat(restaurant.get_id());


        return response;
    }

    @GetMapping("/getJwt")
    public String getJwt(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String[] parts = authorizationHeader.split(" ");
        String token = parts[1];
        String name =jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findByEmail(name).get();
        return restaurant.toString();
    }

    @PostMapping("/ADD_GOOGLE_UID")
    public CommonResponse<Object>ADD_GOOGLE_UID(@RequestHeader Map<String,String> header, @RequestParam(name = "uid") String uid,@RequestParam(name = "email") String email){
        CommonResponse<Object> response= new CommonResponse<>();

        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);

        if(!restaurant.getEmail().equalsIgnoreCase(email)){
            response.setMessage("email khong dung");
            response.setCode(0);
            return response;
        }

        applicationService.addGoogleUid(restaurant.get_id(),uid);

        response.setCode(1);
        response.setMessage("thanh cong");
        response.setData(restaurant.get_id().toString());
        return response;
    }


    @GetMapping("/GET_RESTAURANT_BY_GOOGLE_UID")
    public CommonResponse<Object> GET_RESTAURANT_BY_GOOGLE_UID(@RequestParam(name = "uid") String uid){
        CommonResponse<Object> response= new CommonResponse<>();

        Optional<Restaurant>opRes =  restaurantRepository.findByUid(uid);
        if(opRes.isPresent()){
            Restaurant restaurant = opRes.get();
            response.setData(restaurant.getName());
            response.setCode(1);
            response.setMessage("thanh cong");
            return response;
        }else {
            response.setCode(0);
            response.setMessage("khong thanh cong");
        }

        return response;
    }


    public Restaurant getInforFromJWT(String jwt){
        System.out.println("===============>"+ jwt);
        String[] parts = jwt.split(" ");
        String token = parts[1];
        String name =jwtService.extractUsername(token);
        Restaurant restaurant = restaurantRepository.findByEmail(name).get();

        return restaurant;
    }


    @GetMapping("/GET_REPORT")
    public CommonResponse<Object> GET_REPORT(@RequestHeader Map<String,String> header){
        CommonResponse<Object> response = new CommonResponse<>();

        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);

        try {
            dailyReportService.transactionByDay(restaurant.get_id(), restaurant.getEmail());
            response.setCode(1);
            response.setData("Thống kê được gử vào email: "+ restaurant.getEmail());
            return response;
        } catch (InvalidFormatException e) {
            e.printStackTrace();

            response.setCode(0);
            response.setData("that bai");
            return response;
        }

    }


    @GetMapping("/GET_ALL_STAT")
    public CommonResponse<Object> GET_ALL_STAT (@RequestHeader Map<String,String> header){
        CommonResponse<Object> response =  new CommonResponse<>();

        String jwt = header.get("authorization");
        log.info("jwt: "+jwt);
        Restaurant restaurant = getInforFromJWT(jwt);
        log.info("request from : "+ restaurant);

        response  = applicationService.getAllStat();


        return response;
    }

    @GetMapping("/GET_ALL_RES")
    public CommonResponse<Object> GET_ALL_RES(@RequestParam("page") int page){
        CommonResponse<Object> response= new CommonResponse<>();

response.setCode(1);
response.setMessage("thanh cong");
Page<Restaurant> page1 = restaurantRepository.findAllByRole("MANAGER", PageRequest.of(page,5));
        List<RestaurantDTO> list = page1.getContent().stream().map((r)->{

            return    new RestaurantDTO(r.get_id().toString(),r.getName(),r.getPhone(),r.getEmail(),r.getAddress_detail(),r.isStatus());
        }).collect(Collectors.toList());

        Page<RestaurantDTO> resPage = new PageImpl<>(list,page1.getPageable(),list.size());
        response.setData(resPage);
        return response;
    }

    @PostMapping("/ON_OFF_RES_STATUS")
    public  CommonResponse<Object> ON_OFF_RES_STATUS(@RequestBody OnOffItemRequest onOffItemRequest){
        CommonResponse<Object> commonResponse = new CommonResponse<>();

        commonResponse = applicationService.onoffRes(onOffItemRequest);

        return commonResponse;
    }



}
