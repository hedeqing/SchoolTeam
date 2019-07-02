package com.example.northuniversity.schoolteam.Bean;

public class Message {
    private String  message;//字段需要何服务器接受信息的字段一致
    private  String  room;
    private  String fromName;

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getRoom() {
        return room;
    }

    public  Message(){}
    public Message(String message, String room, String fromName) {
        this.message = message;
        this.room = room;
        this.fromName = fromName;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFromName() {
        return fromName;
    }


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
