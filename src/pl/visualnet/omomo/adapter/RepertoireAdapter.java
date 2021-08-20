package pl.visualnet.omomo.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.ui.activity.EventActivity;
import pl.visualnet.omomo.ui.activity.MainListActivity;
import pl.visualnet.omomo.ui.activity.MapActivity;
import pl.visualnet.omomo.ui.activity.WebActivity;
import pl.visualnet.omomo.utils.AnimateFirstDisplayListener;
import pl.visualnet.omomo.utils.LocationParcel;
import pl.visualnet.omomo.utils.MapUtils;
import pl.visualnet.omomo.utils.SettingsParcel;

import java.io.Serializable;
import java.util.List;

public class RepertoireAdapter extends ArrayAdapter<Repertoire> {

    public static final String FORMAT_KM = "%s km";
    public static final String FORMAT_WEB_ACTIVITY_URL_STRING = "%s%s%d%s";
    App mApp;
    Context mContext;
    int mLayoutResourceId;
    List<Repertoire> mData = null;
    ProgressDialog mProgressDialog;
    LayoutInflater mLayoutInflater;
    ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();

    public RepertoireAdapter(Context context, ProgressDialog progressDialog, App app, int layoutResourceId, List<Repertoire> data) {

        super(context, layoutResourceId, data);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = context;
        this.mData = data;
        this.mProgressDialog = progressDialog;
        this.mApp = app;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        RepertoireHolder holder = null;
        String imageUrl;

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(mLayoutResourceId, parent, false);

            holder = new RepertoireHolder();
            holder.eventTitle = (TextView) convertView.findViewById(R.id.event_name);
            holder.eventPlaceDistance = (TextView) convertView.findViewById(R.id.event_place_distance);
            holder.containerEventPlaceDistance = (RelativeLayout) convertView.findViewById(R.id.distance_container);
            holder.buttonShowWeb = (Button) convertView.findViewById(R.id.show_web);

            holder.eventDate = (Button) convertView.findViewById(R.id.event_date);
            holder.categoryName = (Button) convertView.findViewById(R.id.category);
            holder.placeInstitutionName = (Button) convertView.findViewById(R.id.place_institution_name);

            holder.eventImage = (ImageView) convertView.findViewById(R.id.event_image);

            ImageButton showPlaces = (ImageButton) convertView.findViewById(R.id.show_places);
            showPlaces.setVisibility(View.VISIBLE);
            holder.buttonShowPlaces = showPlaces;

            convertView.setTag(holder);

        } else {
            holder = (RepertoireHolder) convertView.getTag();
        }

        if (mData.get(position).getSystemImageUrl() == null) {
            imageUrl = App.API_HOST + "/" + mData.get(position).getCategoryImage();
        } else {
            imageUrl = mContext.getString(R.string.omomo_http) + mData.get(position).getSystemUrl() + mData.get(position).getSystemImageUrl();
        }

        ImageLoader.getInstance().displayImage(
                imageUrl,
                holder.eventImage,
                this.mApp.getDisplayImageOptions(),
                mAnimateFirstListener
        );

        holder.eventTitle.setText(mData.get(position).getEventTitle());
        holder.eventDate.setText(mData.get(position).getEventDateDayName() + "\n" + mData.get(position).getEventDate());

        if (mData.get(position).getDistance() != 0.0) {
            holder.containerEventPlaceDistance.setVisibility(View.VISIBLE);
            holder.eventPlaceDistance.setText("~" + String.format(FORMAT_KM, mData.get(position).getDistance()));
        } else {
            holder.eventPlaceDistance.setVisibility(View.GONE);
            holder.containerEventPlaceDistance.setVisibility(View.GONE);
        }

        if (mData.get(position).getPlaceInstitutionName() != "") {

            holder.placeInstitutionName.setText(
                    mData.get(position).getPlaceInstitutionName() + " \n" +
                            mData.get(position).getCityName() + "\n" + mData.get(position).getPlaceStreetName());

        } else {
            holder.placeInstitutionName.setText(mData.get(position).getCityName() + "\n" + mData.get(position).getPlaceStreetName());
        }

