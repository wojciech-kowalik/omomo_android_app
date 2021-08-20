package pl.visualnet.omomo.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ErrorParser {

    public pl.visualnet.omomo.domain.Error generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        pl.visualnet.omomo.domain.Error error = new pl.visualnet.omomo.domain.Error();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject object = element.getAsJsonObject();
            error.setCode(object.get("status").getAsInt());
            error.setMessage(object.get("message").getAsString());
        }

        return error;

    }


}
