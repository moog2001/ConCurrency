module com.example.concurrency {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.concurrency to javafx.fxml;
    exports com.example.concurrency;
}