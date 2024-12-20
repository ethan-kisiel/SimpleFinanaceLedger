package com.ethankisiel.simplefinanceledger.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteModalController implements Initializable
{

    private LedgerEditorController parentController;

    @FXML
    Button cancelButton;
    @FXML
    Button deleteButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        return;
    }

    public void setParentController(LedgerEditorController parentController)
    {
        this.parentController = parentController;
    }

    public void deletePressed()
    {
        this.parentController.deleteEntry();
        closeModal();
    }

    public void cancelPressed()
    {
        closeModal();
    }

    public void closeModal()
    {
        Stage stage = (Stage) cancelButton.getScene().getWindow();

        stage.close();
    }
}
