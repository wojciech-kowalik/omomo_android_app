package pl.visualnet.omomo.utils;

import android.location.Address;
import android.location.Geocoder;
import android.os.NetworkOnMainThreadException;
import com.google.android.gms.maps.model.LatLng;
import pl.visualnet.omomo.domain.City;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CityUtils {

    /**
     * Get coordinates for city
     *
     * @param city
     * @return LatLng
     */
    public static LatLng setCityCoordinates(City city, Geocoder geocoder) {

        LatLng coordinates = new LatLng(0, 0);
        List<Address> cities = new ArrayList<Address>();

        try {

            // check if city has your own coordinates
            if (city.hasCoordinates()) {
                coordinates = new LatLng(city.getLatitude(), city.getLongitude());
            } else {
                cities = geocoder.getFromLocationName(city.getName(), 1);
                coordinates = new LatLng(cities.get(0).getLatitude(), cities.get(0).getLongitude());
            }

        } catch (NetworkOnMainThreadException ne) {


            // resolve problem 4.2 API 17 (Service not available)
            // run geolocation engine manually
            //coordinates = MapUtils.getCoordinatesFromLocationName(city.getName());

        } catch (IOException e) {
        }

        return coordinates;

    }


}
