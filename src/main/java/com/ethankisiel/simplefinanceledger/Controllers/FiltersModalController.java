package com.ethankisiel.simplefinanceledger.Controllers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.FinanceLedgerApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class FiltersModalController implements Initializable
{

    @FXML
    ListView<String> checkbooksList;

    @FXML
    ListView<String> categoriesList;

    @FXML
    ListView<String> subcategoriesList;

    @FXML
    ListView<String> itemizationsList;


    private LedgerEditorController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        return;
    }

    public void setParentController(LedgerEditorController parentController)
    {
        this.parentController = parentController;

        checkbooksList.getItems().addAll(parentController.entryFilters.get(Constants.CHECKBOOK_FILTERS).keySet());
        categoriesList.getItems().addAll(parentController.entryFilters.get(Constants.CATEGORY_FILTERS).keySet());
        subcategoriesList.getItems().addAll(parentController.entryFilters.get(Constants.SUBCATEGORY_FILTERS).keySet());
        itemizationsList.getItems().addAll(parentController.entryFilters.get(Constants.ITEMIZATION_FILTERS).keySet());
    }
}
