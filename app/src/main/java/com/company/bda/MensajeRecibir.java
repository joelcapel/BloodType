package com.company.bda;

import com.company.bda.Model.Mensaje;

public class MensajeRecibir extends Mensaje {
    private Long hora;

    public MensajeRecibir(){
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String nombre, String fotoPerfil, String typeMensaje, Long hora) {
        super(mensaje, nombre, fotoPerfil, typeMensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
