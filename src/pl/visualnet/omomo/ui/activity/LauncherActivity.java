package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import pl.visualnet.omomo.R;

public class LauncherActivity extends Activity {

    private static final int SPLASH_TIME = 1000;
    PackageInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        PackageManager manager = this.getPackageManager();

        try {
            mInfo = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView versionText = (TextView) findViewById(R.id.omomo_app_version);
        versionText.setText(String.valueOf("ver. " + mInfo.versionName));

        // check if google service is available
        int state = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (state == ConnectionResult.SUCCESS) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startMainActivity();
                }

            }, SPLASH_TIME);

        } else {

            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(state, this, -1);
            dialog.show();
            return;
        }

    }

    private void startMainActivity() {

        Intent intent = new Intent(LauncherActivity.this, MainListActivity.class);
        startActivity(intent);
        finish();

    }

    public void doConfirmPositiveClick() {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        finish();

    }

    public void doConfirmNegativeClick() {
        startMainActivity();
    }

}
