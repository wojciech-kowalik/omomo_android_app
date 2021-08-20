package pl.visualnet.omomo.reader;

import android.util.Log;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.domain.Location;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.parser.CityParser;
import pl.visualnet.omomo.parser.LocationParser;
import pl.visualnet.omomo.parser.RepertoireParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.net.*;
import java.nio.charset.Charset;
import java.util.List;

public class CityReader {

    // static for common constructors data
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private final URL baseUrl;
    private final String charset;
    private int counter = 0;

    public CityReader(URL baseUrl) {
        this(baseUrl, Charset.forName("UTF-8").name());
    }

    CityReader(URL baseUrl, String charset) {
        this.baseUrl = baseUrl;
        this.charset = charset;
    }

    public int getCounter() {
        return this.counter;
    }

    public List<City> getCities() throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/cities");

        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);

            CityParser cityParser = new CityParser();
            List<City> cities = cityParser.generate(urlConnection);

            //this.counter = cityParser.getAllRepertoireCounter();

            return cities;

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }

    public List<Location> getCityLocations(int cityId) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/cities/" + cityId + "/locations");

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

    public List<Repertoire> getCityRepertoires(int cityId) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/cities/" + cityId + "/repertoires");

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

}
