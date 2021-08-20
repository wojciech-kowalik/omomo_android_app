package pl.visualnet.omomo.utils;

import pl.visualnet.omomo.exception.ReaderException;
import pl.visualnet.omomo.parser.ErrorParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HttpUtils {

    public static void configureConnection(HttpURLConnection urlConnection) {

        urlConnection.setReadTimeout(8000);
        try {
            urlConnection.setRequestMethod("GET");
        } catch (Exception e) {
            // suppress exception
        }

        urlConnection.setRequestProperty("Content-length", "0");
        //urlConnection.setRequestProperty("Accept-Encoding", "gzip");
        urlConnection.setUseCaches(false);
        urlConnection.setAllowUserInteraction(false);

    }

    public static void handleResponseCode(HttpURLConnection urlConnection) throws IOException {

        if (urlConnection.getResponseCode() != 200) {
            throw new ReaderException(new ErrorParser().generate(urlConnection));
        }
    }

    public static String readStreamAndClose(InputStream is) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } finally {
            is.close();
        }
    }
}
