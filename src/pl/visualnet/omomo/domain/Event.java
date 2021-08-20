package pl.visualnet.omomo.domain;

import java.util.List;

public class Event {

    private int id;
    private String name;
    private String image;
    private String systemDescription;
    private String cmsDescription;
    private List<Repertoire> repertoires;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSystemDescription() {
        return systemDescription;
    }

    public void setSystemDescription(String systemDescription) {
        this.systemDescription = systemDescription;
    }

    public String getCmsDescription() {
        return cmsDescription;
    }

    public void setCmsDescription(String cmsDescription) {
        this.cmsDescription = cmsDescription;
    }

    public List<Repertoire> getRepertoires() {
        return repertoires;
    }

    public void setRepertoires(List<Repertoire> repertoires) {
        this.repertoires = repertoires;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", systemDescription='" + systemDescription + '\'' +
                ", cmsDescription='" + cmsDescription + '\'' +
                ", repertoires=" + repertoires +
                '}';
    }
}
