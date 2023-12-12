package com.ethankisiel.simplefinanceledger.Controllers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.FinanceLedgerApplication;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.synedra.validatorfx.Check;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FiltersModalController implements Initializable
{

    @FXML
    Button saveButton;

    @FXML
    Button closeButton;

    @FXML
    ListView<CheckBox> yearsList;

    @FXML
    ListView<CheckBox> checkbooksList;

    @FXML
    ListView<CheckBox> categoriesList;

    @FXML
    ListView<CheckBox> subcategoriesList;

    @FXML
    ListView<CheckBox> itemizationsList;

    private HashMap<String, ArrayList<CheckBox>> pendingChanges = new HashMap<>();

    private LedgerEditorController parentController;

    public void initializeFilters(ListView<CheckBox> listView, HashMap<String, Boolean> filters, String column)
    {
        for (String filter : filters.keySet())
        {
            Boolean isChecked = filters.get(filter);
            CheckBox checkbox = new CheckBox(filter);
            checkbox.setAllowIndeterminate(false);
            checkbox.setSelected(isChecked);
            checkbox.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent)
                {
                    if (pendingChanges.keySet().contains(column))
                    {
                        pendingChanges.get(column).add(checkbox);
                    }
                    else
                    {
                        ArrayList<CheckBox> columnChanges = new ArrayList<>();
                        columnChanges.add(checkbox);

                        pendingChanges.put(column, columnChanges);
                    }
                }
            });

            listView.getItems().add(checkbox);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        return;
    }

    public void setParentController(LedgerEditorController parentController)
    {
        this.parentController = parentController;


        initializeFilters(yearsList, parentController.entryFilters.get(Constants.YEAR_FILTERS), Constants.YEAR_FILTERS);
        initializeFilters(checkbooksList, parentController.entryFilters.get(Constants.CHECKBOOK_FILTERS), Constants.CHECKBOOK_FILTERS);
        initializeFilters(categoriesList, parentController.entryFilters.get(Constants.CATEGORY_FILTERS), Constants.CATEGORY_FILTERS);
        initializeFilters(subcategoriesList, parentController.entryFilters.get(Constants.SUBCATEGORY_FILTERS), Constants.SUBCATEGORY_FILTERS);
        initializeFilters(itemizationsList, parentController.entryFilters.get(Constants.ITEMIZATION_FILTERS), Constants.ITEMIZATION_FILTERS);

//        for (String filter : parentController.entryFilters.get(Constants.CHECKBOOK_FILTERS).keySet())
//        {
//            Boolean isChecked = parentController.entryFilters.get(Constants.CHECKBOOK_FILTERS).get(filter);
//            CheckBox checkbox = new CheckBox(filter);
//            checkbox.setAllowIndeterminate(false);
//            checkbox.setSelected(isChecked);
//
//            checkbooksList.getItems().add(checkbox);
//        }

//        checkbooksList.getItems().addAll(parentController.entryFilters.get(Constants.CHECKBOOK_FILTERS).keySet());
//        categoriesList.getItems().addAll(parentController.entryFilters.get(Constants.CATEGORY_FILTERS).keySet());
//        subcategoriesList.getItems().addAll(parentController.entryFilters.get(Constants.SUBCATEGORY_FILTERS).keySet());
//        itemizationsList.getItems().addAll(parentController.entryFilters.get(Constants.ITEMIZATION_FILTERS).keySet());
    }


    public void saveFilters()
    {
        if (pendingChanges.size() == 0)
        {
            closeModal();
            return;
        }

        for (String columnChanges : pendingChanges.keySet())
        {
            for (CheckBox checkBox : pendingChanges.get(columnChanges))
            {
                parentController.entryFilters.get(columnChanges).put(checkBox.getText(), checkBox.isSelected());
            }
        }


        //ReadWriteUtil.saveFilters(parentController.entryFilters);
        parentController.updateFilters();
        closeModal();
        return;
    }

    public void closeModal()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();

        stage.close();
    }
}
