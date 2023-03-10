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


public class DistanceCalculator {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Earth's radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        return distance;
    }

    public static void main(String[] args) {
        double lat1 = 21.014620;
        double lon1 = 105.782072;

        double lat2 = 21.032344;
        double lon2 = 105.787093;

        double distance = distance(lat1, lon1, lat2, lon2);

        System.out.println("Distance between the two locations is " + distance + " km");
    }
}
