package com.ethankisiel.simplefinanceledger.Controllers;


import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Managers.EntityManager;
import com.ethankisiel.simplefinanceledger.Managers.FiltersManager;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import com.ethankisiel.simplefinanceledger.Models.Filter;
import com.ethankisiel.simplefinanceledger.Models.SortByDate;

import com.ethankisiel.simplefinanceledger.Utils.MoneyUtil;
import com.ethankisiel.simplefinanceledger.Utils.PrintUtil;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;

import com.ethankisiel.simplefinanceledger.Utils.ValidationUtil;
import com.ethankisiel.simplefinanceledger.Views.DeleteModalView;
import com.ethankisiel.simplefinanceledger.Views.FiltersModalView;
import com.ethankisiel.simplefinanceledger.Views.ReportModalView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class LedgerEditorController implements Initializable
{
    public EntityManager entityManager;
    public FiltersManager filtersManager;

    public HashMap<String, HashMap<String, Boolean>> entryFilters;

    private boolean isNewEntry = true; // are we creating a new entry, or editing an existing one?
    private int selectedEntryId;
    private Entry selectedEntry;

    private final ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    MenuBar menuBar;

    @FXML
    Text changeModeText;

    @FXML
    Text totalText;

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
        final String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Mac"))
            menuBar.useSystemMenuBarProperty().set(true);


        deleteButton.setDisable(true);

        saveButton.disableProperty().bind(validationSupport.invalidProperty());

        validationSupport.setErrorDecorationEnabled(true);
        validationSupport.registerValidator(datePicker, Validator.createEmptyValidator("Select a date"));
        validationSupport.registerValidator(amountField, (Control c, String newValue) ->
                ValidationResult.fromErrorIf(c, "Enter a valid amount", ValidationUtil.isInvalidAmount(newValue)));

        ValidationUtil.registerBidirectionalValidation(checkbookField, checkbookSelector, validationSupport);
        ValidationUtil.registerBidirectionalValidation(categoryField, categorySelector, validationSupport);
        ValidationUtil.registerBidirectionalValidation(subcategoryField, subcategorySelector, validationSupport);
        ValidationUtil.registerBidirectionalValidation(itemizationField, itemizationSelector, validationSupport);

        ArrayList<String> years = new ArrayList<>();
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

                int entryYear = entry.getLocalDate().getYear();


                years.add(Year.of(entryYear).toString());

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

        entityManager = new EntityManager(years, checkbooks, categories, subcategories, itemizations);
        filtersManager = new FiltersManager(entityManager);


        checkbookSelector.setItems(entityManager.checkbookOptions);
        //checkbookSelector.setItems(checkbookSelector.getItems().sorted());

        categorySelector.setItems(entityManager.categoryOptions);
        //categorySelector.setItems(categorySelector.getItems().sorted());

        subcategorySelector.setItems(entityManager.subcategoryOptions);
        //subcategorySelector.setItems(subcategorySelector.getItems().sorted());

        itemizationSelector.setItems(entityManager.itemizationOptions);


        //entryFilters = FiltersManager.currentFilters(entityManager);

        // allows the individual cells to be selected
//        entryTable.getSelectionModel().cellSelectionEnabledProperty().set(true);

        // INITIALIZE CELL EDITS
        entryTable.setEditable(true);

        dateColumn.setEditable(true);

        checkbookColumn.setEditable(true);

        checkNumberColumn.setEditable(true);

        amountColumn.setEditable(true);

        categoryColumn.setEditable(true);

        subcategoryColumn.setEditable(true);

        itemizationColumn.setEditable(true);




//        checkNumberColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("fieldCheckNumber"));

        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        dateColumn.setOnEditCommit(event -> {
            final String value = event.getNewValue();
            if (value == null || value.isEmpty())
            {
                return;
            }
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            final LocalDate localDate = LocalDate.parse(value, formatter);
            final Date valueDate = Date.from(localDate.atTime(12, 0).toInstant(ZoneOffset.ofTotalSeconds(0)));
            this.datePicker.setValue(valueDate.toInstant().atOffset(ZoneOffset.ofTotalSeconds(0)).toLocalDate());



            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        checkbookColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        checkbookColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            checkbookField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });


        checkNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        checkNumberColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            checkNumberField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        amountColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            amountField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });


        categoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        categoryColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            categoryField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        subcategoryColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        subcategoryColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            subcategoryField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        itemizationColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        itemizationColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            itemizationField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });

        notesColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        notesColumn.setOnEditCommit(event -> {
            final String newValue = event.getNewValue();
            final String value = newValue != null ? newValue : event.getOldValue();

            notesField.setText(value);

            try
            {
                saveEntry();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });


        //entryTable.setEditable(true);

        initializeTable(entityManager.entries);
    }

    public void sortTableEntries()
    {
        entryTable.getItems().sort(new SortByDate());
    }

    public void filterTableEntries()
    {
//        this.entryFilters = FiltersManager.currentFilters()
        entryTable.getItems().setAll(entityManager.entries.filtered(new Predicate<Entry>()
        {
            @Override
            public boolean test(Entry entry)
            {
                Set<String> activeYears = filtersManager.activeFilters(Constants.YEAR_FILTERS);
                Set<String> activeCheckbooks = filtersManager.activeFilters(Constants.CHECKBOOK_FILTERS);
                Set<String> activeCategories = filtersManager.activeFilters(Constants.CATEGORY_FILTERS);
                Set<String> activeSubcategories = filtersManager.activeFilters(Constants.SUBCATEGORY_FILTERS);
                Set<String> activeItemizations = filtersManager.activeFilters(Constants.ITEMIZATION_FILTERS);

                //String entryYear = Year.of(entry.getLocalDate().getYear()).toString();
                boolean validYear = activeYears.contains(entry.getYear());
                boolean validCheckbook = activeCheckbooks.contains(entry.getCheckbook());
                boolean validCategory = activeCategories.contains(entry.getCategory());
                boolean validSubcategory = activeSubcategories.contains(entry.getSubcategory());
                boolean validItemization = activeItemizations.contains(entry.getItemization());


                return (validYear && validCheckbook && validCategory && validSubcategory && validItemization);
            }
        }));
    }

    public void initializeTable(List<Entry> entries)
    {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("displayDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("displayAmount"));
        checkNumberColumn.setCellValueFactory(new PropertyValueFactory<>("checkNumber"));
        checkbookColumn.setCellValueFactory(new PropertyValueFactory<>("checkbook"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        subcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("subcategory"));
        itemizationColumn.setCellValueFactory(new PropertyValueFactory<>("itemization"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        entryTable.getItems().addAll(entries);

        filterTableEntries();
        sortTableEntries();
        updateTotal();
    }

    public void updateSelectors()
    {
        checkbookSelector.setItems(entityManager.checkbookOptions);
        categorySelector.setItems(entityManager.categoryOptions);
        subcategorySelector.setItems(entityManager.subcategoryOptions);
        itemizationSelector.setItems(entityManager.itemizationOptions);
    }

    public void updateFilters()
    {
        filtersManager.saveFilters();

        filterTableEntries();
        sortTableEntries();
    }

    public void updateTotal()
    {
        int total = 0;

        for (Entry entry : entryTable.getItems())
        {
            total += entry.getAmount();
        }

        totalText.setText(MoneyUtil.centsToString(total));
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


        isComplete = (isDateValid && isAmountValid && isCheckbookValid && isCategoryValid && isSubcategoryValid && isItemizationValid);

//        if (isComplete || !isNewEntry)
//        {
//            saveButton.setDisable(false);
//        }
//        else
//        {
//            saveButton.setDisable(true);
//        }

        if (isNewEntry)
        {
            deleteButton.setDisable(true);
            changeModeText.setText(Constants.CREATE_ENTRY);
            saveButton.setText("Create");
        }
        else
        {
            deleteButton.setDisable(false);
            changeModeText.setText(Constants.EDIT_ENTRY);
            saveButton.setText("Save");
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
            clearFields();

            isNewEntry = false;
            deleteButton.setDisable(false);

            datePicker.setValue(selectedEntry.getLocalDate());
            amountField.setText(selectedEntry.getDisplayAmount());
            checkNumberField.setText(selectedEntry.getCheckNumber());

            checkbookSelector.setValue(selectedEntry.getCheckbook());
            checkbookField.setText(selectedEntry.getCheckbook());

            categorySelector.setValue(selectedEntry.getCategory());
            categoryField.setText(selectedEntry.getCategory());

            subcategorySelector.setValue(selectedEntry.getSubcategory());
            subcategoryField.setText(selectedEntry.getSubcategory());

            itemizationSelector.setValue(selectedEntry.getItemization());
            itemizationField.setText(selectedEntry.getItemization());

            notesField.setText(selectedEntry.getNotes());

            checkForCompletion();
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
            entry.setAmount((Float.parseFloat(amount)));
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

                entityManager.addSelectorsFromEntry(entry);
                filtersManager.addFiltersFromEntry(entry);

                softClearFields();
                //saveButton.setDisable(true);
            }
            else
            {
                entityManager.updateEntry(entry);
                int index = entryTable.getItems().indexOf(entry);

                entryTable.getItems().set(index, entry);

                entityManager.addSelectorsFromEntry(entry);
                filtersManager.addFiltersFromEntry(entry);

                clearFields();
                //saveButton.setDisable(true);
                // update existing entry
            }
        }


        //FiltersManager.updateFilter()
        updateSelectors();
        sortTableEntries();
        updateTotal();
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
            sortTableEntries();
            updateTotal();

            selectedEntry = null;
            isNewEntry = true;

            //saveButton.setDisable(true);
            deleteButton.setDisable(true);

        }
        return;
    }


    public void showFilterModal() throws IOException
    {
        updateFilters();
        try
        {
            //System.out.println(shit);
            FiltersModalView.display(this);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return;
    }

    public void showReportModal()
    {
        try
        {
            ReportModalView.display(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //System.out.println("REPORT MODAL");
    }

    public void showDeleteModal()
    {
        try
        {
            DeleteModalView.display(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
