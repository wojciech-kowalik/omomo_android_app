package pl.visualnet.omomo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.util.DisplayMetrics;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.Location;

import java.util.HashMap;
import java.util.List;

public class MapUtils {

    public static final int RANDOM_INITIAL = 2000;

    public static void directions(Activity activity, LocationParcel location) {

        if (ActualPositionUtils.setActualPositionCoordinates(activity)) {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?" +
                            "daddr=" + location.getLatitude() + ", " + location.getLongitude() + "&" +
                            "saddr=" + App.sActualPosition.latitude + ", " + App.sActualPosition.longitude + ""));

            activity.startActivity(intent);

        }

    }

    /**
     * Get DPI value for height of map fragment
     *
     * @param size
     * @param metrics
     * @return int
     */
    public static int getDPI(int size, DisplayMetrics metrics) {
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    /**
     * Alternative solution for getting coordinates from location name
     *
     * @param address
     * @return LatLng
     */
//    public static LatLng getCoordinatesFromLocationName(String address) {
//
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme("http")
//                .authority("maps.google.com")
//                .appendPath("maps")
//                .appendPath("api")
//                .appendPath("geocode")
//                .appendPath("json")
//                .appendQueryParameter("address", address)
//                .appendQueryParameter("sensor", "false");
//
//        String url = builder.build().toString();
//
//        HttpGet httpGet = new HttpGet(url);
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//        double lat = 0;
//        double lon = 0;
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//
//        } catch (IOException e) {
//        }
//
//        JSONObject jsonObject;
//
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//
//            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//
//            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location").getDouble("lat");
//
//        } catch (JSONException e) {
//        }
//
//        return new LatLng(lat, lon);
//    }

    /**
     * Get location by marker
     *
     * @param marker
     * @param locationMarkers
     * @return Location
     */
    public static Location getLocationByMarker(Marker marker, HashMap<Marker, Location> locationMarkers) {

        Location found = new Location();

        for (Marker locationMarker : locationMarkers.keySet()) {

            if (marker.equals(locationMarker)) {
                found = locationMarkers.get(locationMarker);
            }
        }

        return found;
    }


    /**
     * Add markers to dictionary and map
     *
     * @param context
     * @param map
     * @param locations
     * @param showWindow
     */
    public static void addLocationMarkers(Context context, GoogleMap map, List<Location> locations, boolean showWindow) {

        for (Location location : locations) {

            String name = (location.getCounter() > 0) ? location.getTicketSystemName() : location.getName();

            // set changed name
            location.setName(name);

            Marker marker = MarkerCacheUtils.getMarkerFromCache(location);

            if (marker != null) {

                try {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                } catch (IllegalArgumentException e) {
                }

            } else {

                marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude()))
                        .title(location.getName())
                        .snippet(context.getString(R.string.omomo_show_repertoire))
                        .visible(true));

            }

            // show window above marker
            if (showWindow) {
                marker.showInfoWindow();
            }

            MarkerCacheUtils.addMarkerToCache(marker, location);
            System.gc();
        }

    }

    /**
     * Get actual client's coordinates
     *
     * @param context
     * @return LatLng
     */
    public static LatLng getMyCoordinates(final Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        android.location.Location location = null;

        android.location.Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        android.location.Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (locationNet != null) {
            location = locationNet;
        } else {
            location = locationGPS;
        }

        return (location != null)
                ? new LatLng(location.getLatitude(), location.getLongitude())
                : null;

    }

    /**
     * Get city zoom by distance
     *
     * @param distance
     * @return
     */
    public static int getZoom(int distance) {

        int zoom = 0;

        if (distance >= 10 && distance <= 50) {
            zoom = 9;
        }

        if (distance >= 60 && distance < 100) {
            zoom = 8;
        }

        if (distance == 90 || distance == 100) {
            zoom = 7;
        }

        if (distance >= 200) {
            zoom = 5;
        }

        return zoom;
    }

}
