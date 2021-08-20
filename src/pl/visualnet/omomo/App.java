package pl.visualnet.omomo;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import pl.visualnet.omomo.broadcast.NetworkChangeReceiver;
import pl.visualnet.omomo.domain.Address;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.domain.Settings;

public class App extends Application {

    public static final String API_HOST = "http://omomo.pl";
    public static final int API_VERSION = 1;
    public static final String APPLICATION_NAME = "[OMOMO]";
    public static LatLng sActualPosition = null;
    private static Context sContext;
    private static Settings sSettings;
    private static Filter sFilter;
    private static boolean sIsInternetConnected = false;
    private static boolean sIsActualPositionMode = false;
    private NetworkChangeReceiver receiver;

    public App() {
        Log.i(APPLICATION_NAME, "Start app ");
    }

    /**
     * Get sSettings data
     *
     * @return Settings
     */
    public static Settings getSettingsData() {

        // get configuration data
        SharedPreferences settingsData = PreferenceManager.getDefaultSharedPreferences(sContext);

        Address address = new Address();
        address.setName(settingsData.getString("omomo_settings_order_address_data_name", ""));
        address.setSurname(settingsData.getString("omomo_settings_order_address_data_surname", ""));
        address.setEmail(settingsData.getString("omomo_settings_order_address_data_email", ""));
        address.setPostCode(settingsData.getString("omomo_settings_order_address_data_post", ""));
        address.setStreet(settingsData.getString("omomo_settings_order_address_data_street_number", ""));
        address.setCity(settingsData.getString("omomo_settings_order_address_data_city", ""));
        address.setPhone(settingsData.getString("omomo_settings_order_address_data_phone", ""));

        sSettings = new Settings();
        sSettings.setDistance(Integer.parseInt(settingsData.getString("omomo_settings_location_distance_value", sContext.getResources().getString(R.string.omomo_default_distance_value))));
        sSettings.setDrawCircle(settingsData.getBoolean("omomo_settings_location_draw_circle", true));
        sSettings.setHighResolutionMapPreview(settingsData.getBoolean("omomo_settings_hr_map_preview", false));

        sSettings.setAddress(address);

        if (getFilter() != null) {
            sSettings.setFilter(getFilter());
        }

        return sSettings;

    }

    /**
     * @return boolean
     */
    public static boolean isActualPositionMode() {
        return sIsActualPositionMode;
    }

    /**
     * @param state
     */
    public static void setActualPositionMode(boolean state) {
        sIsActualPositionMode = state;
    }

    /**
     * @param context
     */
    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(30 * 1024 * 1024); // 30 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

    }

    /**
     * @return Filter
     */
    public static Filter getFilter() {
        return sFilter;
    }

    /**
     * @param filterData
     */
    public static void setFilter(Filter filterData) {
        sFilter = filterData;
    }

    /**
     * @return Context
     */
    public static Context getContext() {
        return sContext;
    }

    public static boolean isInternetConnected() {
        return sIsInternetConnected;
    }

    public static void setIsInternetConnected(boolean sIsInternetConnected) {
        App.sIsInternetConnected = sIsInternetConnected;
    }

    public DisplayImageOptions getDisplayImageOptions() {

        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        sContext = getApplicationContext();
        initImageLoader(sContext);


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
