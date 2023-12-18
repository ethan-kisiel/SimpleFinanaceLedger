package com.ethankisiel.simplefinanceledger.Utils;

import com.ethankisiel.simplefinanceledger.Constants;
import com.ethankisiel.simplefinanceledger.Models.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadWriteUtil
{
    public static void saveEntries(List<Entry> entries) throws IOException
    {
        JSONArray jsonArray = new JSONArray();
        for (Entry entry : entries)
        {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", entry.getId());
            jsonObject.put("date", entry.getDateString());
            jsonObject.put("amount", entry.getAmount());
            jsonObject.put("checkbook", entry.getCheckbook());
            jsonObject.put("checkNumber", entry.getCheckNumber());
            jsonObject.put("category", entry.getCategory());
            jsonObject.put("subcategory", entry.getSubcategory());
            jsonObject.put("itemization", entry.getItemization());
            jsonObject.put("notes", entry.getNotes());

            jsonArray.add(jsonObject);
        }


        try (FileWriter fileWriter = new FileWriter(Constants.OBJECTS_PATH))
        {
            fileWriter.write(JSONStringFormatter.formatJSONString(FormattedJSONArray.toJSONString(jsonArray)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return;
    }

    public static List<Entry> loadEntries() throws IOException, ParseException
    {
        List<Entry> entries = new ArrayList<Entry>();

        JSONParser parser = new JSONParser();

        try
        {
            JSONArray entriesArray = (JSONArray) parser.parse(new FileReader(Constants.OBJECTS_PATH));


            for (Object obj : entriesArray)
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) obj;

                    Entry entry = new Entry();

                    entry.setId(((Long) jsonObject.get("id")).intValue());
                    entry.setDate((String) jsonObject.get("date"));
                    entry.setAmount(((Double) jsonObject.get("amount")).floatValue());
                    entry.setCheckbook((String) jsonObject.get("checkbook"));
                    entry.setCheckNumber((String) jsonObject.get("checkNumber"));
                    entry.setCategory((String) jsonObject.get("category"));
                    entry.setSubcategory((String) jsonObject.get("subcategory"));
                    entry.setItemization((String) jsonObject.get("itemization"));
                    entry.setNotes((String) jsonObject.get("notes"));

                    entries.add(entry);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return entries;
    }


    public static HashMap<String, HashMap<String, Boolean>> loadFilters()
    {
        HashMap<String, HashMap<String, Boolean>> filters = new HashMap<>();

        JSONParser parser = new JSONParser();

        try
        {
            JSONObject jsonFilters = (JSONObject) parser.parse(new FileReader(Constants.FILTERS_PATH));

            HashMap<String, Boolean> yearFilters = (HashMap<String, Boolean>) jsonFilters.get(Constants.YEAR_FILTERS);
            HashMap<String, Boolean> checkbookFilters = (HashMap<String, Boolean>) jsonFilters.get(Constants.CHECKBOOK_FILTERS);
            HashMap<String, Boolean> categoryFilters = (HashMap<String, Boolean>) jsonFilters.get(Constants.CATEGORY_FILTERS);
            HashMap<String, Boolean> subcategoryFilters = (HashMap<String, Boolean>) jsonFilters.get(Constants.SUBCATEGORY_FILTERS);
            HashMap<String, Boolean> itemizationFilters = (HashMap<String, Boolean>) jsonFilters.get(Constants.ITEMIZATION_FILTERS);

            filters.put(Constants.YEAR_FILTERS, yearFilters);
            filters.put(Constants.CHECKBOOK_FILTERS, checkbookFilters);
            filters.put(Constants.CATEGORY_FILTERS, categoryFilters);
            filters.put(Constants.SUBCATEGORY_FILTERS, subcategoryFilters);
            filters.put(Constants.ITEMIZATION_FILTERS, itemizationFilters);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return filters;
    }

    public static void saveFilters(Map<String, HashMap<String, Boolean>> filters) throws IOException
    {

        JSONObject jsonObject = new JSONObject(filters);


        try (FileWriter fileWriter = new FileWriter(Constants.FILTERS_PATH))
        {
            fileWriter.write(JSONStringFormatter.formatJSONString(jsonObject.toJSONString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}
