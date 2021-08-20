package pl.visualnet.omomo.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pl.visualnet.omomo.domain.Category;
import pl.visualnet.omomo.utils.HttpUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CategoryParser {

    public List<Category> generate(HttpURLConnection urlConnection) throws IOException {

        String data = HttpUtils.readStreamAndClose(urlConnection.getInputStream());
        List<Category> categories = new ArrayList<Category>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        if (element.isJsonObject()) {

            JsonObject elements = element.getAsJsonObject();
            JsonArray datasets = elements.getAsJsonArray("data");

            for (int i = 0; i < datasets.size(); i++) {

                JsonObject object = datasets.get(i).getAsJsonObject();

                Category category = new Category();
                category.setId(object.get("id").getAsInt());
                category.setName(object.get("name").getAsString());

                categories.add(category);

            }
        }

        return categories;

    }


}
