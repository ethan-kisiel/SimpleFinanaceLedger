package com.ethankisiel.simplefinanceledger.Managers;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Utils.ReadWriteUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FiltersManager
{
    public static HashMap<String, HashMap<String, Boolean>>
    updateFilter(String filter, Set<String> availableOptions, HashMap<String, HashMap<String, Boolean>> filters)
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

        return filters;
    }

    public static HashMap<String, HashMap<String, Boolean>> currentFilters(EntityManager entityManager)
    {
        HashMap<String, HashMap<String, Boolean>> filters = ReadWriteUtil.loadFilters();
        // need to add years

        filters = updateFilter(Constants.YEAR_FILTERS, entityManager.years, filters);
        filters = updateFilter(Constants.CHECKBOOK_FILTERS, entityManager.checkbooks, filters);
        filters = updateFilter(Constants.CATEGORY_FILTERS, entityManager.categories, filters);
        filters = updateFilter(Constants.SUBCATEGORY_FILTERS, entityManager.subcategories, filters);
        filters = updateFilter(Constants.ITEMIZATION_FILTERS, entityManager.itemizations, filters);


        return filters;
    }

    public static Set<String> activeFilters(HashMap<String, HashMap<String, Boolean>> allFilters, String filterType)
    {
        Set<String> activeFilters = new HashSet<>();

        for (String filter : allFilters.get(filterType).keySet())
        {
            if (allFilters.get(filterType).get(filter))
            {
                activeFilters.add(filter);
            }
        }

        return activeFilters;
    }
}
