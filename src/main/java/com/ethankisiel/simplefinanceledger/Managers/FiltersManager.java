package com.ethankisiel.simplefinanceledger.Managers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import com.ethankisiel.simplefinanceledger.Models.Filter;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;

import java.io.IOException;
import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FiltersManager
{

    private void removeItems(Set<String> options, String filterType)
    {
        // This is safe
        filters.get(filterType).keySet().removeIf(key -> !options.contains(key));
    }
    private HashMap<String, HashMap<String, Boolean>> filters;

    public FiltersManager(EntityManager entityManager)
    {
        filters = ReadWriteUtil.loadFilters();

        removeItems(entityManager.years, Constants.YEAR_FILTERS);
        removeItems(entityManager.checkbooks, Constants.CHECKBOOK_FILTERS);
        removeItems(entityManager.categories, Constants.CATEGORY_FILTERS);
        removeItems(entityManager.subcategories, Constants.SUBCATEGORY_FILTERS);
        removeItems(entityManager.itemizations, Constants.ITEMIZATION_FILTERS);

        for (String year : entityManager.years)
        {
            if (!filters.get(Constants.YEAR_FILTERS).containsKey(year))
            {
                filters.get(Constants.YEAR_FILTERS).put(year, true);
            }
        }
        for (String checkbook : entityManager.checkbooks)
        {
            if (!filters.get(Constants.CHECKBOOK_FILTERS).containsKey(checkbook))
            {
                filters.get(Constants.CHECKBOOK_FILTERS).put(checkbook, true);
            }
        }
        for (String category : entityManager.categories)
        {
            if (!filters.get(Constants.CATEGORY_FILTERS).containsKey(category))
            {
                filters.get(Constants.CATEGORY_FILTERS).put(category, true);
            }
        }
        for (String subcategory : entityManager.subcategories)
        {
            if (!filters.get(Constants.SUBCATEGORY_FILTERS).containsKey(subcategory))
            {
                filters.get(Constants.SUBCATEGORY_FILTERS).put(subcategory, true);
            }
        }
        for (String itemization : entityManager.itemizations)
        {
            if (!filters.get(Constants.ITEMIZATION_FILTERS).containsKey(itemization))
            {
                filters.get(Constants.ITEMIZATION_FILTERS).put(itemization, true);
            }
        }

        saveFilters();
    }

    public void saveFilters()
    {
        try
        {
            ReadWriteUtil.saveFilters(filters);
        }
        catch( IOException e)
        {
            e.printStackTrace();
        }
    }
    public void addFiltersFromEntry(Entry entry)
    {
        String year = entry.getYear();
        String checkbook = entry.getCheckbook();
        String category = entry.getCategory();
        String subcategory = entry.getSubcategory();
        String itemization = entry.getItemization();

        if (!filters.get(Constants.YEAR_FILTERS).containsKey(year))
        {
            filters.get(Constants.YEAR_FILTERS).put(year, true);
        }
        if (!filters.get(Constants.CHECKBOOK_FILTERS).containsKey(checkbook))
        {
            filters.get(Constants.CHECKBOOK_FILTERS).put(checkbook, true);
        }
        if (!filters.get(Constants.CATEGORY_FILTERS).containsKey(category))
        {
            filters.get(Constants.CATEGORY_FILTERS).put(category, true);
        }
        if (!filters.get(Constants.SUBCATEGORY_FILTERS).containsKey(subcategory))
        {
            filters.get(Constants.SUBCATEGORY_FILTERS).put(subcategory, true);
        }
        if (!filters.get(Constants.ITEMIZATION_FILTERS).containsKey(itemization))
        {
            filters.get(Constants.ITEMIZATION_FILTERS).put(itemization, true);
        }

        saveFilters();
    }

    public void updateFilterStatus(String filterType, String filter, Boolean isEnabled)
    {
        filters.get(filterType).put(filter, isEnabled);
    }
    public static HashMap<String, HashMap<String, Boolean>>
    updateFilter(String filter, Set<String> availableOptions, HashMap<String, HashMap<String, Boolean>> filters)
    {
        // Remove filter option if there are no entries which contain those filters
        // add a new filter option if that option is present in available options, but not in the filters
        try
        {
            for (String option : filters.get(filter).keySet())
            {
                if (!availableOptions.contains(option))
                {
                    filters.get(filter).remove(option);
                }
            }

            for (String option : availableOptions)
            {
                if (!filters.get(filter).containsKey(option))
                {
                    filters.get(filter).put(option, true);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return filters;
    }


    public HashMap<String, HashMap<String, Boolean>> currentFilters()
    {
//        HashMap<String, HashMap<String, Boolean>> filters = ReadWriteUtil.loadFilters();
        // need to add years

//        filters = updateFilter(Constants.YEAR_FILTERS, entityManager.years, filters);
//        filters = updateFilter(Constants.CHECKBOOK_FILTERS, entityManager.checkbooks, filters);
//        filters = updateFilter(Constants.CATEGORY_FILTERS, entityManager.categories, filters);
//        filters = updateFilter(Constants.SUBCATEGORY_FILTERS, entityManager.subcategories, filters);
//        filters = updateFilter(Constants.ITEMIZATION_FILTERS, entityManager.itemizations, filters);


        return filters;
    }

    public Set<String> activeFilters(String filterType)
    {
        Set<String> activeFilters = new HashSet<>();

        for (String filter : filters.get(filterType).keySet())
        {
            // if the given filter is set to true
            if (filters.get(filterType).get(filter))
            {
                activeFilters.add(filter);
            }
        }

        return activeFilters;
    }
}
