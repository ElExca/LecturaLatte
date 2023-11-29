package com.example.LecturaLatte.view;

import com.example.LecturaLatte.models.*;
import com.example.LecturaLatte.observadores.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Observable;
import java.util.Observer;


public class scene implements Observer{

    //@FXML
    //private ImageView enterDinerImageView;
    private ClienteMonitor clienteMonitor;

    private ExitMonitor exitMonitor;

    private LecturaLatte lecturaLatte;

    private Barista barista;

    private final Color EmptySpaceColor=Color.web("#232424");


    private final Color MeseroColor =Color.web("#f87b72");

    private final Color BaristaColor =Color.web("#7dc0ff");

    @FXML
    private Button btn_start;

    @FXML
    private Rectangle enter_waitress;


    @FXML
    private Rectangle add_command;

    @FXML
    private Rectangle cooking_command;

    @FXML
    private Rectangle deliver_command;

    @FXML
    private Rectangle enter_diner;

    @FXML
    private Rectangle exit;

    @FXML
    private Rectangle exit_door;

    @FXML
    private HBox queue_wait;

    @FXML
    private Rectangle receive_command;

    @FXML
    private GridPane tables;

    @FXML
    private Rectangle wait_command;
    @FXML
    public void initialize() {
        this.lecturaLatte = new LecturaLatte();
        this.clienteMonitor = new ClienteMonitor(10, this.lecturaLatte);
        this.barista = new Barista(this.lecturaLatte);
        this.exitMonitor = new ExitMonitor(this.lecturaLatte);
        initSimulation();
    }

    @FXML
    void onClickedStart(MouseEvent event) {
        initSimulation();
    }

    private void initSimulation(){
        ObservadorConsumirColaEnEspera observadorConsumirColaEnEspera = new ObservadorConsumirColaEnEspera(this.clienteMonitor);
        ProducirColaEnEspera producirColaEnEspera = new ProducirColaEnEspera(this.clienteMonitor);
        ObservadorConsumirOrdenes observadorConsumirOrdenes = new ObservadorConsumirOrdenes(this.barista);
        ObservadorColaDeSalida ObservadorColaDeSalida = new ObservadorColaDeSalida(this.exitMonitor);

        observadorConsumirColaEnEspera.addObserver(this);
        producirColaEnEspera.addObserver(this);
        observadorConsumirOrdenes.addObserver(this);
        ObservadorColaDeSalida.addObserver(this);

        Thread consumeCommandsThread= new Thread(observadorConsumirOrdenes);
        consumeCommandsThread.setDaemon(true);
        Thread produceQueueWaitThread = new Thread(producirColaEnEspera);
        produceQueueWaitThread.setDaemon(true);
        Thread consumeQueueWaitThread = new Thread(observadorConsumirColaEnEspera);
        consumeQueueWaitThread.setDaemon(true);
        Thread ConsumeExitQueueThread= new Thread(ObservadorColaDeSalida);
        ConsumeExitQueueThread.setDaemon(true);

        consumeCommandsThread.start();
        ConsumeExitQueueThread.start();
        produceQueueWaitThread.start();
        consumeQueueWaitThread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        switch (Integer.valueOf(String.valueOf(o))) {
            case 1:
                addDinerToQueueWait();
                break;
            case 2:
                enterDinerToEntrace();
                waitSecond(2);
                sitDinerAtSomeTable();
                makeOrder();
                break;
            case 3:
                eatTimer();
                break;
            case 4:
                leaveDiner();
                break;
        }
    }

