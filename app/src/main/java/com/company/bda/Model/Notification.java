package com.company.bda.Model;

public class Notification {
    String receptorId, envioId, texto, date;

    public Notification() {
    }

    public Notification(String receptorId, String envioId, String texto, String date) {
        this.receptorId = receptorId;
        this.envioId = envioId;
        this.texto = texto;
        this.date = date;
    }

    public String getReceptorId() {
        return receptorId;
    }

    public void setReceptorId(String receptorId) {
        this.receptorId = receptorId;
    }

    public String getEnvioId() {
        return envioId;
    }

    public void setEnvioId(String envioId) {
        this.envioId = envioId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
