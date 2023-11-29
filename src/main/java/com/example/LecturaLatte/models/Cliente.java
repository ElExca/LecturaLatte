package com.example.LecturaLatte.models;
import com.example.LecturaLatte.models.estados.EstadoCliente;
import javafx.scene.paint.Color;

public class Cliente {
    private EstadoCliente state;
    private int id;
    private  int tableId;
    private Color color;
    private int time;

    public Cliente(int id){
        this.id=id;
        this.time=0;
        this.color=GenerateColorRandom();
        this.state= EstadoCliente.ESPERAR;
        this.tableId=-1;
    }

    private Color GenerateColorRandom(){
        int rangR = (int)(Math.random() * (255 - 130)) + 130;
        int rangG = (int)(Math.random() * (255 - 130)) + 130;
        int rangB = (int)(Math.random() * (255 - 130)) + 130;
        return Color.rgb(rangR, rangG, rangB);
    }

    public Color getColor() {
        return color;
    }

    public int getTime() {
        return time;
    }

    public EstadoCliente getState() {
        return state;
    }

    public void setState(EstadoCliente state) {
        this.state = state;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void decrementTime(){
        this.time--;
    }
}
