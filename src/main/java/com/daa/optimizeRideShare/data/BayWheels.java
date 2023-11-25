package com.daa.optimizeRideShare.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Entity class to represent BayWheels data(Pre-Cleaning)
 */
@Entity
@Table(name = "\"BayWheelsData\"", schema = "public")
public class BayWheels {

    @Id
    @Column(name = "ride_id")
    private String ride_id;

    @Column(name = "rideable_type")
    private String rideable_type;
    @Column(name = "started_at")
    private Date started_at;
    @Column(name = "ended_at")
    private Date ended_at;
    @Column(name = "start_station_name")
    private String start_station_name;
    @Column(name = "start_station_id")
    private String start_station_id;
    @Column(name = "end_station_name")
    private String end_station_name;
    @Column(name = "end_station_id")
    private String end_station_id;


    @Column(name = "start_lat")
    private Double start_lat;
    @Column(name = "start_lng")
    private Double start_lng;
    @Column(name = "end_lat")
    private Double end_lat;
    @Column(name = "end_lng")
    private Double end_lng;

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getRideable_type() {
        return rideable_type;
    }

    public void setRideable_type(String rideable_type) {
        this.rideable_type = rideable_type;
    }

    public Date getStarted_at() {
        return started_at;
    }

    public void setStarted_at(Date started_at) {
        this.started_at = started_at;
    }

    public Date getEnded_at() {
        return ended_at;
    }

    public void setEnded_at(Date ended_at) {
        this.ended_at = ended_at;
    }

    public String getStart_station_name() {
        return start_station_name;
    }

    public void setStart_station_name(String start_station_name) {
        this.start_station_name = start_station_name;
    }

    public String getStart_station_id() {
        return start_station_id;
    }

    public void setStart_station_id(String start_station_id) {
        this.start_station_id = start_station_id;
    }

    public String getEnd_station_name() {
        return end_station_name;
    }

    public void setEnd_station_name(String end_station_name) {
        this.end_station_name = end_station_name;
    }

    public String getEnd_station_id() {
        return end_station_id;
    }

    public void setEnd_station_id(String end_station_id) {
        this.end_station_id = end_station_id;
    }

    public Double getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(Double start_lat) {
        this.start_lat = start_lat;
    }

    public Double getStart_lng() {
        return start_lng;
    }

    public void setStart_lng(Double start_lng) {
        this.start_lng = start_lng;
    }

    public Double getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(Double end_lat) {
        this.end_lat = end_lat;
    }

    public Double getEnd_lng() {
        return end_lng;
    }

    public void setEnd_lng(Double end_lng) {
        this.end_lng = end_lng;
    }

    public String getMember_casual() {
        return member_casual;
    }

    public void setMember_casual(String member_casual) {
        this.member_casual = member_casual;
    }

    @Column(name = "member_casual")
    private String member_casual;
}
