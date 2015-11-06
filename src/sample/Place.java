package sample;

import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Place {
    private StringProperty placeName;
    private StringProperty placeArea;
    private String address;
    private String phoneNumber;
    private String minCharge;
    private String placeRating;
    private String doWeLikeIt;

    public Place(StringProperty placeName, StringProperty placeArea) {
        setName(placeName);
        setArea(placeArea);
    }

    public Place() {
    }

    public void setName(StringProperty placeName) {
        this.placeName = placeName;
    }

    public void setArea(StringProperty placeArea) {
        this.placeArea = placeArea;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMinCharge(String minCharge) {
        this.minCharge = minCharge;
    }

    public void setPlaceRating(String placeRating) {
        this.placeRating = placeRating;
    }

    public void setDoWeLikeIt(String doWeLikeIt) {
        this.doWeLikeIt = doWeLikeIt;
    }
    @XmlJavaTypeAdapter(Adapter.class)
    public StringProperty getName() {
        return placeName;
    }
    @XmlJavaTypeAdapter(Adapter.class)
    public StringProperty getArea() {
        return this.placeArea;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getMinCharge() {
        return this.minCharge;
    }

    public String getPlaceRating() {
        return this.placeRating;
    }

    public String getDoWeLikeIt() {
        return this.doWeLikeIt;
    }
}
