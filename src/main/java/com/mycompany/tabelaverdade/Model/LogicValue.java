package com.mycompany.tabelaverdade.Model;

public class LogicValue {
    private int value;

    public LogicValue(int value){
        this.set(value);
    }

    public void set(int value){
        if (value <= 0) this.value = 0;
        else this.value = 1;
    }

    public int get() {return value;}

    public String toString() {return Integer.toString(value);}
}