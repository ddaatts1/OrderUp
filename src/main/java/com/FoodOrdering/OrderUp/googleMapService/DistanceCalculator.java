package com.FoodOrdering.OrderUp.googleMapService;

//import com.google.maps.DistanceMatrixApi;
//import com.google.maps.GeoApiContext;
//import com.google.maps.model.DistanceMatrix;
//import com.google.maps.model.DistanceMatrixElement;
//import com.google.maps.model.DistanceMatrixRow;
//import com.google.maps.model.TravelMode;
//
//public class DistanceCalculator {
//
//    public static double getDistance(double originLat, double originLng, double destinationLat, double destinationLng) throws Exception {
//        // Set up the GeoApiContext
//        String apiKey = "YOUR_API_KEY";
//        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
//
//        // Make a request to the Distance Matrix API
//        DistanceMatrix result = DistanceMatrixApi.newRequest(context)
//                .origins(new com.google.maps.model.LatLng(originLat, originLng))
//                .destinations(new com.google.maps.model.LatLng(destinationLat, destinationLng))
//                .mode(TravelMode.DRIVING)
//                .await();
//
//        // Parse the result and return the distance in meters
//        DistanceMatrixRow[] rows = result.rows;
//        DistanceMatrixElement[] elements = rows[0].elements;
//        return elements[0].distance.inMeters;
//    }
//
//    public static void main(String[] args) {
//        double originLat = 21.013910;
//        double originLng = 105.784561;
//        double destinationLat = 21.016765;
//        double destinationLng = 105.781961;
//
//        try {
//            double distance = getDistance(originLat, originLng, destinationLat, destinationLng);
//            System.out.println("Distance between the two locations is " + distance + " meters");
//        } catch (Exception e) {
//            System.out.println("An error occurred: " + e.getMessage());
//        }
//    }
//}


import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

import java.io.IOException;

public class DistanceCalculator {
    public static final double R = 6372.8;

    public static double distance(double lat1, double lon1, double lat2, double lon2)  {
//        GeoApiContext context = new GeoApiContext.Builder()
//                .apiKey("AIzaSyC1QyAzxN2d5A-i0XscLboZjrW6p0EmmoE")
//                .build();
//
//        DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(context)
//                .origins(new String[]{lat1+","+lon1})
//                .destinations(new String[]{lat2+","+lon2})
//                .mode(TravelMode.DRIVING);
//
//        DistanceMatrix matrix = null;
//        try{
//             matrix = request.await();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        System.out.println(matrix.rows[0].elements[0].distance);

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;

    }

    public static void main(String[] args) {
        double lat1 = 21.014620;
        double lon1 = 105.782072;

        double lat2 =  21.034109;
        double lon2 = 105.780162;

        double distance = distance(lat1, lon1, lat2, lon2);

        System.out.println("Distance between the two locations is " + distance + " km");
    }
}
