package pl.visualnet.omomo.utils;

import android.text.TextUtils;
import pl.visualnet.omomo.domain.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationCacheUtils {

    static List<Location> locations = new ArrayList<Location>();

    /**
     * Add location to cache
     *
     * @param location
     */
    public static void addLocationToCache(Location location, boolean isOnMap) {

        if (isLocationInCache(location) == false) {
            locations.add(location);
        } else {
            getLocationFromCache(location).setOnMap(true);
        }
    }

    /**
     * Remove state is-on-map from locations
     */
    public static void unMarkIsOnMapLocations() {

        for (Location location : locations) {
            location.setOnMap(false);
        }
    }

    /**
     * Get all locations
     *
     * @return List<Location>
     */
    public static List<Location> getLocationsFromCache() {
        return locations;
    }

    /**
     * Clean locations
     */
    public static void clean() {
        locations.clear();
    }

    /**
     * Check if location exists
     *
     * @param location
     * @return boolean
     */
    public static boolean isLocationInCache(Location location) {
        return (getLocationFromCache(location) != null) ? true : false;
    }

    /**
     * Get location
     *
     * @param location
     */
    public static Location getLocationFromCache(Location location) {

        Location foundLocation = null;

        for (Location cacheLocation : locations) {

            if (location.getLongitude() == cacheLocation.getLongitude()
                    && location.getLatitude() == cacheLocation.getLatitude()) {

                foundLocation = cacheLocation;
                break;
            }

        }

        return foundLocation;
    }

    public static String stringify() {

        return TextUtils.join(",", locations);

    }
}
