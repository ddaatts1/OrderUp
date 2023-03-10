package com.FoodOrdering.OrderUp.Service;

import com.FoodOrdering.OrderUp.Model.AverageRating;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.payload.request.AddItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.FindNearbyRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.GetItemDTO;
import com.FoodOrdering.OrderUp.Model.payload.request.EditItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.request.OnOffItemRequest;
import com.FoodOrdering.OrderUp.Model.payload.response.CommonResponse;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationService {

    @Autowired
    MongoRepo mongoRepo;
    @Autowired
    ItemRepository itemRepository;





    public List<GetItemDTO> getListItem(){
        List<GetItemDTO> list = new ArrayList<>();

        // get list item
        Page<Item> items = itemRepository.findAll(PageRequest.of(0,5));
        List<Item> listItem = items.getContent();

        // get list media
        List<Media> mediaList = mongoRepo.geListMediaByListItem(listItem);
        Map<ObjectId,String> mapItemMedia = new HashMap<>();
        // set map
        for (Media m:mediaList
             ) {
            if(m.getSeq() == 1){
                mapItemMedia.put(m.getReferenceId(),m.getMediaUrl());
            }
        }
        //get list average rating
        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(listItem);
        Map<ObjectId,Double> mapItemAverageRating = new HashMap<>();
        //set map
        for (AverageRating a:averageRatingList
             ) {
            mapItemAverageRating.put(a.getReferenceId(),a.getAverage_rating());
        }

        // set list GetItemDTO
        for (Item i:listItem
             ) {
            GetItemDTO getItemDTO = new GetItemDTO();
            getItemDTO.setItem(i);
            getItemDTO.setImage_url(mapItemMedia.get(i.get_id()));
            getItemDTO.setRate_average(mapItemAverageRating.get(i.get_id()));
            list.add(getItemDTO);
        }

        return list;
    }


    public CommonResponse<Object> addItem(AddItemRequest item) {
        CommonResponse<Object> insertResult  = new CommonResponse<>();

       boolean check = mongoRepo.addItem(item);
       if(check){
           insertResult.setCode(1);
           insertResult.setMessage("Thêm thành công");
       }
       else {
           insertResult.setCode(0);
           insertResult.setMessage("insert thất bại");
       }

        return insertResult;
    }

    public CommonResponse<Object> onnoffitem(OnOffItemRequest onOffItemRequest) {
        CommonResponse<Object> response = new CommonResponse<>();

        List<String> status = Arrays.asList("ON","OFF");
        if(!status.contains(onOffItemRequest.getStatus()))
        {
            response.setCode(0);
            response.setMessage("status phai la on hoc off");
            return response;
        }
        boolean check = mongoRepo.onoffitem(onOffItemRequest);
        if(check){
            response.setCode(1);
            response.setMessage(onOffItemRequest.getStatus()+" thanh cong");
        }

        return response;
    }

    public CommonResponse<Object> editItem(EditItemRequest item) {
        CommonResponse<Object> response = new CommonResponse<>();

        boolean check = mongoRepo.editItem(item);
        if(check){
            response.setCode(1);
            response.setMessage("Sửa thành công");
            return response;
        }else {
            response.setCode(0);
            response.setMessage("Sửa thất bại");
        }

        return response;
    }

    public CommonResponse<Object> findNearby(FindNearbyRequest findNearbyRequest) {
        CommonResponse<Object> response =new  CommonResponse<>();


        return response;
    }
}
