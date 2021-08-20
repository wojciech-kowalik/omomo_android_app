package pl.visualnet.omomo.task;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.TextView;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.adapter.RepertoireAdapter;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.domain.Settings;
import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.reader.RepertoireReader;

import java.util.ArrayList;
import java.util.List;

public class RepertoireTask extends AsyncTask<Void, Void, List<Repertoire>> {

    public static final String STUB_VALUE = "stub_value";
    ProgressDialog mProgressDialog;
    Geocoder mGeocoder;
    App mApp;
    ListView mListView;
    Settings mSettings;
    SwipeRefreshLayout mSwipeLayout;
    private ListActivity mContext;

    public RepertoireTask(ListActivity context, ProgressDialog progressDialog, ListView listView, App app, SwipeRefreshLayout swipeLayout) {

        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mGeocoder = new Geocoder(this.mContext);
        this.mApp = app;
        this.mListView = listView;
        this.mSwipeLayout = swipeLayout;

    }

    @Override
    protected List<Repertoire> doInBackground(Void... item) {

        try {

            return (App.isActualPositionMode())
                    ? RepertoireReader.getInstance(App.API_HOST).getRepertoiresFromActualPositionByDistance(mSettings)
                    : RepertoireReader.getInstance(App.API_HOST).getRepertoires(mSettings);

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

        super.onPreExecute();
        mSettings = App.getSettingsData();
        mProgressDialog.show();

    }

    @Override
    protected void onPostExecute(List<Repertoire> repertoires) {

        if (repertoires == null) {
            repertoires = new ArrayList<Repertoire>();
        }

        if (repertoires.isEmpty()) {

            //Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();
            mListView.setAdapter(null);

        } else {

            TextView distanceIndicator = (TextView) mContext.findViewById(R.id.text_omomo_actual_position_on);
            distanceIndicator.setText(mSettings.getDistance() + " km");

            RepertoireAdapter adapter = new RepertoireAdapter(
                    this.mContext,
                    mProgressDialog,
                    mApp,
                    R.layout.listview_repertoire_item_row,
                    repertoires
            );

            mListView.setAdapter(adapter);
        }

        mSwipeLayout.setRefreshing(false);
        mProgressDialog.dismiss();

    }


}
