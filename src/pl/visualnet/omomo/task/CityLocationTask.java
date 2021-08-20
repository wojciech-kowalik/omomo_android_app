package pl.visualnet.omomo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.domain.Location;
import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.reader.CityReader;
import pl.visualnet.omomo.utils.CityUtils;
import pl.visualnet.omomo.utils.LocationCacheUtils;
import pl.visualnet.omomo.utils.LocationUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CityLocationTask extends AsyncTask<City, Void, List<Location>> {

    public static final int CITY_ZOOM = 9;
    ProgressDialog mProgressDialog;
    LatLng mCityPosition;
    Geocoder mGeocoder;
    App mApp;
    CityReader mReader;
    ImageView mImageView;
    private Context mContext;
    private City mSelectedCity;

    public CityLocationTask(Context context, ProgressDialog progressDialog, ImageView imageView, App app) {

        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mGeocoder = new Geocoder(this.mContext);
        this.mApp = app;
        this.mImageView = imageView;

    }

    @Override
    protected List<Location> doInBackground(City... item) {

        try {

            City city = (City) item[0];
            this.mSelectedCity = city;

            // get city mCityPosition
            mCityPosition = CityUtils.setCityCoordinates(this.mSelectedCity, mGeocoder);

            mReader = new CityReader(new URL(App.API_HOST));

            // get specify locations in city
            return LocationUtils.getLocationsCoordinates(this.mSelectedCity, mReader.getCityLocations(city.getId()));

        } catch (ReaderException tse) {
        } catch (Exception e) {
            mProgressDialog.dismiss();
            return null;
        }

        mProgressDialog.dismiss();

        return null;

    }

    @Override
    protected void onPreExecute() {

        mSelectedCity = null;
        super.onPreExecute();
        mProgressDialog.show();

    }

    @Override
    protected void onPostExecute(List<Location> locations) {

        if (locations == null) {
            locations = new ArrayList<Location>();
        }

        if (locations.isEmpty()) {
            Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();
            mProgressDialog.dismiss();
            return;
        }

        boolean isOnMap = false;

        LocationCacheUtils.unMarkIsOnMapLocations();

        if (App.isActualPositionMode()) {
            isOnMap = true;
        }

        for (Location location : locations) {
            LocationCacheUtils.addLocationToCache(location, isOnMap);
        }

        //new MapImageTask(
        //        mContext,
        //        mImageView,
        //        new LatLng(mSelectedCity.getLatitude(), mSelectedCity.getLongitude()),
        //        LocationCacheUtils.getLocationsFromCache(),
        //        ActualPositionLocationTask.ACTUAL_POSITION_ZOOM
        //).execute();

        mProgressDialog.dismiss();
        System.gc();

    }


}
