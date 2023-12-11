package com.ethankisiel.simplefinanceledger.Managers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import com.ethankisiel.simplefinanceledger.Models.SortByID;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Entity;

import java.io.IOException;
import java.util.*;

public class EntityManager
{
    public ObservableList<Entry> entries;


    public Set<String> checkbooks;
    public Set<String> categories;
    public Set<String> subcategories;
    public Set<String> itemizations;
    public Set<String> years;

    public ObservableList<String> checkbookOptions;
    public ObservableList<String> categoryOptions;
    public ObservableList<String> subcategoryOptions;
    public ObservableList<String> itemizationOptions;

    // Load in entries
    // Grab existing categories and such from loaded entries

    public EntityManager(List<String> checkbooks, List<String> categories, List<String> subcategories, List<String> itemizations)
    {

        this.checkbooks = new HashSet<>(checkbooks);
        this.categories = new HashSet<>(categories);
        this.subcategories = new HashSet<>(subcategories);
        this.itemizations = new HashSet<>(itemizations);

        checkbookOptions = FXCollections.observableArrayList(this.checkbooks).sorted();
        categoryOptions = FXCollections.observableArrayList(this.categories).sorted();
        subcategoryOptions = FXCollections.observableArrayList(this.subcategories).sorted();
        itemizationOptions = FXCollections.observableArrayList(this.itemizations).sorted();

        try
        {
            entries = FXCollections.observableArrayList(ReadWriteUtil.loadEntries());
        }
        catch (Exception e)
        {
            entries = FXCollections.observableArrayList();
        }
    }

    public void addCheckbook(String checkbook)
    {
        if (checkbooks.add(checkbook))
        {
            checkbookOptions = FXCollections.observableArrayList(checkbooks).sorted();
        }
    }

    public void addCategory(String category)
    {
        if (categories.add(category))
        {
            categoryOptions = FXCollections.observableArrayList(categories).sorted();
        }
    }

    public void addSubcategory(String subcategory)
    {
        if (subcategories.add(subcategory))
        {
            subcategoryOptions = FXCollections.observableArrayList(subcategories).sorted();
        }
    }

    public void addItemization(String itemization)
    {
        if (itemizations.add(itemization))
        {
            itemizationOptions = FXCollections.observableArrayList(itemizations).sorted();
        }
    }

    public void updateAllSelectors()
    {
        checkbooks.clear();
        categories.clear();
        subcategories.clear();
        itemizations.clear();

        for (Entry entry: entries)
        {
            updateSelectors(entry);
        }

        addCheckbook(Constants.CASH_OPTION);

        addCategory(Constants.MISCELLANEOUS_OPTION);
        addCategory(Constants.MISCELLANEOUS_OPTION);
        addItemization(Constants.MISCELLANEOUS_OPTION);
    }

    public void updateSelectors(Entry entry)
    {
        // look thru the different items: Checkbook, Category, Subcategory, Itemization
        // which utilize selectors/reusiability, and if the current selector lists
        // don't contain the items listed, we will add them here

        addCheckbook(entry.getCheckbook());
        //checkbooks.add(entry.getCheckbook());

        addCategory(entry.getCategory());
        //categories.add(entry.getCategory());

        addSubcategory(entry.getSubcategory());
        //subcategories.add(entry.getSubcategory());

        addItemization(entry.getItemization());
        //itemizations.add(entry.getItemization());
    }

    public void addEntry(Entry entry) throws IOException
    {
//        int largestId = entries.get(0).getId();
//        entry.setId(largestId+1);

        entries.add(entry);

        updateSelectors(entry);

        try
        {
            ReadWriteUtil.saveEntries(entries);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    public void updateEntry(Entry entry)
    {
        //entries.sort(Comparator.comparing(Entry::getId).reversed());
        int entryIndex = entries.indexOf(entry);
        entries.set(entryIndex, entry);

        updateSelectors(entry);

        try
        {
            ReadWriteUtil.saveEntries(entries);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        updateAllSelectors();
    }

    public void deleteEntry(Entry entry)
    {
        entries.remove(entry);

        try
        {
            ReadWriteUtil.saveEntries(entries);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        updateAllSelectors();
    }

    public int getNextId()
    {
        entries.sort(new SortByID());

        int lastIndex = entries.size() - 1;

        return entries.get(lastIndex).getId() + 1;
    }
}
