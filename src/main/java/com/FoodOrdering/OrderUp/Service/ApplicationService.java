package com.FoodOrdering.OrderUp.Service;

import com.FoodOrdering.OrderUp.Model.AverageRating;
import com.FoodOrdering.OrderUp.Model.Item;
import com.FoodOrdering.OrderUp.Model.Media;
import com.FoodOrdering.OrderUp.Model.payload.GetItemDTO;
import com.FoodOrdering.OrderUp.Repository.ItemRepository;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    @Autowired
    MongoRepo mongoRepo;
    @Autowired
    ItemRepository itemRepository;



    public List<GetItemDTO> getListItem(){
        List<GetItemDTO> list = new ArrayList<>();

        List<Item> itemList = new ArrayList<>();

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
                mapItemMedia.put(m.get_id(),m.getMediaUrl());
            }
        }
        //get list average rating
        List<AverageRating> averageRatingList = mongoRepo.getListAverageRatingByListItem(listItem);
        Map<ObjectId,Double> mapItemAverageRating = new HashMap<>();
        //set map
        for (AverageRating a:averageRatingList
             ) {
            mapItemAverageRating.put(a.get_id(),a.getAverage_rating());
        }

        // set list GetItemDTO
        for (Item i:itemList
             ) {
            GetItemDTO getItemDTO = new GetItemDTO();
            getItemDTO.setItem(i);
            getItemDTO.setImage_url(mapItemMedia.get(i.get_id()));
            getItemDTO.setRate_average(mapItemAverageRating.get(i.get_id()));
            list.add(getItemDTO);
        }

        return list;
    }




}
