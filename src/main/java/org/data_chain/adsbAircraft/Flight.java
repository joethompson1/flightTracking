package org.data_chain.adsbAircraft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

    private String hex;
    private String type;
    private String flight;
    private String registration;
    private String aircraftType;
    private Integer altitudeBaro;
    private String squawk;
    private String emergency;
    private String category;

    @JsonProperty("track")
    private Double track;

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lon")
    private Double longitude;

    @JsonProperty("nic")
    private Integer nic;

    @JsonProperty("rc")
    private Integer rc;

    @JsonProperty("messages")
    private Integer messages;

    @JsonProperty("dst")
    private Double dst;

    @JsonProperty("dir")
    private Double dir;



    @JsonProperty("hex")
    void setHex(String hex) {
        this.hex = hex.trim();
    }

    @JsonProperty("type")
    void setType(String type) {
        this.type = type.trim();
    }

    @JsonProperty("flight")
    void setFlight(String flight) {
        this.flight = flight.trim();
    }

    @JsonProperty("r")
    void setRegistration(String registration) {
        this.registration = registration.trim();
    }

    @JsonProperty("t")
    void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType.trim();
    }


    @JsonProperty("alt_baro")
    void setAltitudeBaro(Object altBaro) {
        if (altBaro instanceof Integer) {
            this.altitudeBaro = (Integer) altBaro;
        } else {
            this.altitudeBaro = 0;
        }
    }

    @JsonProperty("squawk")
    void setSquawk(String squawk) {
        this.squawk = squawk.trim();
    }

    @JsonProperty("emergency")
    void setEmergency(String emergency) {
        this.emergency = emergency.trim();
    }

    @JsonProperty("category")
    void setCategory(String category) {
        this.category = category.trim();
    }


    public String hex() {
        return this.hex;
    }

    public String type() {
        return this.type;
    }

    public String flight() {
        return this.flight;
    }

    public String registration() {
        return this.registration;
    }

    public String aircraftType() {
        return this.aircraftType;
    }

    public Integer altitudeBaro() {
        return this.altitudeBaro;
    }

    public Double track() {
        return this.track;
    }

    public String squawk() {
        return this.squawk;
    }

    public String emergency() {
        return this.emergency;
    }

    public String category() {
        return this.category;
    }

    public Double latitude() {
        return this.latitude;
    }

    public Double longitude() {
        return this.longitude;
    }

    public Integer nic() {
        return this.nic;
    }

    public Integer rc() {
        return this.rc;
    }

    public Integer messages() {
        return this.messages;
    }

    public Double dst() {
        return this.dst;
    }

    public Double dir() {
        return this.dir;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "hex='" + hex + '\'' +
                ", type='" + type + '\'' +
                ", flight='" + flight + '\'' +
                ", registration='" + registration + '\'' +
                ", aircraftType='" + aircraftType + '\'' +
                ", altitudeBaro=" + altitudeBaro +
                ", track=" + track +
                ", squawk='" + squawk + '\'' +
                ", emergency='" + emergency + '\'' +
                ", category='" + category + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nic=" + nic +
                ", rc=" + rc +
                ", messages=" + messages +
                ", dst=" + dst +
                ", dir=" + dir +
                '}';
    }
}
