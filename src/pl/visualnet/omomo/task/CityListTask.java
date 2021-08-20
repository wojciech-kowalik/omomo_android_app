package pl.visualnet.omomo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.reader.CityReader;
import pl.visualnet.omomo.utils.MultiSpinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class CityListTask extends AsyncTask<Filter, Void, List<City>> {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private CityReader mReader;
    private MultiSpinner mSpinner;
    private EditText mCityId;
    private EditText mCityName;
    private Filter mFilter;

    public CityListTask(Context context, ProgressDialog progressDialog, MultiSpinner spinner, EditText cityId, EditText cityName) {
        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mSpinner = spinner;
        this.mCityId = cityId;
        this.mCityName = cityName;
    }

    @Override
    protected List<City> doInBackground(Filter... params) {

        try {

            mFilter = (params[0] == null) ? new Filter() : params[0];
            mReader = new CityReader(new URL(App.API_HOST));
            List<City> cities = mReader.getCities();

            return cities;

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
    protected void onPostExecute(final List<City> cities) {

        LinkedList<String> citiesNames = new LinkedList<String>();

        if (cities == null) {

            Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();

        } else {

            for (City city : cities) {
                citiesNames.add(city.getName());
            }

            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, citiesNames);

            MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {

                public void onItemsSelected(boolean[] selected) {

                    List<String> names = new ArrayList<String>();
                    List<String> ids = new ArrayList<String>();

                    for (int i = 0; i < selected.length; i++) {

                        if (selected[i]) {
                            names.add(cities.get(i).getName());
                            ids.add(String.valueOf(cities.get(i).getId()));
                        }
                    }

                    mCityId.setText(TextUtils.join(",", ids));
                    mCityName.setText(TextUtils.join(", ", names));

                }
            };

            dataAdapter.setDropDownViewResource(R.layout.item_spinner);
            mSpinner.setAdapter(dataAdapter, false, onSelectedListener);

            if (mFilter.getCity() != null) {

                // select chosen value
                boolean[] selectedItems = new boolean[0];

                if (!mFilter.getCity().getName().equals(null)) {

                    selectedItems = new boolean[dataAdapter.getCount()];
                    StringTokenizer tokens = new StringTokenizer(mFilter.getCity().getName(), ", ");

                    while (tokens.hasMoreTokens()) {
                        selectedItems[dataAdapter.getPosition(tokens.nextToken())] = true;
                    }
                }

                mSpinner.setSelected(selectedItems);
                mCityId.setText(mFilter.getCity().getId());
                mCityName.setText(mFilter.getCity().getName());

            }

            citiesNames = null;
            System.gc();

        }

        mProgressDialog.dismiss();

    }


}