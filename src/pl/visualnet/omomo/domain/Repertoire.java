package pl.visualnet.omomo.domain;

public class Repertoire {

    private int id;
    private int repertoireId;
    private int repertoireSystemId;
    private int eventId;
    private String eventTitle;
    private String placeName;
    private String placeInstitutionName;
    private String categoryName;
    private String categoryImage;
    private int cityId;
    private String cityName;
    private String placeStreetName;
    private String eventDateHour;
    private String eventDateDay;
    private String eventDateDayName;
    private String eventDateMonth;
    private String eventDate;
    private String eventDescription;
    private String systemUrl;
    private String systemImageUrl;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double distance;
    private boolean isFreeTickets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setRepertoireId(int repertoireId) {
        this.repertoireId = repertoireId;
    }

    public int getRepertoireSystemId() {
        return repertoireSystemId;
    }

    public void setRepertoireSystemId(int repertoireSystemId) {
        this.repertoireSystemId = repertoireSystemId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setEventDateHour(String eventDateHour) {
        this.eventDateHour = eventDateHour;
    }

    public void setEventDateDay(String eventDateDay) {
        this.eventDateDay = eventDateDay;
    }

    public void setEventDateMonth(String eventDateMonth) {
        this.eventDateMonth = eventDateMonth;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEventDateDayName() {
        return eventDateDayName;
    }

    public void setEventDateDayName(String eventDateDayName) {
        this.eventDateDayName = eventDateDayName;
    }

    public String getPlaceStreetName() {
        return placeStreetName;
    }

    public void setPlaceStreetName(String placeStreetName) {
        this.placeStreetName = placeStreetName;
    }

    public String getPlaceInstitutionName() {
        return placeInstitutionName;
    }

    public void setPlaceInstitutionName(String placeInstitutionName) {
        this.placeInstitutionName = placeInstitutionName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getSystemImageUrl() {
        return systemImageUrl;
    }

    public void setSystemImageUrl(String systemImageUrl) {
        this.systemImageUrl = systemImageUrl;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public boolean isFreeTickets() {
        return isFreeTickets;
    }

    public void setIsFreeTickets(boolean isFreeTickets) {
        this.isFreeTickets = isFreeTickets;
    }
}
