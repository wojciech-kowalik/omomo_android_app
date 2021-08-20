package pl.visualnet.omomo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.R;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.i(App.APPLICATION_NAME, context.getString(R.string.omomo_received_network_status));
        isNetworkAvailable(context);
    }

    /**
     * @param context
     * @return boolean
     */
    private boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Network[] networks = connectivityManager.getAllNetworks();

                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        App.setIsInternetConnected(true);
                        Log.i(App.APPLICATION_NAME, context.getString(R.string.omomo_has_internet_connection));
                        return true;
                    }
                }

            } else {

                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();

                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            App.setIsInternetConnected(true);
                            Log.i(App.APPLICATION_NAME, context.getString(R.string.omomo_has_internet_connection));
                            return true;
                        }
                    }
                }
            }

        }

        Log.e(App.APPLICATION_NAME, context.getString(R.string.no_network));
        Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
        App.setIsInternetConnected(false);

        return false;
    }
}

