package com.company.bda;

import com.company.bda.Model.Mensaje;

import java.util.Map;

public class MensajeEnviar extends Mensaje {
    private Map hora;

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String s, String toString, Map hora) {
        super();
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String typeMensaje, Map hora) {
        super(mensaje, nombre, fotoPerfil, typeMensaje);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String typeMensaje, String urlFoto, Map hora) {
        super(mensaje, nombre, fotoPerfil, typeMensaje, urlFoto);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
