package pl.visualnet.omomo.domain;


import pl.visualnet.omomo.utils.CategoryParcel;
import pl.visualnet.omomo.utils.CityParcel;
import pl.visualnet.omomo.utils.CommonUtils;

public class Filter {

    private String dateFrom;
    private String dateTo;
    private String eventName;
    private CityParcel city;
    private CategoryParcel category;

    public String getDateFrom(boolean convert) {

        if (convert) return CommonUtils.convertFilterDate(dateFrom);
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo(boolean convert) {

        if (convert) return CommonUtils.convertFilterDate(dateTo);
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public CityParcel getCity() {
        return city;
    }

    public void setCity(CityParcel city) {
        this.city = city;
    }

    public CategoryParcel getCategory() {
        return category;
    }

    public void setCategory(CategoryParcel category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "dateFrom='" + dateFrom + '\'' +
                ", dateTo='" + dateTo + '\'' +
                ", eventName='" + eventName + '\'' +
                ", city=" + city +
                ", category=" + category +
                '}';
    }
}
