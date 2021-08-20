package pl.visualnet.omomo.utils;

import android.location.Address;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.domain.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationUtils {

    /**
     * Group locations
     *
     * @param locations
     * @param cityId
     * @return HashMap<String, Location>
     */
    public static HashMap<String, Location> groupAndMarkTheSame(List<Location> locations, int cityId) {

        HashMap<String, Location> groupped = new HashMap<String, Location>();

        for (Location location : locations) {

            int hashCode = location.getStreet().hashCode();

            String key = location.getTicketSystemId() + "" + cityId + "" + String.valueOf(hashCode);

            if (!groupped.containsKey(key)) {
                groupped.put(key, location);
            } else {
                int count = groupped.get(key).getCounter();
                groupped.get(key).setCounter(count + 1);
            }

            groupped.get(key).setGroupedIds(location.getId());

        }

        return groupped;
    }

    /**
     * Get coordinates for locations
     *
     * @param locations
     * @return List<Location>
     */
    public static List<Location> getLocationsCoordinates(City city, List<Location> locations) {

        List<Address> place;
        List<Location> locationsCoordinates = new ArrayList<Location>();
        HashMap<String, Location> grouped = LocationUtils.groupAndMarkTheSame(locations, city.getId());

        for (Map.Entry<String, Location> entry : grouped.entrySet()) {

            Location location = entry.getValue();

            try {

                if (!location.hasCoordinates()) {

                    // resolve problem 4.2 API 17 (Service not available)
                    // run geolocation engine manually
                    //LatLng coordinates = MapUtils.getCoordinatesFromLocationName(location.getStreet() + ", " + city.getName());
                    //location.setLatitude(coordinates.latitude);
                    //location.setLongitude(coordinates.longitude);

                }

                locationsCoordinates.add(location);

            } catch (Exception e) {
            }

        }

        return locationsCoordinates;
    }

}
