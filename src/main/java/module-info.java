module de.medieninformatik {
    requires javafx.controls;
    requires javafx.fxml;


    opens de.medieninformatik to javafx.fxml;
    exports de.medieninformatik;
}