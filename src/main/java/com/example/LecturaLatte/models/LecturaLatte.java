package com.example.LecturaLatte.models;

import com.example.LecturaLatte.models.estados.EstadoCliente;
import com.example.LecturaLatte.models.estados.EstadoMesa;

import java.util.Arrays;

public class LecturaLatte {
    private Mesa[] mesas;

    public LecturaLatte() {
        this.mesas = new Mesa[20];
        for (int i = 0; i < this.mesas.length; i++) {
            this.mesas[i] = new Mesa(null);
        }
    }

    public boolean isFull(){
        System.out.println(Arrays.toString(this.mesas));
        for (Mesa mesa : this.mesas) {
            if (EstadoMesa.VACIA.equals(mesa.getState()))
                return false;
        }
        return true;
    }

    public void setData(Cliente cliente){
        for (int i = 0; i < this.mesas.length; i++) {
            if(EstadoMesa.VACIA.equals(this.mesas[i].getState())) {
                cliente.setTableId(i);
                this.mesas[i].setDiner(cliente);
                this.mesas[i].setState(EstadoMesa.OCUPADA);
                return;
            }
        }
    }

    public Cliente getDinnerByState(EstadoCliente state){
        for (int i = 0; i < this.mesas.length; i++) {
            if(EstadoMesa.OCUPADA.equals(this.mesas[i].getState())) {
                if(this.mesas[i].getDiner().getState().equals(state)){
                    System.out.println("encontre1: "+this.mesas[i].toString());
                    return  this.mesas[i].getDiner();
                }
            }
        }
        return null;
    }

    public void removeDinnerByTableId(int id){
        this.mesas[id].setDiner(null);
        this.mesas[id].setState(EstadoMesa.VACIA);
    }


}
