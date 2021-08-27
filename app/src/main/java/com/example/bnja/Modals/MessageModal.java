package com.example.bnja.Modals;

public class MessageModal {
    String id, msg;
    Long timeStamp;

    public MessageModal(String id, String msg, Long timeStamp) {
        this.id = id;
        this.msg = msg;
        this.timeStamp = timeStamp;
    }

    public MessageModal(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public MessageModal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
