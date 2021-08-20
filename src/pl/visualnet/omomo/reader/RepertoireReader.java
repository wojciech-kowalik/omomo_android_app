package pl.visualnet.omomo.reader;

import android.util.Log;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.domain.Filter;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.domain.Settings;
import pl.visualnet.omomo.parser.RepertoireParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RepertoireReader {

    public static final int LIMIT = 40;
    private static RepertoireReader instance;

    // static for common constructors data
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private final URL baseUrl;
    private final String charset;
    private int counter = 0;

    RepertoireReader(URL baseUrl) {
        this(baseUrl, Charset.forName("UTF-8").name());
    }

    RepertoireReader(URL baseUrl, String charset) {
        this.baseUrl = baseUrl;
        this.charset = charset;
    }

    public synchronized static RepertoireReader getInstance(String baseUrl) throws MalformedURLException {

        if (instance == null) {
            instance = new RepertoireReader(new URL(baseUrl));
        }

        return instance;
    }

    public List<Repertoire> getRepertoires(Settings settings) throws Exception {

        StringBuilder url = new StringBuilder();
        url.append("api.php/v" + App.API_VERSION + "/repertoires");
        url.append("?limit=" + LIMIT);

        setFilterData(settings.getFilter(), url);

        URL getUrl = new URL(baseUrl, url.toString());

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

    public List<Repertoire> getRepertoiresFromActualPositionByDistance(Settings settings) throws Exception {

        if (App.sActualPosition == null) {
            return new ArrayList<Repertoire>();
        }

        StringBuilder url = new StringBuilder();
        url.append("api.php/v" + App.API_VERSION + "/repertoires/lat/" + App.sActualPosition.latitude + "/lon/" + App.sActualPosition.longitude + "/distance/" + settings.getDistance());
        url.append("?limit=" + LIMIT);

        setFilterData(settings.getFilter(), url);

        URL getUrl = new URL(baseUrl, url.toString());

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

    private void setFilterData(Filter filter, StringBuilder url) {

        if (filter != null) {

            if (filter.getDateFrom(false) != null) {
                url.append("&from=" + filter.getDateFrom(false));
            }

            if (filter.getDateTo(false) != null) {
                url.append("&to=" + filter.getDateTo(false));
            }

            if (filter.getEventName() != null) {
                try {
                    url.append("&event=" + URLEncoder.encode(filter.getEventName(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.e(App.APPLICATION_NAME, "event encode exception");
                }
            }

            if (filter.getCity() != null) {
                url.append("&city_id=" + filter.getCity().getId());
            }

            if (filter.getCategory() != null) {
                url.append("&category_id=" + filter.getCategory().getId());
            }

        }

    }


}
