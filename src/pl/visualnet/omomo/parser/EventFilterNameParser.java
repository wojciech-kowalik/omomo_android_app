package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class EventFilterNameParser {

    public List<Event> generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        List<Event> events = new ArrayList<Event>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonArray datasets = elements.getAsJsonArray("data");

            for (int i = 0; i < datasets.size(); i++) {

                JsonObject object = datasets.get(i).getAsJsonObject();

                Event event = new Event();
                event.setId(object.get("id").getAsInt());
                event.setName(object.get("name").getAsString());
                events.add(event);
            }
        }

        return events;

    }

}