        holder.categoryName.setText(mData.get(position).getCategoryName());

        if (mData.get(position).isFreeTickets()) {
            holder.buttonShowWeb.setBackgroundResource(R.color.omomo_blue);
            holder.buttonShowWeb.setText(mContext.getString(R.string.omomo_buy_ticket));
        } else {
            holder.buttonShowWeb.setBackgroundResource(R.color.omomo_no_buy_ticket);
            holder.buttonShowWeb.setText(mContext.getString(R.string.omomo_no_buy_ticket));
        }

        holder.buttonShowWeb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mData.get(position).isFreeTickets()) {
                    openRepertoireWebView(position);
                }
            }

        });

        holder.buttonShowPlaces.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openMapView(position);
            }

        });

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openEventData(position);
            }

        });

        holder.categoryName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openEventData(position);
            }

        });

        holder.eventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openEventData(position);
            }

        });

        holder.placeInstitutionName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openEventData(position);
            }

        });

        return convertView;
    }

    private void openRepertoireWebView(int position) {

        Repertoire repertoire = mData.get(position);
        Intent intent = new Intent(mContext, WebActivity.class);

        intent.putExtra(WebActivity.TAG_URL,
                String.format(FORMAT_WEB_ACTIVITY_URL_STRING,
                        mContext.getString(R.string.omomo_http),
                        mContext.getString(R.string.omomo_url) + "/",
                        repertoire.getRepertoireSystemId(),
                        mContext.getString(R.string.omomo_repertoire_url_const
                        ))
        );

        ((Activity) mContext).startActivity(intent);
        System.gc();

    }

    private void openMapView(int position) {

        Repertoire repertoire = mData.get(position);
        Intent intent = new Intent(mContext, MapActivity.class);

        intent.putExtra(MapActivity.TAG_SETTINGS, new SettingsParcel(
                App.getSettingsData().getDistance(),
                App.getSettingsData().isDrawCircle(),
                App.isActualPositionMode()));

        intent.putExtra(MapActivity.TAG_LOCATION, new LocationParcel(
                String.valueOf(repertoire.getId()),
                repertoire.getPlaceName(),
                repertoire.getPlaceInstitutionName(),
                repertoire.getCityName() + ", " + repertoire.getPlaceStreetName(),
                repertoire.getLatitude(),
                repertoire.getLongitude()
        ));

        ((Activity) mContext).startActivity(intent);
        System.gc();

    }

    private void openDirections(int position) {

        Repertoire repertoire = mData.get(position);

        LocationParcel locationParcel = new LocationParcel(
                String.valueOf(repertoire.getId()),
                repertoire.getPlaceName(),
                repertoire.getPlaceInstitutionName(),
                repertoire.getCityName() + ", " + repertoire.getPlaceStreetName(),
                repertoire.getLatitude(),
                repertoire.getLongitude()
        );

        MapUtils.directions(((Activity) mContext), locationParcel);

    }

    private void openEventData(int position) {

        Repertoire repertoire = mData.get(position);

        Intent intent = new Intent(mContext, EventActivity.class);
        intent.putExtra(EventActivity.TAG_EVENT_ID, repertoire.getEventId());
        intent.putExtra(EventActivity.TAG_EVENT_NAME, repertoire.getEventTitle());
        intent.putExtra(EventActivity.TAG_REPERTOIRE_ID, repertoire.getRepertoireSystemId());

        ((Activity) mContext).startActivityForResult(intent, MainListActivity.REQUEST_REPERTOIRE_FILTER);
        System.gc();

    }

    static class RepertoireHolder implements Serializable {

        TextView eventTitle;
        Button eventDate;
        Button placeInstitutionName;
        Button categoryName;
        TextView eventPlaceDistance;
        RelativeLayout containerEventPlaceDistance;
        Button buttonShowWeb;
        ImageButton buttonShowPlaces;
        ImageView eventImage;
    }

}
