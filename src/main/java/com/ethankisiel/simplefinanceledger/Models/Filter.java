package com.ethankisiel.simplefinanceledger.Models;

import java.util.HashMap;

public class Filter
{
    private String filterName;
    private boolean isActive;

    public Filter(String filterName, Boolean isActive)
    {
        this.filterName = filterName;
        this.isActive = isActive;
    }
}
