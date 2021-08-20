package pl.visualnet.omomo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.adapter.RepertoireAdapter;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.utils.LocationParcel;

import java.util.ArrayList;
import java.util.List;

public class LocationRepertoireTask extends AsyncTask<LocationParcel, Void, List<Repertoire>> {

    App mApp;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private ListView mListView;

    public LocationRepertoireTask(Context context, ProgressDialog progressDialog, ListView listView, App app) {
        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mListView = listView;
        this.mApp = app;
    }

    @Override
    protected List<Repertoire> doInBackground(LocationParcel... params) {

        try {

            //List<Repertoire> repertoires = LocationReader
            //        .getInstance(App.API_HOST)
            //        .getLocationRepertoires(params[0].getCityId(), params[0].getId());

            //return repertoires;

            return null;

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
    protected void onPostExecute(List<Repertoire> repertoires) {

        if (repertoires == null) {
            repertoires = new ArrayList<Repertoire>();
        }

        if (repertoires.isEmpty()) {

            Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();
            mListView.setAdapter(null);

        } else {

            RepertoireAdapter adapter = new RepertoireAdapter(
                    this.mContext,
                    mProgressDialog,
                    mApp,
                    R.layout.listview_repertoire_item_row,
                    repertoires
            );

            mListView.setAdapter(adapter);

        }

        System.gc();
        mProgressDialog.dismiss();

    }

}