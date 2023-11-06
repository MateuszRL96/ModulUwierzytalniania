package com.example.auth.entity;

public enum Code {
    SUCCESS("Operacja zakonczona sukcesem"),
    A1("nie udalo sie zalogowac"),
    A2("uzytkownik o podanej nazwie nie isnieje");

    public final String label;

    private Code(String label){
    this.label = label;}

}
