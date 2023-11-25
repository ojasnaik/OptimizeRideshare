package com.daa.optimizeRideShare.graph;

import java.util.Objects;

/**
 * Template to represent a BayWheels station
 */
public class BayWheelsNode {

    private String station_id;
    private String station_name;
    private Double station_longitude;
    private Double station_latitude;

    public BayWheelsNode() {
    }

    public BayWheelsNode(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public Double getStation_longitude() {
        return station_longitude;
    }

    public void setStation_longitude(Double station_longitude) {
        this.station_longitude = station_longitude;
    }

    public Double getStation_latitude() {
        return station_latitude;
    }

    public void setStation_latitude(Double station_latitude) {
        this.station_latitude = station_latitude;
    }

    @Override
    public String toString() {
        return station_id + ":" + station_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BayWheelsNode that)) return false;
        return Objects.equals(getStation_id(), that.getStation_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStation_id());
    }
}
