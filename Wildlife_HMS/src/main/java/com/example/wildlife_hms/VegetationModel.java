package com.example.wildlife_hms;

public class VegetationModel {

    private int id;
    private String vgId;
    private  String name;
    private  String desc;
    private String haId;
    private int habitatId;


    private String habitatName;

    public VegetationModel(int id, String vgId, String name, String desc,int habitatId, String haId,String habitatName) {
        this.id = id;
        this.vgId = vgId;
        this.name = name;
        this.desc = desc;
        this.habitatId=habitatId;
        this.haId = haId;
        this.habitatName = habitatName;
    }

    public VegetationModel(int id, String vgId, String name, String desc, int habitatId) {
        this.id = id;
        this.vgId = vgId;
        this.name = name;
        this.desc = desc;
        this.habitatId = habitatId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVgId() {
        return vgId;
    }

    public void setVgId(String vgId) {
        this.vgId = vgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public void setHaId(String haId) {
        this.haId = haId;
    }

    public int getHabitatId() {
        return habitatId;
    }


    public void setHabitatName(String habitatName) {
        this.habitatName = habitatName;
    }

    public String getHaId() {
        return haId;
    }

    public String getHabitatName() {
        return habitatName;
    }
}
