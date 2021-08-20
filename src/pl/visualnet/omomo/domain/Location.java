package pl.visualnet.omomo.domain;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private int id;
    private int cityId;
    private String name;
    private String street;
    private List<Integer> groupedIds = new ArrayList<Integer>();
    private int ticketSystemId;
    private String ticketSystemName;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private int counter;
    private boolean isOnMap = false;
    private int repertoireCounter;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getGroupedIds() {
        return TextUtils.join(",", groupedIds);
    }

    public void setGroupedIds(int groupedIds) {
        this.groupedIds.add(groupedIds);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getTicketSystemId() {
        return this.ticketSystemId;
    }

    public void setTicketSystemId(int ticketSystemId) {
        this.ticketSystemId = ticketSystemId;
    }

    public String getTicketSystemName() {
        return this.ticketSystemName;
    }

    public void setTicketSystemName(String ticketSystemName) {
        this.ticketSystemName = ticketSystemName;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
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

    public boolean isOnMap() {
        return isOnMap;
    }

    public void setOnMap(boolean isOnMap) {
        this.isOnMap = isOnMap;
    }

    public int getRepertoireCounter() {
        return repertoireCounter;
    }

    public void setRepertoireCounter(int repertoireCounter) {
        this.repertoireCounter = repertoireCounter;
    }

    @Override
    public String toString() {
        return "" + this.id + " " + this.name + " " + this.street + "" + this.getGroupedIds();
    }

}
