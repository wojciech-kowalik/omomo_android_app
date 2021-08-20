package pl.visualnet.omomo.ui.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.task.LocationListTask;
import pl.visualnet.omomo.utils.CityParcel;

public class LocationListActivity extends ListActivity {

    public static final String TAG_LOCATION = "tag_location";
    ProgressDialog mProgressDialog;
    App mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        mApp = (App) getApplication();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.omomo_location_retrieving_data));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        Bundle data = getIntent().getExtras();

        CityParcel cityParcel = data.getParcelable(TAG_LOCATION);

        TextView locationHeader = (TextView) findViewById(R.id.list_header_location);
        locationHeader.setText(cityParcel.getName());

        City city = new City();
        city.setId(Integer.parseInt(cityParcel.getId()));
        city.setName(city.getName());
        city.setLatitude(0);
        city.setLongitude(0);

        new LocationListTask(this, mProgressDialog, getListView(), mApp).execute(city);

    }


}
