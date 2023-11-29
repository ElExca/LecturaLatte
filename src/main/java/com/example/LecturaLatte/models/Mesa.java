package com.example.LecturaLatte.models;

import com.example.LecturaLatte.models.estados.EstadoMesa;

public class Mesa {
    private Cliente cliente;
    private EstadoMesa state;

    public Mesa(Cliente cliente) {
        this.cliente = cliente;
        this.state= EstadoMesa.VACIA;
    }

    public Cliente getDiner() {
        return cliente;
    }

    public void setDiner(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoMesa getState() {
        return state;
    }

    public void setState(EstadoMesa state) {
        this.state = state;
    }

}
