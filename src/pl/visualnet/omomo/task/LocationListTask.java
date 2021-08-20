package pl.visualnet.omomo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.adapter.LocationAdapter;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.domain.Location;
import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.reader.CityReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationListTask extends AsyncTask<City, Void, List<Location>> {

    App mApp;
    Geocoder mGeocoder;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private ListView mListView;
    private CityReader mReader;
    private City mSelectedCity;

    public LocationListTask(Context context, ProgressDialog progressDialog, ListView listView, App app) {
        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mGeocoder = new Geocoder(this.mContext);
        this.mListView = listView;
        this.mApp = app;
    }

    @Override
    protected List<Location> doInBackground(City... item) {

        try {

            City city = (City) item[0];
            this.mSelectedCity = city;

            mReader = new CityReader(new URL(App.API_HOST));

            // get specify locations in city
            return mReader.getCityLocations(city.getId());

        } catch (ReaderException tse) {
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

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
            mListView.setAdapter(null);

        } else {

            LocationAdapter adapter = new LocationAdapter(
                    this.mContext,
                    mProgressDialog,
                    mApp,
                    R.layout.listview_location_item_row,
                    locations
            );

            mListView.setAdapter(adapter);

        }

        locations = null;
        System.gc();
        mProgressDialog.dismiss();

    }

}