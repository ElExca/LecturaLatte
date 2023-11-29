module com.example.restaurant_simulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.LecturaLatte to javafx.fxml;
    exports com.example.LecturaLatte;
    exports com.example.LecturaLatte.view;
    opens com.example.LecturaLatte.view to javafx.fxml;
}