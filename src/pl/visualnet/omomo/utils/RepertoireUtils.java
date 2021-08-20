package pl.visualnet.omomo.utils;

import com.google.gson.JsonObject;
import pl.visualnet.omomo.domain.Repertoire;

public class RepertoireUtils {

    /**
     * Get repertoire entity from json data
     *
     * @param object JsonObject
     * @return List<Location>
     */
    public static Repertoire getFromJson(JsonObject object) {

        Repertoire repertoire = new Repertoire();

        repertoire.setEventTitle(object.get("event_title").getAsString());
        repertoire.setPlaceName(object.get("place_name").getAsString());
        repertoire.setCityId(object.get("city_id").getAsInt());
        repertoire.setCityName(object.get("city_name").getAsString());
        repertoire.setEventDateHour(object.get("event_date_hour").getAsString());
        repertoire.setEventDateDay(object.get("event_date_day").getAsString());
        repertoire.setEventDateMonth(object.get("event_date_month").getAsString());
        repertoire.setEventDate(object.get("event_date").getAsString());
        repertoire.setSystemUrl(object.get("system_url").getAsString());
        repertoire.setRepertoireId(object.get("repertoire_id").getAsInt());
        repertoire.setRepertoireSystemId(object.get("repertoire_system_id").getAsInt());
        repertoire.setEventId(object.get("event_id").getAsInt());
        repertoire.setEventDateDayName(object.get("event_date_day_name").getAsString());
        repertoire.setPlaceStreetName(object.get("street").getAsString());
        repertoire.setIsFreeTickets(object.get("is_free_tickets").getAsBoolean());

        if (object.get("event_description").isJsonNull()) {
            repertoire.setEventDescription("");
        } else {
            repertoire.setEventDescription(object.get("event_description").getAsString());
        }

        if (object.get("location_institution_name").isJsonNull()) {
            repertoire.setPlaceInstitutionName("");
        } else {
            repertoire.setPlaceInstitutionName(object.get("location_institution_name").getAsString());
        }

        if (object.has("distance")) {
            repertoire.setDistance(object.get("distance").getAsDouble());
        } else {
            repertoire.setDistance(0.0);
        }

        if (object.get("latitude").isJsonNull()) {
            repertoire.setLatitude(0.0);
        } else {
            repertoire.setLatitude(object.get("latitude").getAsDouble());
        }

        if (object.get("longitude").isJsonNull()) {
            repertoire.setLongitude(0.0);
        } else {
            repertoire.setLongitude(object.get("longitude").getAsDouble());
        }

        if (object.get("category_name").isJsonNull()) {
            repertoire.setCategoryName("");
        } else {
            repertoire.setCategoryName(object.get("category_name").getAsString());
        }

        if (object.get("system_image_url").isJsonNull() == false) {
            repertoire.setSystemImageUrl(object.get("system_image_url").getAsString());
        }

        if (object.get("category_image").isJsonNull() == false) {
            repertoire.setCategoryImage(object.get("category_image").getAsString());
        }

        return repertoire;

    }

}
