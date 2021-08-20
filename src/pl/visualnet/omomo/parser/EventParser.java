package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.Event;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.utils.HttpUtils;
import pl.visualnet.omomo.utils.RepertoireUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class EventParser {

    public Event generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        Event event = new Event();
        List<Repertoire> repertoires = new ArrayList<Repertoire>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonObject object = elements.getAsJsonObject("data");

            event.setId(object.get("id").getAsInt());
            event.setName(object.get("name").getAsString());

            //if (object.get("system_description").isJsonNull()) {
            //    event.setSystemDescription(null);
            //} else {
            //    event.setSystemDescription(object.get("system_description").getAsString());
            //}

            if (object.get("cms_description").isJsonNull()) {
                event.setCmsDescription(null);
            } else {
                event.setCmsDescription(object.get("cms_description").getAsString());
            }

            JsonArray sets = object.getAsJsonArray("repertoires");

            if (sets.isJsonArray()) {

                for (int i = 0; i < sets.size(); i++) {
                    repertoires.add(RepertoireUtils.getFromJson(sets.get(i).getAsJsonObject()));
                }

            } else {
                repertoires = null;
            }

            event.setRepertoires(repertoires);

        }

        return event;

    }


}
