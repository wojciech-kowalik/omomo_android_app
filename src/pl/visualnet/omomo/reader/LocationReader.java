package pl.visualnet.omomo.reader;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.domain.Location;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.domain.Settings;
import pl.visualnet.omomo.parser.LocationParser;
import pl.visualnet.omomo.parser.RepertoireParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.net.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

public class LocationReader {

    private static LocationReader instance;
    private static Properties properties = new Properties();

    // static for common constructors data
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private final URL baseUrl;
    private final String charset;
    private int counter = 0;

    LocationReader(URL baseUrl) {
        this(baseUrl, Charset.forName("UTF-8").name());
    }

    LocationReader(URL baseUrl, String charset) {
        this.baseUrl = baseUrl;
        this.charset = charset;
    }

    public synchronized static LocationReader getInstance(String baseUrl) throws MalformedURLException {

        if (instance == null) {
            instance = new LocationReader(new URL(baseUrl));
        }

        return instance;
    }

    public List<Repertoire> getLocationRepertoires(int cityId, String locationId) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/cities/" + cityId + "/locations/" + locationId + "/repertoires");

        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);

            return new RepertoireParser().generate(urlConnection);

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }

    public List<Location> getLocationsFromActualPositionByDistance(LatLng position, Settings settings) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/locations/lat/" + position.latitude + "/lon/" + position.longitude + "/distance/" + settings.getDistance());
        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);

            return new LocationParser().generate(urlConnection);

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }


}
