package pl.visualnet.omomo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.Location;

import java.io.Serializable;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    Context mContext;
    int mLayoutResourceId;
    List<Location> mData = null;
    ProgressDialog mProgressDialog;
    App mApp;
    LayoutInflater mLayoutInflater;
    TextView mDistanceValue;

    public LocationAdapter(Context context, ProgressDialog progressDialog, App app, int layoutResourceId, List<Location> data) {

        super(context, layoutResourceId, data);
        this.mLayoutResourceId = layoutResourceId;
        this.mContext = context;
        this.mData = data;
        this.mProgressDialog = progressDialog;
        this.mApp = app;
        mLayoutInflater = LayoutInflater.from(getContext());

    }

    public List<Location> getItems() {
        return this.mData;
    }

    public void setItems(List<Location> data) {
        this.mData = data;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ListView listView = (ListView) parent;
        LocationHolder holder = null;

        if (convertView == null) {

            convertView = mLayoutInflater.inflate(mLayoutResourceId, parent, false);

            holder = new LocationHolder();
            holder.name = (TextView) convertView.findViewById(R.id.location_name);
            holder.counter = (TextView) convertView.findViewById(R.id.location_repertoire_counter);
            holder.buttonShowAllRepertoire = (ImageView) convertView.findViewById(R.id.show_all_repertoire);
            convertView.setTag(holder);

        } else {
            holder = (LocationHolder) convertView.getTag();
        }

        holder.name.setText(mData.get(position).getName());
        holder.counter.setText(mContext.getResources().getString(R.string.omomo_term_counter) + " " + String.valueOf(mData.get(position).getRepertoireCounter()));

        holder.buttonShowAllRepertoire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Location location = mData.get(position);

                //if (location == null) {
                //    return;
                //}

                //Intent intent = new Intent(mContext, RepertoireListActivity.class);
                //intent.putExtra(RepertoireListActivity.TAG_LOCATION, new LocationParcel(String.valueOf(location.getId()), location.getCityId(), location.getName()));
                //((Activity) mContext).startActivity(intent);

                System.gc();

            }
        });


        return convertView;
    }

    static class LocationHolder implements Serializable {
        TextView name;
        TextView counter;
        ImageView buttonShowAllRepertoire;
    }
}
