package pl.visualnet.omomo.reader;

import android.util.Log;
import pl.visualnet.omomo.App;
import pl.visualnet.omomo.domain.Category;
import pl.visualnet.omomo.parser.CategoryParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.net.*;
import java.nio.charset.Charset;
import java.util.List;

public class CategoryReader {

    // static for common constructors data
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private final URL baseUrl;
    private final String charset;
    private int counter = 0;

    public CategoryReader(URL baseUrl) {
        this(baseUrl, Charset.forName("UTF-8").name());
    }

    CategoryReader(URL baseUrl, String charset) {
        this.baseUrl = baseUrl;
        this.charset = charset;
    }

    public List<Category> getCategories() throws Exception {

        URL getUrl = new URL(baseUrl, "api.php/v" + App.API_VERSION + "/categories");

        HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();

        try {

            HttpUtils.configureConnection(urlConnection);
            HttpUtils.handleResponseCode(urlConnection);

            CategoryParser categoryParser = new CategoryParser();
            List<Category> categories = categoryParser.generate(urlConnection);

            return categories;

        } catch (Exception cse) {
            Log.e(App.APPLICATION_NAME, cse.getMessage());
            return null;
        } finally {
            urlConnection.disconnect();
        }

    }
}
