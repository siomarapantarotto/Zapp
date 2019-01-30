package br.com.siomara.zapp.model;

/**
 * Created by 80114369 on 16/07/2018.
 */

public class Message {

    private String userIDSender;
    private String messageDetail;

    public Message() {
    }

    public String getUserIDSender() {
        return userIDSender;
    }

    public void setUserIDSender(String userIDSender) {
        this.userIDSender = userIDSender;
    }

    public String getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
    }
}
