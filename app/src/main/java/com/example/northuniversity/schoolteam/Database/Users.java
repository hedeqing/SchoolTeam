package com.example.northuniversity.schoolteam.Database;

import org.litepal.crud.LitePalSupport;

public class Users extends LitePalSupport {
    private  String  id;
    private String name;
    private String gender;
    private  String number;
    private  String qq;
    private  String chatNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getChatNum() {
        return chatNum;
    }

    public void setChatNum(String chatNum) {
        this.chatNum = chatNum;
    }

    public String getDynamic() {
        return dynamic;
    }

    public void setDynamic(String dynamic) {
        this.dynamic = dynamic;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    private String dynamic;
    private int teamID;

}
