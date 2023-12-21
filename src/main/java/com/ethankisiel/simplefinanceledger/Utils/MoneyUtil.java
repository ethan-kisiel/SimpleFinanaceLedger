package com.ethankisiel.simplefinanceledger.Utils;

public class MoneyUtil
{
    public static int floatToCents(float amount)
    {
        int cents = (int) (amount * 100);
        return 0;
    }

    public static String centsToString(int cents)
    {

//        String floatString = Float.toString(amount);
//        String dollars = floatString.split("\\.")[0];
//        String cents = floatString.split("\\.")[1];


        String centsString = String.valueOf(cents % 100);
        String dollarsString = String.valueOf(cents / 100);

        StringBuilder formattedDollars = new StringBuilder();
        if (centsString.length() == 1)
        {
            centsString += "0";
        } else if (centsString.length() > 2)
        {
            centsString = centsString.substring(0, 2);
        }

        int x = 0;
        for (int i = dollarsString.length()-1; i >= 0; i--)
        {
            formattedDollars.insert(0, dollarsString.charAt(i));
            if ((++x) % 3 == 0 && i != 0)
            {
                formattedDollars.insert(0, ",");
            }
        }

        return formattedDollars + "." + centsString;
    }
}
