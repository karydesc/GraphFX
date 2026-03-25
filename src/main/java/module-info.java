module com.assign1.assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.incubator.vector;


    opens com.karydesc.GraphFX to javafx.fxml;
    exports com.karydesc.GraphFX;
}