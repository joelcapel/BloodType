package com.company.bda.Model;

public class User {
    String nombre, gruposangre,buscar, dni, email, id, profilepictureurl, telefono, type;

    public User() {
    }

    public User(String nombre, String gruposangre, String buscar, String dni, String email, String id, String profilepictureurl, String telefono, String type) {
        this.nombre = nombre;
        this.gruposangre = gruposangre;
        this.buscar = buscar;
        this.dni = dni;
        this.email = email;
        this.id = id;
        this.profilepictureurl = profilepictureurl;
        this.telefono = telefono;
        this.type = type;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGruposangre() {
        return gruposangre;
    }

    public void setGruposangre(String gruposangre) {
        this.gruposangre = gruposangre;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
