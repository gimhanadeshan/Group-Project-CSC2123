package com.example.wildlife_hms;

public class HabitatModel {
    private int id;

    private String haId;
    private String name;
    private String location;
    private  double size;
    private  String desc;

    public HabitatModel(int id,String haId, String name, String location, double size, String desc) {
        this.id = id;
        this.haId=haId;
        this.name = name;
        this.location = location;
        this.size = size;
        this.desc = desc;
    }

    public HabitatModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public HabitatModel() {
    }

    public  int getId() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHaId() {
        return haId;
    }

    public void setHaId(String haId) {
        this.haId = haId;
    }

    @Override
    public String toString() {
        return name;
    }
}
