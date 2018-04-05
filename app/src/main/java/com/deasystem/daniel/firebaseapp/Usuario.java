package com.deasystem.daniel.firebaseapp;

/**
 * Created by daniel on 27/10/17.
 */

public class Usuario {

    private String nome;
    private String email;
    private String uid;

    public Usuario(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return nome;
    }
}
