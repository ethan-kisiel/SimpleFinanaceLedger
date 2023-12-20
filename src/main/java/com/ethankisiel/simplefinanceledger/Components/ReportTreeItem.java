package com.ethankisiel.simplefinanceledger.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.w3c.dom.Text;

public class ReportTreeItem extends TreeItem<HBox>
{
    Label itemLabel;
    Label amountLabel;
    public ReportTreeItem(String itemString, String amountString)
    {
        super();


        itemLabel = new Label(itemString);
        amountLabel = new Label(("$ " + amountString));

        amountLabel.setFont(new Font(18));
        amountLabel.setPadding(new Insets(0, 10, 0, 0));
        itemLabel.setFont(new Font(20));

        HBox container = new HBox();
        VBox itemContainer = new VBox();
        VBox amountContainer = new VBox();


        container.setAlignment(Pos.CENTER);

        itemContainer.setAlignment(Pos.CENTER_LEFT);
        itemContainer.setFillWidth(true);

        HBox.setHgrow(itemLabel, Priority.ALWAYS);
        itemLabel.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(itemContainer, Priority.ALWAYS);
        //container.getChildren().add(itemLabel);

        amountContainer.setAlignment(Pos.CENTER_RIGHT);
        amountContainer.setFillWidth(true);
        HBox.setHgrow(amountLabel, Priority.ALWAYS);
        amountLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(amountContainer, Priority.ALWAYS);
        //container.getChildren().add(amountLabel);

        HBox.setHgrow(container, Priority.ALWAYS);


        itemContainer.getChildren().add(itemLabel);
        amountContainer.getChildren().add(amountLabel);


        container.getChildren().addAll(itemContainer, amountContainer);

        this.setValue(container);
    }

    public void setAmountText(String amountString)
    {
        amountLabel.setText(("$ " + amountString));
    }
}
