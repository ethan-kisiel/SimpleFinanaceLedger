package com.ethankisiel.simplefinanceledger.Models;

import java.util.Comparator;

public class SortByDate implements Comparator<Entry>
{

    @Override
    public int compare(Entry o1, Entry o2)
    {
        return o1.getDate().compareTo(o2.getDate());
    }
}