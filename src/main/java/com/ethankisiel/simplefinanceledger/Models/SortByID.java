package com.ethankisiel.simplefinanceledger.Models;

import java.util.Comparator;

public class SortByID implements Comparator<Entry>
{

    @Override
    public int compare(Entry o1, Entry o2)
    {
        return o1.getId() - o2.getId();
    }
}