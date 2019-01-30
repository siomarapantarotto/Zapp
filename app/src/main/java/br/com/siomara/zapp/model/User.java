package br.com.siomara.zapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.siomara.zapp.config.FirebaseConfiguration;

/**
 * Created by siomara.com.br on 05/04/2018.
 */

public class User {

    private String id;
    private String name;
    private String email;
    private String password;

    // No argument constructor is required by Firebase
    public User() {
    }

    public void save() {
        DatabaseReference firebasereference = FirebaseConfiguration.getFirebaseIntance();
        firebasereference.child("users").child(getId()).setValue(this);
    }

    @Exclude // Firebase não vai salvar essa info em duplicado
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

    @Exclude // Firebase não vai salvar pois já está na autenticação
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
