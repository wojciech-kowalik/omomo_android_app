package pl.visualnet.omomo.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.*;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;
import pl.visualnet.omomo.utils.JavascriptUtils;

import java.util.Iterator;

public class WebActivity extends Activity {

    public static final String TAG_URL = "tag_url";
    public static final String WORTH_FORMAT = "(%.2f zł)";
    ProgressDialog mProgressDialog;
    App mApp;
    Button mButtonRealizeOrder;
    Button mButtonCheckData;
    Button mButtonBackToRepertoire;
    RelativeLayout mCartContainer;
    RelativeLayout mEventDescription;
    WebView mWebView;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mApp = (App) getApplication();
        activity = this;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.omomo_location_retrieving_data));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mWebView = (WebView) findViewById(R.id.web);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.addJavascriptInterface(new JavascriptUtils(this), "AndroidInterface");

        Bundle data = getIntent().getExtras();
        mWebView.loadUrl(data.get(TAG_URL).toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }

        mButtonRealizeOrder = (Button) findViewById(R.id.btn_omomo_realize_order);
        mButtonCheckData = (Button) findViewById(R.id.btn_omomo_order_pay);
        mButtonBackToRepertoire = (Button) findViewById(R.id.btn_omomo_back_to_the_list);
        mCartContainer = (RelativeLayout) findViewById(R.id.omomo_cart_container);
        mEventDescription = (RelativeLayout) findViewById(R.id.omomo_web_view);

        mCartContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                androidShowCartData();
            }
        });

        mButtonBackToRepertoire.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle(R.string.omomo_close_web_view);

                alert.setPositiveButton(R.string.omomo_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.finish();
                    }
                });

                alert.setNegativeButton(R.string.omomo_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                alert.show();

            }
        });

        mButtonRealizeOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                androidRealizeOrder();
            }
        });

        mButtonCheckData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                androidRealizeCheckData();
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (mProgressDialog != null) mProgressDialog.show();
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgressDialog != null) mProgressDialog.dismiss();
                super.onPageFinished(view, url);

            }

        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {

        super.onPause();

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.web, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_return:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Procedure to realize order
     * invoke java script code
     */
    public void androidRealizeOrder() {
        mWebView.loadUrl("javascript:Omomo.method.androidRealizeOrder();");
        mButtonRealizeOrder.setVisibility(View.GONE);
        mButtonCheckData.setVisibility(View.VISIBLE);
    }

    /**
     * Procedure to realizecheck data
     * invoke java script code
     */
    public void androidRealizeCheckData() {
        mWebView.loadUrl("javascript:Omomo.method.androidCheckOrderData();");
    }

    /**
     * Procedure after paying
     * invoke java script code
     */
    public void androidPay() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mButtonRealizeOrder.setVisibility(View.GONE);
                mButtonCheckData.setVisibility(View.GONE);
                mButtonBackToRepertoire.setVisibility(View.VISIBLE);
                mCartContainer.setVisibility(View.GONE);
                mEventDescription.setVisibility(View.GONE);
            }

        });
    }

    /**
     * Procedure to invoke order function
     * invoke java script code
     */
    public void androidInvokeOrder() {
        mWebView.loadUrl("javascript:order();");
    }

    /**
     * Procedure to invoke show cart
     * invoke java script code
     */
    public void androidShowCartData() {
        mWebView.loadUrl("javascript:Omomo.method.androidShowCartData();");
        mWebView.setInitialScale(100);
    }

    /**
     * Set event data on android side
     *
     * @param name
     * @param location
     * @param date
     */
    public void androidSetEventData(final String name, final String location, final String date) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView cartEventName = (TextView) findViewById(R.id.omomo_cart_header_name);
                cartEventName.setText(String.valueOf(name));

                TextView cartEventLocation = (TextView) findViewById(R.id.omomo_cart_event_location);
                cartEventLocation.setText(String.valueOf(location));

                TextView cartEventDate = (TextView) findViewById(R.id.omomo_cart_event_date);
                cartEventDate.setText(String.valueOf(date));

                mButtonCheckData.setVisibility(View.GONE);

            }
        });

    }

    /**
     * Set cart data on android side
     *
     * @param count
     * @param worth
     */
    public void androidSetCartData(final String count, final double worth) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView cartCount = (TextView) findViewById(R.id.omomo_cart_count);
                cartCount.setText(count + " ");

                TextView cartWorth = (TextView) findViewById(R.id.omomo_cart_worth);
                cartWorth.setText(String.format(WORTH_FORMAT, worth));

                mButtonRealizeOrder.setVisibility((Integer.parseInt(count) > 0) ? View.VISIBLE : View.GONE);

            }
        });
    }

    /**
     * Get json from string
     *
     * @param stringify String
     * @return JSONObject
     * @throws JSONException
     */
    private JSONObject getJson(String stringify) throws JSONException {
        return new JSONObject(stringify);
    }

    /**
     * Set adress preferences data
     *
     * @param json   JSONObject
     * @param editor SharedPreferences.Editor
     */
    public boolean setAddressesPreferencesData(JSONObject json, SharedPreferences.Editor editor) {

        try {

            Iterator iterator = json.keys();

            while (iterator.hasNext()) {

                String key = (String) iterator.next();
                editor.putString(key, json.get(key).toString());
            }

            editor.commit();

        } catch (JSONException e) {
            return false;
        }

        return true;

    }

    /**
     * Method invoking by dialog
     */
    public void doDialogPositiveClick() {
        finish();
    }

    /**
     * Method invoking by confirm
     */
    public void doConfirmPositiveClick(String stringify) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();

        try {

            if (setAddressesPreferencesData(getJson(stringify), editor)) {
                Toast.makeText(this, getString(R.string.omomo_address_data_saved), Toast.LENGTH_SHORT).show();
                //androidInvokeOrder();
            }

        } catch (JSONException e) {
        }

    }

    /**
     * Method invoking by confirm
     */
    public void doConfirmNegativeClick() {
        //androidInvokeOrder();
    }

}
