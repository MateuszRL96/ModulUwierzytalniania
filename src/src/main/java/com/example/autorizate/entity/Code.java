package com.example.autorizate.entity;

public enum Code {
    SUCCESS("udalo sie!"),
    A1("sda"), 
    A2("iudsd");


    public final String label;
    private Code(String label)
    {
        this.label = label;
    }
}
