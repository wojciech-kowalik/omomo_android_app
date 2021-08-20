package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.task.EventTask;

public class EventActivity extends Activity {

    public static final String TAG_EVENT_ID = "tag_event_id";
    public static final String TAG_REPERTOIRE_ID = "tag_repertoire_id";
    public static final String TAG_EVENT_NAME = "tag_event_name";
    ProgressDialog mProgressDialog;
    int mEventId;
    String mEventName;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);

        Bundle extras = getIntent().getExtras();
        mEventId = extras.getInt(TAG_EVENT_ID);
        mEventName = extras.getString(TAG_EVENT_NAME);
        mActivity = this;

        Button latestRepertoireButton = (Button) findViewById(R.id.show_event_latest_repertoire);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.omomo_location_retrieving_data));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        latestRepertoireButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                App.setFilter(null);
                Intent intent = new Intent();

                intent.putExtra(RepertoireDateTimeFilterActivity.FILTER_STRING_EVENT_NAME, mEventName);
                intent.putExtra(RepertoireDateTimeFilterActivity.FILTER_STRING_EVENT_ID, mEventId);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        new EventTask(this, mProgressDialog, (App) getApplication()).execute(mEventId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_reload:
                new EventTask(this, mProgressDialog, (App) getApplication()).execute(mEventId);
                break;

            case R.id.menu_return:
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
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        setResult(RESULT_FIRST_USER, intent);
        finish();
        super.onBackPressed();
    }
}
