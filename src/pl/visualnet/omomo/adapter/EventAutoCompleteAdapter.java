package pl.visualnet.omomo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.reader.EventReader;

import java.util.ArrayList;
import java.util.List;

public class EventAutoCompleteAdapter extends BaseAdapter implements Filterable {

    private static final int MAX_RESULTS = 5;
    private Context mContext;
    private List<Event> mResultList = new ArrayList<Event>();

    public EventAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public Event getItem(int index) {
        return mResultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_autocomplete, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.autocomplete_event_name)).setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {

                    List<Event> events = findEvents(constraint.toString());
                    filterResults.values = events;
                    filterResults.count = events.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    mResultList = (List<Event>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<Event> findEvents(String name) {

        List<Event> events = null;

        try {
            events = EventReader
                    .getInstance(App.API_HOST)
                    .getEventsNamesByQuery(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
}
