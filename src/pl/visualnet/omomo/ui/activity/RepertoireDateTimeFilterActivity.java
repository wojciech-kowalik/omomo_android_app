package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.adapter.EventAutoCompleteAdapter;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.task.CategoryListTask;
import pl.visualnet.omomo.task.CityListTask;
import pl.visualnet.omomo.ui.fragment.AppDialogFragment;
import pl.visualnet.omomo.ui.view.DelayAutoCompleteTextView;
import pl.visualnet.omomo.utils.CategoryParcel;
import pl.visualnet.omomo.utils.CityParcel;
import pl.visualnet.omomo.utils.MultiSpinner;

import java.util.Calendar;

public class RepertoireDateTimeFilterActivity extends Activity implements View.OnClickListener {

    public static final String FILTER_STRING_REPERTOIRE_DATE_FROM = "filter_repertoire_date_from";
    public static final String FILTER_STRING_REPERTOIRE_DATE_TO = "filter_repertoire_date_to";
    public static final String FILTER_STRING_EVENT_NAME = "filter_event_name";
    public static final String FILTER_STRING_EVENT_CITY = "filter_event_city";
    public static final String FILTER_STRING_CATEGORY = "filter_category";
    public static final String FILTER_STRING_EVENT_ID = "filter_event_id";
    public static final int THRESHOLD = 2;

    EditText mTxtDateFrom, mTxtTimeFrom, mTxtDateTo, mTxtTimeTo, mTxtCityId, mTxtCityName, mTxtCategoryId, mTxtCategoryName;
    Calendar mCalendar;
    Button mBtnFilter, mBtnFilterClean;
    ImageButton mBtnFilterClose;
    ProgressDialog mProgressDialog;
    MultiSpinner mSpinnerCities, mSpinnerCategories;
    DelayAutoCompleteTextView mDelayAutoCompleteTextView;

