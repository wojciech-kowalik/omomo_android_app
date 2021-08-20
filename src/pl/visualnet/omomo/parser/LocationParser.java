package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.Location;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class LocationParser {

    public List<Location> generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        List<Location> locations = new ArrayList<Location>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonArray datasets = elements.getAsJsonArray("data");

            for (int i = 0; i < datasets.size(); i++) {

                JsonObject object = datasets.get(i).getAsJsonObject();

                Location location = new Location();
                location.setId(object.get("id").getAsInt());
                location.setCityId(object.get("city_id").getAsInt());
                location.setName(object.get("name").getAsString());
                location.setStreet(object.get("street").getAsString());
                location.setTicketSystemId(object.get("ticket_system_id").getAsInt());
                location.setTicketSystemName(object.get("ticket_system_name").getAsString());

                if (object.get("repertoire_counter") != null) {
                    location.setRepertoireCounter(object.get("repertoire_counter").getAsInt());
                }

                if (object.get("latitude").isJsonNull()) {
                    location.setLatitude(0.0);
                } else {
                    location.setLatitude(object.get("latitude").getAsDouble());
                }

                if (object.get("longitude").isJsonNull()) {
                    location.setLongitude(0.0);
                } else {
                    location.setLongitude(object.get("longitude").getAsDouble());
                }

                locations.add(location);

            }
        }

        return locations;

    }


}
