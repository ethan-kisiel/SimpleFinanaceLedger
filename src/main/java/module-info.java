module com.ethankisiel.simplefinanceledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires json.simple;


    opens com.ethankisiel.simplefinanceledger to javafx.fxml;


    exports com.ethankisiel.simplefinanceledger;
    exports com.ethankisiel.simplefinanceledger.Controllers;
    exports com.ethankisiel.simplefinanceledger.Managers;
    exports com.ethankisiel.simplefinanceledger.Models;

    opens com.ethankisiel.simplefinanceledger.Controllers to javafx.fxml;
}
