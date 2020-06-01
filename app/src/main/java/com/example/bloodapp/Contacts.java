package com.example.bloodapp;

public class Contacts
{
    public String name, blood, number, city; //this will be the same name as firebase database name


    public Contacts()
    {

    }

    public Contacts(String name, String blood, String city, String number) {
        this.name = name;
        this.blood = blood;
        this.number=number;
        this.city=city;



    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getblood()
    {
        return blood;
    }

    public void setblood(String blood)
    {
        this.blood = blood;
    }

    public void setCity( String city)
    {
        this.city=city;
    }

    public String getCity()
   {
    return city;
   }

   public void setNumber(String number)
   {
       this.number=number;
   }
   public String getNumber()
   {
       return number;
   }
    }

