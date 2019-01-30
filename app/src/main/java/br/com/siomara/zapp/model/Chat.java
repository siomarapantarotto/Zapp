package br.com.siomara.zapp.model;

/**
 * Created by 80114369 on 24/07/2018.
 */

public class Chat {

    private String userId;
    private String name;
    private String message;

    public Chat() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
