package com.ethankisiel.simplefinanceledger.Controllers;


import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Managers.EntityManager;
import com.ethankisiel.simplefinanceledger.Managers.FiltersManager;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import com.ethankisiel.simplefinanceledger.Models.SortByDate;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;

import com.ethankisiel.simplefinanceledger.Views.FiltersModalView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class LedgerEditorController implements Initializable
{
    private EntityManager entityManager;

    public HashMap<String, HashMap<String, Boolean>> entryFilters;

    private boolean isNewEntry = true; // are we creating a new entry, or editing an existing one?
    private int selectedEntryId;
    private Entry selectedEntry;


    @FXML
    Text changeModeText;

    @FXML
    TableView<Entry> entryTable;
    @FXML
    TableColumn<Entry, String> dateColumn;
    @FXML
    TableColumn<Entry, String> amountColumn;
    @FXML
    TableColumn<Entry, String> checkNumberColumn;
    @FXML
    TableColumn<Entry, String> checkbookColumn;
    @FXML
    TableColumn<Entry, String> categoryColumn;
    @FXML
    TableColumn<Entry, String> subcategoryColumn;
    @FXML
    TableColumn<Entry, String> itemizationColumn;
    @FXML
    TableColumn<Entry, String> notesColumn;

    @FXML
    DatePicker datePicker;

    @FXML
    TextField amountField;

    @FXML
    TextField checkNumberField;

    @FXML
    ChoiceBox<String> checkbookSelector;
    @FXML
    TextField checkbookField;

    @FXML
    ChoiceBox<String> categorySelector;
    @FXML
    TextField categoryField;

    @FXML
    ChoiceBox<String> subcategorySelector;
    @FXML
    TextField subcategoryField;

    @FXML
    ChoiceBox<String> itemizationSelector;
    @FXML
    TextField itemizationField;

    @FXML
    TextArea notesField;

    @FXML
    Button saveButton;
    @FXML
    Button clearButton;
    @FXML
    Button deleteButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        saveButton.setDisable(true);
        deleteButton.setDisable(true);

        ArrayList<String> checkbooks = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> subcategories = new ArrayList<>();
        ArrayList<String> itemizations = new ArrayList<>();


        checkbooks.add(Constants.CASH_OPTION);
        categories.add(Constants.MISCELLANEOUS_OPTION);
        subcategories.add(Constants.MISCELLANEOUS_OPTION);
        itemizations.add(Constants.MISCELLANEOUS_OPTION);

        try
        {

            ArrayList<Entry> entries = (ArrayList<Entry>) ReadWriteUtil.loadEntries();



            for (Entry entry : entries)
            {
                checkbooks.add(entry.getCheckbook());
                categories.add(entry.getCategory());
                subcategories.add(entry.getSubcategory());
                itemizations.add(entry.getItemization());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        entityManager = new EntityManager(checkbooks, categories, subcategories, itemizations);


        checkbookSelector.setItems(entityManager.checkbookOptions);
        //checkbookSelector.setItems(checkbookSelector.getItems().sorted());

        categorySelector.setItems(entityManager.categoryOptions);
        //categorySelector.setItems(categorySelector.getItems().sorted());

        subcategorySelector.setItems(entityManager.subcategoryOptions);
        //subcategorySelector.setItems(subcategorySelector.getItems().sorted());

        itemizationSelector.setItems(entityManager.itemizationOptions);


        entryFilters = FiltersManager.currentFilters(entityManager);

        initializeTable(entityManager.entries);
    }

    public void sortTableEntries()
    {
        entryTable.getItems().sort(new SortByDate());
    }

    public void initializeTable(List<Entry> entries)
    {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("displayDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        checkNumberColumn.setCellValueFactory(new PropertyValueFactory<>("checkNumber"));
        checkbookColumn.setCellValueFactory(new PropertyValueFactory<>("checkbook"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        subcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("subcategory"));
        itemizationColumn.setCellValueFactory(new PropertyValueFactory<>("itemization"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        entryTable.getItems().addAll(entries);

        sortTableEntries();
    }

    public void setEntityManager(EntityManager manager)
    {
        this.entityManager = manager;
    }

    public void updateButtons()
    {
        if (isNewEntry)
        {
            deleteButton.setDisable(true);
        }
    }
    public void updateSelectors()
    {
        checkbookSelector.setItems(entityManager.checkbookOptions);
        categorySelector.setItems(entityManager.categoryOptions);
        subcategorySelector.setItems(entityManager.subcategoryOptions);
        itemizationSelector.setItems(entityManager.itemizationOptions);
    }

    public boolean checkForCompletion()
    {
        boolean isComplete = false;

        boolean isDateValid = datePicker.getValue() != null;
        boolean isAmountValid = !Objects.equals(amountField.getText(), Constants.EMPTY_STRING);
        boolean isCheckNumberValid = !Objects.equals(checkNumberField.getText(), Constants.EMPTY_STRING);

        boolean isCheckbookValid = (checkbookSelector.getValue() != null) || !checkbookField.getText().isEmpty();
        boolean isCategoryValid = (categorySelector.getValue() != null) || !categoryField.getText().isEmpty();
        boolean isSubcategoryValid = (subcategorySelector.getValue() != null) || !subcategoryField.getText().isEmpty();
        boolean isItemizationValid = (itemizationSelector.getValue() != null) || !itemizationField.getText().isEmpty();
        //boolean

        isComplete = (isDateValid && isAmountValid && isCheckNumberValid && isCheckbookValid && isCategoryValid && isSubcategoryValid && isItemizationValid);

        if (isComplete || !isNewEntry)
        {
            saveButton.setDisable(false);
        }
        else
        {
            saveButton.setDisable(true);
        }

        if (!isNewEntry)
        {
            deleteButton.setDisable(false);
            changeModeText.setText(Constants.EDIT_ENTRY);
        }
        else
        {
            deleteButton.setDisable(true);
            changeModeText.setText(Constants.CREATE_ENTRY);
        }

        return isComplete;
    }

    public void softClearFields()
    {
        amountField.setText(Constants.EMPTY_STRING);

        categorySelector.setValue(null);
        categoryField.setText(Constants.EMPTY_STRING);

        subcategorySelector.setValue(null);
        subcategoryField.setText(Constants.EMPTY_STRING);

        itemizationSelector.setValue(null);
        itemizationField.setText(Constants.EMPTY_STRING);

        notesField.setText(Constants.EMPTY_STRING);

        isNewEntry = true;
    }


    public void clearFields()
    {
        datePicker.setValue(null);
        amountField.setText(Constants.EMPTY_STRING);
        checkNumberField.setText(Constants.EMPTY_STRING);

        checkbookSelector.setValue(null);
        checkbookField.setText(Constants.EMPTY_STRING);

        categorySelector.setValue(null);
        categoryField.setText(Constants.EMPTY_STRING);

        subcategorySelector.setValue(null);
        subcategoryField.setText(Constants.EMPTY_STRING);

        itemizationSelector.setValue(null);
        itemizationField.setText(Constants.EMPTY_STRING);

        notesField.setText(Constants.EMPTY_STRING);

        isNewEntry = true;

        checkForCompletion();
    }

    public void entrySelected()
    {
        selectedEntry = entryTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null)
        {
            isNewEntry = false;

            saveButton.setDisable(false);
            deleteButton.setDisable(false);

            datePicker.setValue(selectedEntry.getLocalDate());
            amountField.setText(String.valueOf(selectedEntry.getAmount()));
            checkNumberField.setText(selectedEntry.getCheckNumber());

            checkbookSelector.setValue(selectedEntry.getCheckbook());
            categorySelector.setValue(selectedEntry.getCategory());
            subcategorySelector.setValue(selectedEntry.getSubcategory());
            itemizationSelector.setValue(selectedEntry.getItemization());
            notesField.setText(selectedEntry.getNotes());
        }
    }

    public void saveEntry() throws IOException
    {

        if (checkForCompletion())
        {

            LocalDate date = datePicker.getValue();
            String amount = amountField.getText();
            String checkNumber = checkNumberField.getText();

            String checkbook = checkbookField.getText().isEmpty() ? checkbookSelector.getValue() : checkbookField.getText();
            String category = categoryField.getText().isEmpty() ? categorySelector.getValue() : categoryField.getText();
            String subcategory = subcategoryField.getText().isEmpty() ? subcategorySelector.getValue() : subcategoryField.getText();
            String itemization = itemizationField.getText().isEmpty() ? itemizationSelector.getValue() : itemizationField.getText();
            String notes = notesField.getText();

            Entry entry;

            if (isNewEntry)
            {
                entry = new Entry();
                entry.setId(entityManager.getNextId());
            }
            else
            {
                entry = selectedEntry;
            }

            entry.setDate(date);
            entry.setAmount(Float.parseFloat(amount));
            entry.setCheckNumber(checkNumber);

            entry.setCheckbook(checkbook);
            entry.setCategory(category);
            entry.setSubcategory(subcategory);
            entry.setItemization(itemization);
            entry.setNotes(notes);

            if (isNewEntry)
            {
                entityManager.addEntry(entry);
                entryTable.getItems().add(entry);

                entityManager.updateSelectors(entry);

                softClearFields();
                saveButton.setDisable(true);
            }
            else
            {
                entityManager.updateEntry(entry);
                int index = entryTable.getItems().indexOf(entry);

                entryTable.getItems().set(index, entry);

                entityManager.updateSelectors(entry);

                clearFields();
                saveButton.setDisable(true);
                // update existing entry
            }
        }

        updateSelectors();
        sortTableEntries();
        return;
    }

    public void deleteEntry()
    {
        if (selectedEntry != null)
        {
            entityManager.deleteEntry(selectedEntry);
            entryTable.getItems().remove(selectedEntry);

            clearFields();
            updateSelectors();

            selectedEntry = null;
            isNewEntry = true;

            saveButton.setDisable(true);
            deleteButton.setDisable(true);

        }
        return;
    }





    public void showFilterModal() throws IOException
    {
        try
        {
            FiltersModalView.display(this);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return;
    }
}
