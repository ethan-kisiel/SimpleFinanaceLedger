package com.ethankisiel.simplefinanceledger;

public class Constants
{
    public static String EMPTY_STRING = "";
    public static String OBJECTS_PATH = System.getProperty("user.dir") + "/saved/data/entries.json";
    public static String FILTERS_PATH = System.getProperty("user.dir") + "/saved/data/filters.json";
    public static String ICON_PATH = "file:" + System.getProperty("user.dir") + "/saved/icons/appicon.png";

    public static String NOT_APPLICABLE_OPTION = "N/A";
    public static String MISCELLANEOUS_OPTION = "Misc";
    public static String CASH_OPTION = "Cash";

    public static String EDIT_ENTRY = "Edit Entry";
    public static String CREATE_ENTRY = "Create Entry";



    public static String YEAR_FILTERS = "YEAR_FILTERS";
    public static String CHECKBOOK_FILTERS = "CHECKBOOK_FILTERS";
    public static String CATEGORY_FILTERS = "CATEGORY_FILTERS";
    public static String SUBCATEGORY_FILTERS = "SUBCATEGORY_FILTERS";
    public static String ITEMIZATION_FILTERS = "ITEMIZATION_FILTERS";

    public static String formattedFloatAsMoney(float amount)
    {
        String floatString = Float.toString(amount);
        String dollars = floatString.split("\\.")[0];
        String cents = floatString.split("\\.")[1];
        String formattedDollars = "";
        if (cents.length() == 1)
        {
            cents += "0";
        }
        int x = 0;
        for (int i = dollars.length()-1; i >= 0; i--)
        {
            formattedDollars = dollars.charAt(i) + formattedDollars;
            if ((++x) % 3 == 0 && i != 0)
            {
                formattedDollars = "," + formattedDollars;
            }
        }

        return formattedDollars + "." +cents;
    }
}
