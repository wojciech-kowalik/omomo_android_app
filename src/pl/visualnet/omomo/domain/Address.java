package pl.visualnet.omomo.domain;

public class Address {

    private String name;
    private String surname;
    private String email;
    private String street;
    private String postCode;
    private String city;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFullFilled() {

        if (getName().isEmpty() && getSurname().isEmpty() &&
                getEmail().isEmpty() && getCity().isEmpty() && getPhone().isEmpty()) {

            return false;
        }

        //for (Field f : this.getClass().getDeclaredFields()) {
        //    f.setAccessible(true);
        //    try {
        //
        //        if (f.get(this).toString() == "") {
        //            return false;
        //        }
        //    } catch (IllegalAccessException ignored) {
        //    }
        //}

        return true;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
