package br.com.siomara.zapp.model;

/**
 * Created by 80114369 on 19/04/2018.
 */

public class Contact {

    private String id;
    private String name;
    private String email;

    public Contact() {
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
