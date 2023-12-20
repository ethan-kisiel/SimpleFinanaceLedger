package com.ethankisiel.simplefinanceledger.Controllers;

import com.ethankisiel.simplefinanceledger.Components.ReportTreeItem;
import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Managers.EntityManager;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ReportModalController implements Initializable
{
    private HashMap<String, HashMap<String, HashMap<String, List<Entry>>>> treeContent = new HashMap<>();
    private LedgerEditorController parentController;

    @FXML
    Button closeButton;

    @FXML
    TreeView<HBox> treeView;


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

        float totalAmount = 0.0f;
        ReportTreeItem rootItem = new ReportTreeItem("Grand Total", Constants.EMPTY_STRING);

        for (String category : treeContent.keySet())
        {
            float categoryTotal = 0.0f;
            ReportTreeItem categoryTreeItem = new ReportTreeItem(category, Constants.EMPTY_STRING);


            for (String subcategory : treeContent.get(category).keySet())
            {
                float subcategoryTotal = 0.0f;
                ReportTreeItem subcategoryTreeItem = new ReportTreeItem(subcategory, Constants.EMPTY_STRING);

                for (String itemization : treeContent.get(category).get(subcategory).keySet())
                {
                    float itemizationTotal = 0.0f;
                    ReportTreeItem itemizationTreeItem = new ReportTreeItem(itemization, Constants.EMPTY_STRING);

                    for (Entry entry : treeContent.get(category).get(subcategory).get(itemization))
                    {
                        ReportTreeItem entryTreeItem = new ReportTreeItem(entry.getCheckbook(), Constants.formattedFloatAsMoney(entry.getAmount()));

                        itemizationTotal += entry.getAmount();
                        itemizationTreeItem.getChildren().add(entryTreeItem);
                    }

                    subcategoryTotal += itemizationTotal;
                    itemizationTreeItem.setAmountText(Constants.formattedFloatAsMoney(itemizationTotal));
                    subcategoryTreeItem.getChildren().add(itemizationTreeItem);
                }

                categoryTotal += subcategoryTotal;
                subcategoryTreeItem.setAmountText(Constants.formattedFloatAsMoney(subcategoryTotal));
                categoryTreeItem.getChildren().add(subcategoryTreeItem);
            }

            totalAmount += categoryTotal;
            categoryTreeItem.setAmountText(Constants.formattedFloatAsMoney(categoryTotal));
            rootItem.getChildren().add(categoryTreeItem);
        }

        rootItem.setAmountText(Constants.formattedFloatAsMoney(totalAmount));
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(true);
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
