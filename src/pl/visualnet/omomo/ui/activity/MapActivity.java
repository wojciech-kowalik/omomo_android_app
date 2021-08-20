package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.utils.LocationParcel;
import pl.visualnet.omomo.utils.MapUtils;

public class MapActivity extends Activity implements OnMapReadyCallback {

    public static final String TAG_SETTINGS = "tag_settings";
    public static final String TAG_LOCATION = "tag_location";
    public static final int ZOOM = 15;
    LocationParcel mLocationParcel;
    LatLng mLocationCoordinates;
    Activity mActivity;
    MapFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        mActivity = this;

        //TextView institutionName = (TextView) findViewById(R.id.map_view_institution_name);
        //TextView placeName = (TextView) findViewById(R.id.map_view_place_name);
        Button buttonDirection = (Button) findViewById(R.id.button_direction);

        Bundle data = getIntent().getExtras();
        mLocationParcel = data.getParcelable(TAG_LOCATION);

        mLocationCoordinates = new LatLng(mLocationParcel.getLatitude(), mLocationParcel.getLongitude());

        //if (mLocationParcel.getInstitutionName().isEmpty()) {
        //    institutionName.setVisibility(View.GONE);
        //} else {
        //    institutionName.setText(mLocationParcel.getInstitutionName());
        //}

        //placeName.setText(mLocationParcel.getStreet());

        buttonDirection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MapUtils.directions(mActivity, mLocationParcel);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.addMarker(new MarkerOptions()
                .position(mLocationCoordinates)
                .title(mLocationParcel.getInstitutionName())
                .snippet(mLocationParcel.getStreet())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .visible(true)).showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocationCoordinates, ZOOM));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
            }

        });

    }
}
