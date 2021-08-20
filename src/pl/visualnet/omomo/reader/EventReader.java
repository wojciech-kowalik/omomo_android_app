package pl.visualnet.omomo.reader;

import android.util.Log;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.parser.EventFilterNameParser;
import pl.visualnet.omomo.parser.EventParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.net.*;
import java.nio.charset.Charset;
import java.util.List;

public class EventReader {

    private static EventReader instance;

    // static for common constructors data
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private final URL baseUrl;
    private final String charset;

    public EventReader(URL baseUrl) {
        this(baseUrl, Charset.forName("UTF-8").name());
    }

    EventReader(URL baseUrl, String charset) {
        this.baseUrl = baseUrl;
        this.charset = charset;
    }

    public synchronized static EventReader getInstance(String baseUrl) throws MalformedURLException {

        if (instance == null) {
            instance = new EventReader(new URL(baseUrl));
        }

        return instance;
    }

    public List<Event> getEventsNamesByQuery(String name) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/events?name=" + URLEncoder.encode(name, "UTF-8"));

        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);
            EventFilterNameParser parser = new EventFilterNameParser();

            return parser.generate(urlConnection);

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }

    public Event getEvent(int id) throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/events/" + id);

        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);

            EventParser eventParser = new EventParser();
            Event event = eventParser.generate(urlConnection);

            return event;

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }

}
