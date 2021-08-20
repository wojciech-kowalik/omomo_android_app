package pl.visualnet.omomo.utils;

import com.google.android.gms.maps.model.Marker;
import pl.visualnet.omomo.domain.Location;

import java.util.HashMap;
import java.util.Map;

public class MarkerCacheUtils {

    static HashMap<Marker, Location> mapMarkers = new HashMap<Marker, Location>();

    /**
     * Add marker to cache
     *
     * @param marker
     * @param location
     */
    public static void addMarkerToCache(Marker marker, Location location) {
        mapMarkers.put(marker, location);
    }

    /**
     * Get all markers
     *
     * @return HashMap<Marker, Location>
     */
    public static HashMap<Marker, Location> getMarkersFromCache() {
        return mapMarkers;
    }

    /**
     * Clean markers
     */
    public static void cleanMarkersCache() {
        mapMarkers.clear();
    }

    /**
     * Check if marker exists
     *
     * @param location
     * @return boolean
     */
    public static boolean isMarkerInCache(Location location) {
        return (getMarkerFromCache(location) != null) ? true : false;
    }

    /**
     * Get marker
     *
     * @param location
     * @return Marker
     */
    public static Marker getMarkerFromCache(Location location) {

        Marker marker = null;

        for (Map.Entry<Marker, Location> entry : mapMarkers.entrySet()) {

            Marker cacheMarker = entry.getKey();
            Location cacheLocation = entry.getValue();

            if (location.getLongitude() == cacheLocation.getLongitude()
                    && location.getLatitude() == cacheLocation.getLatitude()) {

                marker = cacheMarker;
            }

        }

        return marker;
    }
}
