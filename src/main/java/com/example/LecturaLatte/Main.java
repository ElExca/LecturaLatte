package com.example.LecturaLatte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("simulator-view.fxml"));
        //Cargando Escena
        Scene scene = new Scene(fxmlLoader.load(), 1000, 900);
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("simulator_view.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}