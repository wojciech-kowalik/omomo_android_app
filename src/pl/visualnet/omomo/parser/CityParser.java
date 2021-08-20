package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.City;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CityParser {

    private int allRepertoireCounter = 0;

    public int getAllRepertoireCounter() {
        return this.allRepertoireCounter;
    }

    public List<City> generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        List<City> cities = new ArrayList<City>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonArray datasets = elements.getAsJsonArray("data");

            for (int i = 0; i < datasets.size(); i++) {

                JsonObject object = datasets.get(i).getAsJsonObject();

                City city = new City();
                city.setId(object.get("id").getAsInt());
                city.setName(object.get("name").getAsString());
                city.setZip(object.get("zip").getAsString());

                if (object.get("latitude").isJsonNull()) {
                    city.setLatitude(0.0);
                } else {
                    city.setLatitude(object.get("latitude").getAsDouble());
                }

                if (object.get("longitude").isJsonNull()) {
                    city.setLongitude(0.0);
                } else {
                    city.setLongitude(object.get("longitude").getAsDouble());
                }

                this.allRepertoireCounter = this.allRepertoireCounter + object.get("repertoire_counter").getAsInt();

                city.setRepertoireCounter(object.get("repertoire_counter").getAsInt());

                cities.add(city);

            }
        }

        return cities;

    }


}
