package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.Repertoire;
import pl.visualnet.omomo.utils.HttpUtils;
import pl.visualnet.omomo.utils.RepertoireUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class RepertoireParser {

    public List<Repertoire> generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        List<Repertoire> repertoires = new ArrayList<Repertoire>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonArray datasets = elements.getAsJsonArray("data");

            for (int i = 0; i < datasets.size(); i++) {
                repertoires.add(RepertoireUtils.getFromJson(datasets.get(i).getAsJsonObject()));
            }
        }

        return repertoires;

    }


}
