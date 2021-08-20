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
import pl.visualnet.omomo.domain.Category;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.reader.CategoryReader;
import pl.visualnet.omomo.utils.MultiSpinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class CategoryListTask extends AsyncTask<Filter, Void, List<Category>> {

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private CategoryReader mReader;
    private MultiSpinner mSpinner;
    private EditText mCategoryId;
    private EditText mCategoryName;
    private Filter mFilter;

    public CategoryListTask(Context context, ProgressDialog progressDialog, MultiSpinner spinner, EditText categoryId, EditText categoryName) {
        this.mContext = context;
        this.mProgressDialog = progressDialog;
        this.mSpinner = spinner;
        this.mCategoryId = categoryId;
        this.mCategoryName = categoryName;
    }

    @Override
    protected List<Category> doInBackground(Filter... params) {

        try {

            mFilter = (params[0] == null) ? new Filter() : params[0];
            mReader = new CategoryReader(new URL(App.API_HOST));
            List<Category> categories = mReader.getCategories();

            return categories;

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
    protected void onPostExecute(final List<Category> categories) {

        LinkedList<String> categoriesNames = new LinkedList<String>();

        if (categories == null) {

            Toast.makeText(this.mContext, R.string.omomo_get_data_empty, Toast.LENGTH_SHORT).show();

        } else {

            for (Category category : categories) {
                categoriesNames.add(category.getName());
            }

            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categoriesNames);

            MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {

                public void onItemsSelected(boolean[] selected) {

                    List<String> names = new ArrayList<String>();
                    List<String> ids = new ArrayList<String>();

                    for (int i = 0; i < selected.length; i++) {

                        if (selected[i]) {
                            names.add(categories.get(i).getName());
                            ids.add(String.valueOf(categories.get(i).getId()));
                        }
                    }

                    mCategoryId.setText(TextUtils.join(",", ids));
                    mCategoryName.setText(TextUtils.join(", ", names));

                }
            };

            dataAdapter.setDropDownViewResource(R.layout.item_spinner);
            mSpinner.setAdapter(dataAdapter, false, onSelectedListener);

            if (mFilter.getCategory() != null) {

                // select chosen value
                boolean[] selectedItems = new boolean[0];

                if (!mFilter.getCategory().getName().equals(null)) {

                    selectedItems = new boolean[dataAdapter.getCount()];
                    StringTokenizer tokens = new StringTokenizer(mFilter.getCategory().getName(), ", ");

                    while (tokens.hasMoreTokens()) {
                        selectedItems[dataAdapter.getPosition(tokens.nextToken())] = true;
                    }
                }

                mSpinner.setSelected(selectedItems);
                mCategoryId.setText(mFilter.getCategory().getId());
                mCategoryName.setText(mFilter.getCategory().getName());

            }

            categoriesNames = null;
            System.gc();

        }

        mProgressDialog.dismiss();

    }


}