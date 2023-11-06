package com.example.Auth.entity;

public enum Code {
    SUCCESS("Operation succes"),
    A1("Operation failed"),
    A2("idlk");

    public final String label;

    private Code(String label){
    this.label = label;}

}