    private void leaveDiner(){
        Cliente cliente =exitMonitor.removeFromExitQueue();
        StackPane stackPane= getTable(cliente.getTableId());
        Rectangle rectangle=(Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            text.setText("-");
            text.setFill(Color.WHITE);
            rectangle.setFill(EmptySpaceColor);
            exit_door.setFill(cliente.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit_door.setFill(EmptySpaceColor);
            exit.setFill(cliente.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit.setFill(EmptySpaceColor);
        });

    }

    private void eatTimer(){
        deliverCommand();
        waitSecond(1);
        Cliente cliente = barista.getOrders().remove();
        StackPane stackPane=getTable(cliente.getTableId());
        Text text = (Text) stackPane.getChildren().get(1);
        Timeline timeline = new Timeline();
        EventHandler<ActionEvent> eventHandler = event -> {
            Platform.runLater(()->{
                text.setText(String.valueOf(cliente.getTime()));
            });
            if (cliente.getTime() == 0) {
                Platform.runLater(()->{
                    text.setText("");
                });
                timeline.stop();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), eventHandler);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        countdown(cliente);
        waitSecond(1);
        CheckCommands();
    }

    private void CheckCommands(){
        Platform.runLater(()->{
            wait_command.setFill(BaristaColor);
            cooking_command.setFill(BaristaColor);
            deliver_command.setFill(EmptySpaceColor);
        });
    }

    private void deliverCommand(){
        Platform.runLater(()->{
            deliver_command.setFill(BaristaColor);
            cooking_command.setFill(EmptySpaceColor);
            wait_command.setFill(EmptySpaceColor);
        });
    }

    private void countdown(Cliente cliente){
        TiempoDeComer counterEatDiner=new TiempoDeComer(this.exitMonitor, cliente);
        Thread CounterToEatThread=new Thread(counterEatDiner);
        CounterToEatThread.setDaemon(true);
        CounterToEatThread.start();
    }
    private void makeOrder(){
        ProducirOrden producirOrden =new ProducirOrden(this.barista);
        Thread produceCommandThread=new Thread(producirOrden);
        produceCommandThread.setDaemon(true);
        produceCommandThread.start();
        workChef();
    }

    private void workChef(){
        Platform.runLater(()->{
            wait_command.setFill(EmptySpaceColor);
            deliver_command.setFill(EmptySpaceColor);
            cooking_command.setFill(BaristaColor);
        });
    }

    private void enterDinerToEntrace(){
        //String imagePath = "/assets/customer_image.png";
        Rectangle popDiner= (Rectangle) queue_wait.getChildren().get(0);
        Platform.runLater(()->{
            enter_diner.setFill(popDiner.getFill());
            queue_wait.getChildren().remove(popDiner);
        });
    }
    private void sitDinerAtSomeTable(){
        for(Node hboxNode:tables.getChildren()){
            VBox hbox=(VBox) hboxNode;
            Rectangle waitress = (Rectangle) hbox.getChildren().get(0);
            StackPane stackPane= (StackPane) hbox.getChildren().get(1);
            Rectangle diner = (Rectangle) stackPane.getChildren().get(0);
            if(EmptySpaceColor.equals(diner.getFill())){
                sitDiner(waitress, stackPane);
                waitSecond(1);
                waitresReturnEntrace(waitress);
                break;
            }
        }
    }

    private void sitDiner( Rectangle waitress,StackPane stackPane){
        Rectangle diner = (Rectangle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            enter_waitress.setFill(EmptySpaceColor);
            waitress.setFill(MeseroColor);
            diner.setFill(enter_diner.getFill());
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Arial", FontWeight.LIGHT, 10));
            text.setText("Esperando");
            enter_diner.setFill(EmptySpaceColor);
        });
    }

    private void waitresReturnEntrace(Rectangle waitress){
        Platform.runLater(()->{
            enter_waitress.setFill(MeseroColor);
            waitress.setFill(EmptySpaceColor);
        });
    }
    private void addDinerToQueueWait(){
        Cliente newCliente =this.clienteMonitor.getQueue_wait().getLast();
        Rectangle square = new Rectangle(50, 50, newCliente.getColor());
        square.setArcHeight(30);
        square.setArcWidth(30);
        Platform.runLater(()->{queue_wait.getChildren().add(square);});
    }

    private void waitSecond(int second){
        int milliseconds= second*1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private StackPane getTable(int id){
        Node hboxNode=tables.getChildren().get(id);
        VBox hbox=(VBox) hboxNode;
        return  (StackPane) hbox.getChildren().get(1);
    }
}
