package com.ethankisiel.simplefinanceledger.Utils;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.w3c.dom.Text;

import java.io.Console;

public class ValidationUtil
{

    public static boolean isInvalidAmount(String amount)
    {
        try
        {
            String amountFloat = ((Float) Float.parseFloat(amount)).toString();

            int decimalIndex = amount.indexOf('.');

            if (decimalIndex != -1)
            {
                int decimalPlaces = amountFloat.length() - decimalIndex - 1;
                if(decimalPlaces > 2)
                {
                    System.out.println(amountFloat);
                    return true;
                }
            }

        } catch (Exception e)
        {
            return true;
        }

        return false;
    }


    public static void registerBidirectionalValidation(TextField textField, ChoiceBox<String> choiceBox, ValidationSupport validationSupport)
    {
        textField.setText("");
        choiceBox.setValue("");

        validationSupport.registerValidator(choiceBox, (Control c, String newValue) -> {

            validationSupport.registerValidator(textField, (Control c1, String newValue1) -> {

                validationSupport.redecorate();

                return ValidationResult.fromErrorIf(c1, "Select or enter a value",
                        ValidationUtil.isInvalidSelectorOrTextField(choiceBox, textField));
            });

            return ValidationResult.fromErrorIf(c, "Select or enter a value",
                    ValidationUtil.isInvalidSelectorOrTextField(choiceBox, textField));
        });

        validationSupport.registerValidator(textField, (Control c, String newValue) -> {

            validationSupport.registerValidator(choiceBox, (Control c1, String newValue1) -> {

                validationSupport.redecorate();

                return ValidationResult.fromErrorIf(c1, "Select or enter a value",
                        ValidationUtil.isInvalidSelectorOrTextField(choiceBox, textField));
            });

            return ValidationResult.fromErrorIf(c, "Select or enter a value",
                    ValidationUtil.isInvalidSelectorOrTextField(choiceBox, textField));
        });

        return;
    }
    public static boolean isInvalidSelectorOrTextField(ChoiceBox<String> selectField, TextField textField)
    {
        String selectFieldData = selectField.getValue();
        String textFieldData = textField.getText();

//        System.out.println(selectFieldData);
//        System.out.println(textFieldData);

        if (selectFieldData != null)
        {
            if (!selectFieldData.isEmpty())
            {
                return false;
            }
        }
        if (textFieldData != null)
        {
            if (!textFieldData.isEmpty())
            {
                return false;
            }
        }

        return true;
    }
}
