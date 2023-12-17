package com.ethankisiel.simplefinanceledger.Controllers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.FinanceLedgerApplication;
import com.ethankisiel.simplefinanceledger.Managers.FiltersManager;
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
    CheckBox yearSelectAll;

    @FXML
    ListView<CheckBox> checkbooksList;
    @FXML
    CheckBox checkbookSelectAll;

    @FXML
    ListView<CheckBox> categoriesList;
    @FXML
    CheckBox categorySelectAll;

    @FXML
    ListView<CheckBox> subcategoriesList;
    @FXML
    CheckBox subcategorySelectAll;

    @FXML
    ListView<CheckBox> itemizationsList;
    @FXML
    CheckBox itemizationSelectAll;

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
        HashMap<String, HashMap<String, Boolean>> filters = parentController.filtersManager.currentFilters();

        initializeFilters(yearsList, filters.get(Constants.YEAR_FILTERS), Constants.YEAR_FILTERS);
        initializeFilters(checkbooksList, filters.get(Constants.CHECKBOOK_FILTERS), Constants.CHECKBOOK_FILTERS);
        initializeFilters(categoriesList, filters.get(Constants.CATEGORY_FILTERS), Constants.CATEGORY_FILTERS);
        initializeFilters(subcategoriesList, filters.get(Constants.SUBCATEGORY_FILTERS), Constants.SUBCATEGORY_FILTERS);
        initializeFilters(itemizationsList, filters.get(Constants.ITEMIZATION_FILTERS), Constants.ITEMIZATION_FILTERS);
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
                //parentController.entryFilters.get(columnChanges).put(checkBox.getText(), checkBox.isSelected());
                parentController.filtersManager.updateFilterStatus(columnChanges, checkBox.getText(), checkBox.isSelected());
            }
        }


        //ReadWriteUtil.saveFilters(parentController.entryFilters);
        parentController.updateFilters();
        closeModal();
        return;
    }

    public void selectAllYears()
    {
        if (!pendingChanges.keySet().contains(Constants.YEAR_FILTERS))
        {
            ArrayList<CheckBox> columnChanges = new ArrayList<>();

            pendingChanges.put(Constants.YEAR_FILTERS, columnChanges);
        }

        for (CheckBox checkBox : yearsList.getItems())
        {
            if (checkBox.isSelected() != yearSelectAll.isSelected())
            {
                checkBox.setSelected(yearSelectAll.isSelected());
                pendingChanges.get(Constants.YEAR_FILTERS).add(checkBox);
            }
        }
    }
    public void selectAllCheckbooks()
    {
        if (!pendingChanges.keySet().contains(Constants.CHECKBOOK_FILTERS))
        {
            ArrayList<CheckBox> columnChanges = new ArrayList<>();

            pendingChanges.put(Constants.CHECKBOOK_FILTERS, columnChanges);
        }

        for (CheckBox checkBox : checkbooksList.getItems())
        {
            if (checkBox.isSelected() != checkbookSelectAll.isSelected())
            {
                checkBox.setSelected(checkbookSelectAll.isSelected());
                pendingChanges.get(Constants.CHECKBOOK_FILTERS).add(checkBox);
            }
        }
    }
    public void selectAllCategories()
    {
        if (!pendingChanges.keySet().contains(Constants.CATEGORY_FILTERS))
        {
            ArrayList<CheckBox> columnChanges = new ArrayList<>();

            pendingChanges.put(Constants.CATEGORY_FILTERS, columnChanges);
        }

        for (CheckBox checkBox : categoriesList.getItems())
        {
            if (checkBox.isSelected() != categorySelectAll.isSelected())
            {
                checkBox.setSelected(categorySelectAll.isSelected());
                pendingChanges.get(Constants.CATEGORY_FILTERS).add(checkBox);
            }
        }
    }
    public void selectAllSubcategories()
    {
        if (!pendingChanges.keySet().contains(Constants.SUBCATEGORY_FILTERS))
        {
            ArrayList<CheckBox> columnChanges = new ArrayList<>();

            pendingChanges.put(Constants.SUBCATEGORY_FILTERS, columnChanges);
        }

        for (CheckBox checkBox : subcategoriesList.getItems())
        {
            if (checkBox.isSelected() != subcategorySelectAll.isSelected())
            {
                checkBox.setSelected(subcategorySelectAll.isSelected());
                pendingChanges.get(Constants.SUBCATEGORY_FILTERS).add(checkBox);
            }
        }
    }
    public void selectAllItemizations()
    {
        if (!pendingChanges.keySet().contains(Constants.ITEMIZATION_FILTERS))
        {
            ArrayList<CheckBox> columnChanges = new ArrayList<>();

            pendingChanges.put(Constants.ITEMIZATION_FILTERS, columnChanges);
        }

        for (CheckBox checkBox : itemizationsList.getItems())
        {
            if (checkBox.isSelected() != itemizationSelectAll.isSelected())
            {
                checkBox.setSelected(itemizationSelectAll.isSelected());
                pendingChanges.get(Constants.ITEMIZATION_FILTERS).add(checkBox);
            }
        }
    }

    public void closeModal()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();

        stage.close();
    }
}
