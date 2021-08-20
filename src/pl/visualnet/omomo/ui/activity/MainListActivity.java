package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.task.RepertoireTask;
import pl.visualnet.omomo.utils.ActualPositionUtils;
import pl.visualnet.omomo.utils.CategoryParcel;
import pl.visualnet.omomo.utils.CityParcel;
import pl.visualnet.omomo.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends ListActivity {

    public static final int REQUEST_REPERTOIRE_FILTER = 1;
    public static final int REQUEST_SETTINGS = 2;
    App mApp;
    ProgressDialog mProgressDialog;
    TextView mFilterDescription, mEmptyRepertoire;
    RelativeLayout mFilterContainerDescription;
    Activity mActivity;
    ImageButton mActualPositionOff, mActualPositionOn;
    TextView mActualPositionOnText;
    SwipeRefreshLayout mSwipeLayout;
    Button mCleanFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        mApp = (App) getApplication();
        mProgressDialog = new ProgressDialog(MainListActivity.this);
        mActivity = this;

        this.getListView().setVerticalFadingEdgeEnabled(false);
        mActualPositionOff = (ImageButton) findViewById(R.id.btn_omomo_actual_position_off);
        mActualPositionOn = (ImageButton) findViewById(R.id.btn_omomo_actual_position_on);
        mActualPositionOnText = (TextView) findViewById(R.id.text_omomo_actual_position_on);
        mEmptyRepertoire = (TextView) findViewById(R.id.empty_list);
        mCleanFilter = (Button) findViewById(R.id.btn_omomo_filtr_clean);

        mProgressDialog.setMessage(getResources().getString(R.string.omomo_location_retrieving_data));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.omomo_blue, R.color.omomo_navy, R.color.omomo_navy);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doTasks(mSwipeLayout);
            }

        });

        doTasks(mSwipeLayout);

        if (App.isActualPositionMode()) {
            mActualPositionOn.setVisibility(View.VISIBLE);
            mActualPositionOnText.setVisibility(View.VISIBLE);
            mActualPositionOff.setVisibility(View.GONE);
        } else {
            mActualPositionOn.setVisibility(View.GONE);
            mActualPositionOnText.setVisibility(View.GONE);
            mActualPositionOff.setVisibility(View.VISIBLE);
        }

        mActualPositionOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActualPositionUtils.enable(mActivity, mActualPositionOn, mActualPositionOff, mActualPositionOnText);
                doTasks(mSwipeLayout);
            }

        });

        mActualPositionOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActualPositionUtils.disable(mActualPositionOn, mActualPositionOff, mActualPositionOnText);
                doTasks(mSwipeLayout);
            }

        });

        mCleanFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                App.setFilter(null);
                doTasks(mSwipeLayout);
                mFilterContainerDescription.setVisibility(View.GONE);
            }

        });

    }

    @Override
    public void onLowMemory() {

        System.gc();
        super.onLowMemory();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_reload:
                doTasks(mSwipeLayout);
                break;

            case R.id.menu_filter_repertoire:
                this.startFilterActivity();
                break;

            case R.id.menu_settings:
                Intent intent = new Intent(MainListActivity.this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_SETTINGS);
                break;

            case R.id.menu_clear_cache:
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().clearDiskCache();
                doTasks(mSwipeLayout);
                break;

            case R.id.menu_close:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onPause() {

        super.onPause();

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    @Override
    protected void onRestart() {

        markInternetConnected();
        super.onRestart();
    }

    @Override
    protected void onResume() {

        //revert(false);
        markInternetConnected();

        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // do action from settings mActivity
        if (requestCode == REQUEST_SETTINGS) {

            if (resultCode == RESULT_OK) {

                doTasks(mSwipeLayout);


            }

        }

        // do action from filter mActivity
        if (requestCode == REQUEST_REPERTOIRE_FILTER) {

            mFilterDescription = (TextView) findViewById(R.id.list_header_filtr);
            mFilterContainerDescription = (RelativeLayout) findViewById(R.id.list_header_filtr_container);

            if (resultCode == RESULT_OK) {

                filterData(data);
            }

            if (resultCode == RESULT_CANCELED) {

                if (App.getFilter() == null) {
                    mFilterDescription.setVisibility(View.GONE);
                    mFilterContainerDescription.setVisibility(View.GONE);
                    doTasks(mSwipeLayout);
                }
            }

        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.omomo_close_application);

        alert.setPositiveButton(R.string.omomo_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mApp.setFilter(null);
                finish();
                //moveTaskToBack(true);
            }
        });

        alert.setNegativeButton(R.string.omomo_no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        alert.show();

    }

    /**
     * @param swipeLayout
     */
    public void doTasks(SwipeRefreshLayout swipeLayout) {

        new RepertoireTask(this, mProgressDialog, getListView(), mApp, swipeLayout).execute();
        markInternetConnected();
    }

    private void startFilterActivity() {

        Intent intent = new Intent(this, RepertoireDateTimeFilterActivity.class);
        startActivityForResult(intent, REQUEST_REPERTOIRE_FILTER);

    }

    private void revert(boolean withoutTasks) {

        // regenerate actual position
        App.sActualPosition = MapUtils.getMyCoordinates(this);

        if (withoutTasks == false) {
            doTasks(mSwipeLayout);
        }

    }

    public void doConfirmPositiveClick() {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        finish();

    }

    public void filterData(Intent data) {

        Filter filter = new Filter();
        filter.setDateFrom(data.getExtras().getString(RepertoireDateTimeFilterActivity.FILTER_STRING_REPERTOIRE_DATE_FROM));
        filter.setDateTo(data.getExtras().getString(RepertoireDateTimeFilterActivity.FILTER_STRING_REPERTOIRE_DATE_TO));
        filter.setEventName(data.getExtras().getString(RepertoireDateTimeFilterActivity.FILTER_STRING_EVENT_NAME));
        filter.setCity(data.<CityParcel>getParcelableExtra(RepertoireDateTimeFilterActivity.FILTER_STRING_EVENT_CITY));
        filter.setCategory(data.<CategoryParcel>getParcelableExtra(RepertoireDateTimeFilterActivity.FILTER_STRING_CATEGORY));

        App.setFilter(filter);

        List<String> parameters = new ArrayList<String>();

        if (App.getFilter() != null) {
            mFilterDescription.setVisibility(View.VISIBLE);
            mFilterContainerDescription.setVisibility(View.VISIBLE);
        }

        if (!App.getFilter().getDateFrom(true).isEmpty()) {
            parameters.add(getString(R.string.omomo_filter_from) + App.getFilter().getDateFrom(true));
        }

        if (!App.getFilter().getDateTo(true).isEmpty()) {
            parameters.add(getString(R.string.omomo_filter_to) + App.getFilter().getDateTo(true));
        }

        if (!App.getFilter().getEventName().isEmpty()) {
            parameters.add(getString(R.string.omomo_filter_event) + App.getFilter().getEventName());
        }

        if (App.getFilter().getCity() != null) {
            parameters.add(getString(R.string.omomo_filter_city) + App.getFilter().getCity().getName());
        }

        if (App.getFilter().getCategory() != null) {
            parameters.add(getString(R.string.omomo_filter_category) + App.getFilter().getCategory().getName());
        }

        mFilterDescription.setText(getString(R.string.omomo_filter) + TextUtils.join(", ", parameters.toArray()));
        doTasks(mSwipeLayout);

    }

    public void markInternetConnected() {

        if (App.isInternetConnected()) {
            mEmptyRepertoire.setText(getString(R.string.omomo_no_repertoire));
        } else {
            mEmptyRepertoire.setText(getString(R.string.no_network));
        }

    }

}
