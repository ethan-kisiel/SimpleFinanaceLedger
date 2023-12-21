package com.ethankisiel.simplefinanceledger.Models;

import com.ethankisiel.simplefinanceledger.Utils.MoneyUtil;
import javafx.scene.control.PasswordField;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Date;

public class Entry
{
    private int id;
    private Date date;
    private int amount;
    private String checkNumber;

    private String checkbook;
    private String category;
    private String subcategory;
    private String itemization;
    private String notes;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Entry()
    {
    }
    public Entry(int id)
    {
        this.id = id;
    }


    public void setId(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return id;
    }

    public void setDate(LocalDate date)
    {
        this.date = Date.from(date.atTime(12, 0).toInstant(ZoneOffset.ofTotalSeconds(0)));
    }

    public void setDate(String date)
    {
        // parse date from string
        //date = new LocalDate
        this.setDate(LocalDate.parse(date));
    }

    public Date getDate()
    {
        return this.date;
    }

    public LocalDate getLocalDate()
    {
        return date.toInstant().atOffset(ZoneOffset.ofTotalSeconds(0)).toLocalDate();
    }

    public String getDisplayDate()
    {
        try
        {
            return this.dateFormat.format(this.date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public String getDateString()
    {
        try
        {
            return this.localDateFormat.format(this.date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public String getYear()
    {
        return Year.of(this.getLocalDate().getYear()).toString();
    }

    public void setAmount(float newAmount)
    {
        try
        {
            this.amount = MoneyUtil.floatToCents(newAmount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.amount = 0;
        }
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public int getAmount()
    {
        return this.amount;
    }

    public String getDisplayAmount()
    {
        return MoneyUtil.centsToString(this.amount);
    }


    public void setCheckNumber(String newCheckNumber)
    {
        this.checkNumber = newCheckNumber;
    }

    public String getCheckNumber()
    {
        return this.checkNumber;
    }


    public void setCheckbook(String newCheckbook)
    {
        this.checkbook = newCheckbook;
    }

    public String getCheckbook()
    {
        return this.checkbook;
    }


    public void setCategory(String newCategory)
    {
        this.category = newCategory;
    }

    public String getCategory()
    {
        return this.category;
    }

    public void setSubcategory(String newSubcategory)
    {
        this.subcategory = newSubcategory;
    }

    public String getSubcategory()
    {
        return this.subcategory;
    }


    public void setItemization(String newItemization)
    {
        this.itemization = newItemization;
    }

    public String getItemization()
    {
        return this.itemization;
    }


    public void setNotes(String newNotes) { this.notes = newNotes; }

    public String getNotes()
    {
        return this.notes;
    }



    @Override
    public boolean equals(Object obj)
    {
        try
        {
            return (((Entry) obj).getId() == this.getId());
        }
        catch (Exception e)
        {
           return false;
        }
    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
