package pl.visualnet.omomo.domain;

public class Settings {

    private int distance;
    private boolean isDrawCircle;
    private boolean isHighResolutionMapPreview;
    private Filter filter;
    private Address address;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isDrawCircle() {
        return isDrawCircle;
    }

    public void setDrawCircle(boolean isDrawCircle) {
        this.isDrawCircle = isDrawCircle;
    }

    public boolean isHighResolutionMapPreview() {
        return isHighResolutionMapPreview;
    }

    public void setHighResolutionMapPreview(boolean isHighResoultionMapPreview) {
        this.isHighResolutionMapPreview = isHighResoultionMapPreview;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "distance=" + distance +
                ", isDrawCircle=" + isDrawCircle +
                '}';
    }
}