    private int mYearFrom, mMonthFrom, mDayFrom, mHourFrom, mMinuteFrom;
    private int mYearTo, mMonthTo, mDayTo, mHourTo, mMinuteTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertoire_filter);

        initialViews();

        // execute city task
        new CityListTask(this, mProgressDialog, mSpinnerCities, mTxtCityId, mTxtCityName)
                .execute(App.getSettingsData().getFilter());

        // execute category task
        new CategoryListTask(this, mProgressDialog, mSpinnerCategories, mTxtCategoryId, mTxtCategoryName)
                .execute(App.getSettingsData().getFilter());

        this.getDataFromExistingFilter(App.getSettingsData().getFilter());
        this.setListeners();

    }


    @Override
    public void onClick(View v) {

        if (v == mTxtDateFrom) {

            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String date = String.format("%02d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            mTxtDateFrom.setText(date);

                            mHourFrom = 00;
                            mMinuteFrom = 00;
                            mTxtTimeFrom.setText("00:00");

                        }
                    }, mYearFrom, mMonthFrom, mDayFrom);

            dpd.setTitle(getString(R.string.omomo_choose_date));
            dpd.show();
        }

        if (v == mTxtDateTo) {

            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            String date = String.format("%02d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                            mTxtDateTo.setText(date);

                            mHourTo = 23;
                            mMinuteTo = 59;
                            mTxtTimeTo.setText("23:59");

                        }
                    }, mYearTo, mMonthTo, mDayTo);

            dpd.setTitle(getString(R.string.omomo_choose_date));
            dpd.show();
        }

        if (v == mTxtTimeFrom) {

            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            String time = String.format("%02d:%02d", hourOfDay, minute);
                            mTxtTimeFrom.setText(time);
                        }
                    }, mHourFrom, mMinuteFrom, true);

            tpd.setTitle(getString(R.string.omomo_choose_time));
            tpd.show();
        }

        if (v == mTxtTimeTo) {

            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            String time = String.format("%02d:%02d", hourOfDay, minute);
                            mTxtTimeTo.setText(time);
                        }
                    }, mHourTo, mMinuteTo, true);

            tpd.setTitle(getString(R.string.omomo_choose_time));
            tpd.show();
        }

        if (v == mBtnFilter) {

            String dateFrom = mTxtDateFrom.getText().toString();
            String timeFrom = mTxtTimeFrom.getText().toString();
            String dateTo = mTxtDateTo.getText().toString();
            String timeTo = mTxtTimeTo.getText().toString();
            String eventName = mDelayAutoCompleteTextView.getText().toString();
            String cityName = mTxtCityName.getText().toString();
            String cityId = mTxtCityId.getText().toString();
            String categoryName = mTxtCategoryName.getText().toString();
            String categoryId = mTxtCategoryId.getText().toString();

            String errorMonit = "";
            boolean isError = false;

            if (TextUtils.isEmpty(dateFrom) && TextUtils.isEmpty(timeFrom)
                    && TextUtils.isEmpty(dateTo) && TextUtils.isEmpty(timeTo)
                    && TextUtils.isEmpty(eventName) && TextUtils.isEmpty(cityName)
                    && TextUtils.isEmpty(categoryName)) {

                errorMonit = getString(R.string.omomo_empty_filter_criteria);
                isError = true;
            }

            if (!TextUtils.isEmpty(timeFrom) && TextUtils.isEmpty(dateFrom)) {
                errorMonit = getString(R.string.omomo_empty_date_from);
                isError = true;
            }

            if (!TextUtils.isEmpty(timeTo) && TextUtils.isEmpty(dateTo)) {
                errorMonit = getString(R.string.omomo_empty_date_to);
                isError = true;
            }

            if (isError) {

                AppDialogFragment.getInstance(getString(R.string.omomo_error_occur), errorMonit, true)
                        .show(getFragmentManager(), AppDialogFragment.ERROR_DIALOG_TAG);
                return;

            }

            Intent intent = new Intent();
            intent.putExtra(FILTER_STRING_REPERTOIRE_DATE_FROM, (dateFrom.isEmpty() && timeFrom.isEmpty()) ? "" : dateFrom + ";" + timeFrom);
            intent.putExtra(FILTER_STRING_REPERTOIRE_DATE_TO, (dateTo.isEmpty() && timeTo.isEmpty()) ? "" : dateTo + ";" + timeTo);
            intent.putExtra(FILTER_STRING_EVENT_NAME, mDelayAutoCompleteTextView.getText().toString());

            CityParcel cityParcel = (cityId.isEmpty()) ? null : new CityParcel(cityId, cityName, "");
            CategoryParcel categoryParcel = (categoryId.isEmpty()) ? null : new CategoryParcel(categoryId, categoryName);

            //set city parcel
            intent.putExtra(FILTER_STRING_EVENT_CITY, cityParcel);

            //set category parcel
            intent.putExtra(FILTER_STRING_CATEGORY, categoryParcel);

            setResult(RESULT_OK, intent);
            finish();

        }

        if (v == mBtnFilterClean) {

            App.setFilter(null);
            mTxtDateFrom.setText(null);
            mTxtTimeFrom.setText(null);
            mTxtDateTo.setText(null);
            mTxtTimeTo.setText(null);
            mDelayAutoCompleteTextView.setText(null);
            mMinuteFrom = mMinuteTo = 0;
            mSpinnerCities.setDefaultText(getString(R.string.omomo_choose));
            mSpinnerCities.setText("");
            mSpinnerCities.setSelected(false);
            mSpinnerCategories.setDefaultText(getString(R.string.omomo_choose));
            mSpinnerCategories.setText("");
            mSpinnerCategories.setSelected(false);
            mTxtCityName.setText(null);
            mTxtCityId.setText(null);
            mTxtCategoryName.setText(null);
            mTxtCategoryId.setText(null);
            Toast.makeText(this, getString(R.string.omomo_repertoire_filter_clean), Toast.LENGTH_SHORT).show();
        }

        if (v == mBtnFilterClose) {

            if (App.getFilter() == null) {

                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);

            }

            finish();
        }

    }

    private void initialViews() {

        mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage(getResources().getString(R.string.omomo_location_retrieving_data));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mBtnFilter = (Button) findViewById(R.id.btn_filter);
        mBtnFilterClean = (Button) findViewById(R.id.btn_filter_clean);
        //mBtnFilterClose = (ImageButton) findViewById(R.id.btn_filter_close);

        mTxtDateFrom = (EditText) findViewById(R.id.txt_date_from);
        mTxtTimeFrom = (EditText) findViewById(R.id.txt_time_from);

        mTxtDateFrom = (EditText) findViewById(R.id.txt_date_from);
        mTxtTimeFrom = (EditText) findViewById(R.id.txt_time_from);

        mTxtDateTo = (EditText) findViewById(R.id.txt_date_to);
        mTxtTimeTo = (EditText) findViewById(R.id.txt_time_to);

        mTxtCityId = (EditText) findViewById(R.id.txt_city_id);
        mTxtCityName = (EditText) findViewById(R.id.txt_city_name);

        mTxtCategoryId = (EditText) findViewById(R.id.txt_category_id);
        mTxtCategoryName = (EditText) findViewById(R.id.txt_category_name);

        final Calendar c = Calendar.getInstance();

        mYearFrom = mYearTo = c.get(Calendar.YEAR);
        mMonthFrom = mMonthTo = c.get(Calendar.MONTH);
        mDayFrom = mDayTo = c.get(Calendar.DAY_OF_MONTH);
        mHourFrom = mHourTo = c.get(Calendar.HOUR_OF_DAY);
        mMinuteFrom = mMinuteTo = 0;

        mDelayAutoCompleteTextView = (DelayAutoCompleteTextView) findViewById(R.id.auto_complete_filter_text_view);
        mDelayAutoCompleteTextView.setThreshold(THRESHOLD);
        mDelayAutoCompleteTextView.setAdapter(new EventAutoCompleteAdapter(this));
        mDelayAutoCompleteTextView.setLoadingIndicator((ProgressBar) findViewById(R.id.loading_indicator));

        mSpinnerCities = (MultiSpinner) findViewById(R.id.repertoire_filter_city);
        mSpinnerCategories = (MultiSpinner) findViewById(R.id.repertoire_filter_category);

    }

    private void setListeners() {

        // delay completer text view on item click listener
        mDelayAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Event event = (Event) adapterView.getItemAtPosition(position);
                mDelayAutoCompleteTextView.setText(event.getName());
            }

        });

        mTxtDateFrom.setOnClickListener(this);
        mTxtDateTo.setOnClickListener(this);

        mTxtTimeFrom.setOnClickListener(this);
        mTxtTimeTo.setOnClickListener(this);

        mBtnFilter.setOnClickListener(this);
        mBtnFilterClean.setOnClickListener(this);

    }

    /**
     * @param filter Filter
     */
    private void getDataFromExistingFilter(Filter filter) {

        if (filter != null) {

            try {

                mDelayAutoCompleteTextView.setText(filter.getEventName());

                String dateFromFilter = filter.getDateFrom(false);
                String[] dateTimeFrom = new String[]{};
                String dateToFilter = filter.getDateTo(false);
                String[] dateTimeTo = new String[]{};

                if (dateFromFilter != null) {
                    dateTimeFrom = TextUtils.split(filter.getDateFrom(false), ";");
                }

                if (dateToFilter != null) {
                    dateTimeTo = TextUtils.split(filter.getDateTo(false), ";");
                }

                if (dateTimeFrom.length != 0) {

                    String[] dateFrom = TextUtils.split(dateTimeFrom[0], "-");

                    if (dateTimeFrom.length == 2 && !dateTimeFrom[1].isEmpty()) {

                        String[] timeFrom = TextUtils.split(dateTimeFrom[1], ":");
                        mHourFrom = Integer.parseInt(timeFrom[0]);
                        mMinuteFrom = Integer.parseInt(timeFrom[1]);
                        mTxtTimeFrom.setText(dateTimeFrom[1]);

                    }

                    mYearFrom = Integer.parseInt(dateFrom[0]);
                    mMonthFrom = Integer.parseInt(dateFrom[1]) - 1;
                    mDayFrom = Integer.parseInt(dateFrom[2]);

                    mTxtDateFrom.setText(dateTimeFrom[0]);

                }

                if (dateTimeTo.length != 0) {

                    String[] dateTo = TextUtils.split(dateTimeTo[0], "-");

                    if (dateTimeTo.length == 2 && !dateTimeTo[1].isEmpty()) {

                        String[] timeTo = TextUtils.split(dateTimeTo[1], ":");
                        mHourTo = Integer.parseInt(timeTo[0]);
                        mMinuteTo = Integer.parseInt(timeTo[1]);
                        mTxtTimeTo.setText(dateTimeTo[1]);

                    }

                    mYearTo = Integer.parseInt(dateTo[0]);
                    mMonthTo = Integer.parseInt(dateTo[1]) - 1;
                    mDayTo = Integer.parseInt(dateTo[2]);

                    mTxtDateTo.setText(dateTimeTo[0]);

                }

            } catch (IndexOutOfBoundsException e) {
            }

        }

    }


}
