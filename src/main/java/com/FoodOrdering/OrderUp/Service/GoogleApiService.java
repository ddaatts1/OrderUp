package com.FoodOrdering.OrderUp.Service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
public class GoogleApiService {

    private final GeoApiContext context;

    public GoogleApiService(@Value("${google.maps.api.key}") String apiKey) {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }


    public GeocodingResult[] addressToCoordinate(String address) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        return results;
    }

    public DistanceMatrix getDistanceMatrix( Double originlong, Double originlat, Double deslong, Double deslat) throws Exception {

        // Define origin and destination addresses
        String[] origins = new String[] {originlong+","+originlat};
        String[] destinations = new String[] { deslong+","+deslat};

        // Build the DistanceMatrix request
        DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
                .origins(origins)
                .destinations(destinations)
                .mode(TravelMode.DRIVING)
                .units(Unit.METRIC);


        // Execute the request
        DistanceMatrix distanceMatrix = request.await();

//        String distance = distanceMatrix.rows[0].elements[0].distance.humanReadable;
        return distanceMatrix;
    }



}
