package com.example.northuniversity.schoolteam.Bean;

public class Message {
    private String  message;//字段需要何服务器接受信息的字段一致
    private  String  room;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String  username;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
