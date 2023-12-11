package com.ethankisiel.simplefinanceledger.Views;

import com.ethankisiel.simplefinanceledger.Controllers.FiltersModalController;
import com.ethankisiel.simplefinanceledger.Controllers.LedgerEditorController;
import com.ethankisiel.simplefinanceledger.FinanceLedgerApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FiltersModalView
{
    public static void display(LedgerEditorController parentController) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FinanceLedgerApplication.class.getResource("FiltersModal.fxml"));

        Stage window = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        ((FiltersModalController) fxmlLoader.getController()).setParentController(parentController);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Configure Filters");

        window.setScene(scene);

        window.showAndWait();
    }
}
