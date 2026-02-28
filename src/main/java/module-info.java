module compil {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.compil to javafx.fxml;
    exports com.example.compil;
}