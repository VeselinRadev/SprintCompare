package com.veselin.sprintcompare.models;

import java.util.HashMap;

public class Run {
    private String topSpeed;
    private String avgSpeed;
    private String time;
    private String distance;
    private String id;
    private String userID;
    public Run(){

    }
    public Run(String topSpeed, String time, String distance, String id){
        this.topSpeed = topSpeed;
        this.time = time;
        this.distance = distance;
        this.avgSpeed = String.format( "%.2f",Double.parseDouble(distance)/Double.parseDouble(time));
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(String topSpeed) {
        this.topSpeed = topSpeed;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public HashMap<String, String> getHashMap(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id", this.id);
        hashMap.put("topSpeed", this.topSpeed);
        hashMap.put("time", this.time);
        hashMap.put("distance", this.distance);
        hashMap.put("avgSpeed", this.avgSpeed);
        return hashMap;
    }
    public HashMap<String, String> getHashMapWithUId(){
        HashMap<String, String> hashMap = getHashMap();
        hashMap.put("userID", this.userID);
        return hashMap;
    }
}
