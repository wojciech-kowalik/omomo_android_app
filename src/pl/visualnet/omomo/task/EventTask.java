package pl.visualnet.omomo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.adapter.RepertoireAdapter;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.reader.EventReader;
import pl.visualnet.omomo.ui.activity.EventActivity;
import pl.visualnet.omomo.ui.activity.WebActivity;
import pl.visualnet.omomo.utils.AnimateFirstDisplayListener;

public class EventTask extends AsyncTask<Integer, Void, Event> {

    App mApp;
    ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public EventTask(Context context, ProgressDialog progressDialog, App app) {

        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mApp = app;

    }

    @Override
    protected Event doInBackground(Integer... params) {

        try {

            Event event = EventReader
                    .getInstance(App.API_HOST)
                    .getEvent(params[0]);

            return event;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        mProgressDialog.show();

    }

    @Override
    protected void onPostExecute(Event event) {

        if (event == null) {

            Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();

        } else {

            // get event data
            // get latest repertoire data

            final Repertoire latest = event.getRepertoires().get(0);
            String imageUrl;

            TextView headerEvent = (TextView) ((EventActivity) mContext).findViewById(R.id.list_header_event);
            headerEvent.setText(event.getName());

            TextView categoryName = (Button) ((EventActivity) mContext).findViewById(R.id.event_category_name);
            categoryName.setText(latest.getCategoryName());

            Button eventLatestDate = (Button) ((EventActivity) mContext).findViewById(R.id.event_latest_date);
            eventLatestDate.setText(latest.getEventDateDayName() + "\n" + latest.getEventDate());

            Button eventLatestPlace = (Button) ((EventActivity) mContext).findViewById(R.id.event_latest_place);

            ImageView image = (ImageView) ((EventActivity) mContext).findViewById(R.id.event_data_image);
            TextView eventDescription = (TextView) ((EventActivity) mContext).findViewById(R.id.event_description);

            eventLatestPlace.setText(
                    latest.getPlaceInstitutionName() + "\n" +
                            latest.getCityName() + "\n" + latest.getPlaceStreetName());

            // remove latest repertoire
            event.getRepertoires().remove(0);

            if (latest.getSystemImageUrl() == null) {
                imageUrl = App.API_HOST + "/" + latest.getCategoryImage();
            } else {
                imageUrl = mContext.getString(R.string.omomo_http) + latest.getSystemUrl() + latest.getSystemImageUrl();
            }

            if (latest.getEventDescription() == null || latest.getEventDescription().isEmpty()) {
                eventDescription.setText(Html.fromHtml(event.getCmsDescription()));
            } else {
                eventDescription.setText(Html.fromHtml(latest.getEventDescription()));
            }

            // make clickable html links
            eventDescription.setMovementMethod(LinkMovementMethod.getInstance());

            Button buyButton = (Button) ((EventActivity) mContext).findViewById(R.id.show_event_buy_ticket);

            if (latest.isFreeTickets()) {
                buyButton.setBackgroundResource(R.color.omomo_blue);
                buyButton.setText(mContext.getString(R.string.omomo_buy_ticket));
            } else {
                buyButton.setBackgroundResource(R.color.omomo_no_buy_ticket);
                buyButton.setText(mContext.getString(R.string.omomo_no_buy_ticket));
            }

            buyButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    if (latest.isFreeTickets() == false) {
                        return;
                    }

                    Intent intent = new Intent(((EventActivity) mContext), WebActivity.class);

                    intent.putExtra(WebActivity.TAG_URL,
                            String.format(RepertoireAdapter.FORMAT_WEB_ACTIVITY_URL_STRING,
                                    ((EventActivity) mContext).getString(R.string.omomo_http),
                                    ((EventActivity) mContext).getString(R.string.omomo_url) + "/",
                                    latest.getRepertoireSystemId(),
                                    ((EventActivity) mContext).getString(R.string.omomo_repertoire_url_const
                                    ))
                    );

                    ((EventActivity) mContext).startActivity(intent);

                }
            });

            ImageLoader.getInstance().displayImage(
                    imageUrl,
                    image,
                    this.mApp.getDisplayImageOptions(),
                    mAnimateFirstListener
            );

        }

        System.gc();
        mProgressDialog.dismiss();

    }

}