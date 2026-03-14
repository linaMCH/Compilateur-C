module compil {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.compil to javafx.fxml;
    exports com.example.compil;
}