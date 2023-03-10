package com.FoodOrdering.OrderUp.Controller;

import com.FoodOrdering.OrderUp.Service.GoogleApiService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeocodeController {

    @Autowired
    GoogleApiService googleApiService;

    @GetMapping("/geocode")
    public ResponseEntity<GeocodingResult[]> geocode(@RequestParam String address) {
        try {
            GeocodingResult[] results = googleApiService.addressToCoordinate(address);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/distance")
    public DistanceMatrix getDistanceMatrix(@RequestParam Double originlong
            , @RequestParam Double originlat
            , @RequestParam Double deslong, @RequestParam Double deslat) throws Exception {

        // Execute the request
        DistanceMatrix distanceMatrix = googleApiService.getDistanceMatrix(originlong, originlat, deslong, deslat);

//        String distance = distanceMatrix.rows[0].elements[0].distance.humanReadable;
        return distanceMatrix;
    }
}
