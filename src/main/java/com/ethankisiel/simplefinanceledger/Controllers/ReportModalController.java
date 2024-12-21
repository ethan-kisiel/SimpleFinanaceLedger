package com.ethankisiel.simplefinanceledger.Controllers;

import com.ethankisiel.simplefinanceledger.Components.ReportTreeItem;
import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Managers.EntityManager;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import com.ethankisiel.simplefinanceledger.Utils.MoneyUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class ReportModalController implements Initializable
{
    private HashMap<String, HashMap<String, HashMap<String, List<Entry>>>> treeContent = new HashMap<>();
    private LedgerEditorController parentController;

    @FXML
    Button closeButton;

    @FXML
    TreeView<HBox> treeView;

    @FXML
    Label grandTotalLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    private void setTreeContent()
    {
        treeContent = new HashMap<>();

        try
        {
            for (Entry entry: parentController.entityManager.entries)
            {
                treeContent
                        .computeIfAbsent(entry.getCategory(), k -> new HashMap<>())
                        .computeIfAbsent(entry.getSubcategory(), k -> new HashMap<>())
                        .computeIfAbsent(entry.getItemization(), k -> new ArrayList<>())
                        .add(entry);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void initialilizeTreeContent()
    {
        this.setTreeContent();

        int totalAmount = 0;
        ReportTreeItem rootItem = new ReportTreeItem("Grand Total", Constants.EMPTY_STRING);

        ArrayList<String> categoriesList = new ArrayList<>(treeContent.keySet());
        Collections.sort(categoriesList);

        for (String category : categoriesList)
        {
            int categoryTotal = 0;
            ReportTreeItem categoryTreeItem = new ReportTreeItem(category, Constants.EMPTY_STRING);

            ArrayList<String> subcategoriesList = new ArrayList<>(treeContent.get(category).keySet());
            Collections.sort(subcategoriesList);

            for (String subcategory : subcategoriesList)
            {
                int subcategoryTotal = 0;
                ReportTreeItem subcategoryTreeItem = new ReportTreeItem(subcategory, Constants.EMPTY_STRING);

                ArrayList<String> itemizationsList = new ArrayList<>(treeContent.get(category).get(subcategory).keySet());
                Collections.sort(itemizationsList);

                for (String itemization : itemizationsList)
                {
                    int itemizationTotal = 0;
                    ReportTreeItem itemizationTreeItem = new ReportTreeItem(itemization, Constants.EMPTY_STRING);

                    for (Entry entry : treeContent.get(category).get(subcategory).get(itemization))
                    {
                        ReportTreeItem entryTreeItem = new ReportTreeItem(entry.getCheckbook(), MoneyUtil.centsToString(entry.getAmount()));

                        itemizationTotal += entry.getAmount();
                        itemizationTreeItem.getChildren().add(entryTreeItem);
                    }

                    subcategoryTotal += itemizationTotal;
                    itemizationTreeItem.setAmountText(MoneyUtil.centsToString(itemizationTotal));
                    subcategoryTreeItem.getChildren().add(itemizationTreeItem);
                }

                categoryTotal += subcategoryTotal;
                subcategoryTreeItem.setAmountText(MoneyUtil.centsToString(subcategoryTotal));
                categoryTreeItem.getChildren().add(subcategoryTreeItem);
            }

            totalAmount += categoryTotal;
            categoryTreeItem.setAmountText(MoneyUtil.centsToString(categoryTotal));
            rootItem.getChildren().add(categoryTreeItem);
        }

        rootItem.setAmountText(MoneyUtil.centsToString(totalAmount));
        grandTotalLabel.setText(String.format("Grand Total: $%s", MoneyUtil.centsToString((totalAmount))));
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
    }

    public void setParentController(LedgerEditorController parentController)
    {
        this.parentController = parentController;
        initialilizeTreeContent();
    }

    public void closeModal()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
