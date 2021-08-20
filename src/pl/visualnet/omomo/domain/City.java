package pl.visualnet.omomo.domain;

public class City {

    private int id;
    private String name;
    private String zip;
    private int repertoireCounter;
    private boolean isOnMap;
    private double latitude = 0.0;
    private double longitude = 0.0;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public int getRepertoireCounter() {
        return this.repertoireCounter;
    }

    public void setRepertoireCounter(int repertoireCounter) {
        this.repertoireCounter = repertoireCounter;
    }

    public boolean getIsOnMap() {
        return this.isOnMap;
    }

    public void setIsOnMap(boolean isOnMap) {
        this.isOnMap = isOnMap;
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

    public boolean hasCoordinates() {
        return ((this.latitude != 0.0) && (this.longitude != 0.0)) ? true : false;
    }

    @Override
    public String toString() {
        return "" + this.id + " " + this.name + " " + this.zip;
    }

}
