package com.sergiomse.guiatv.model;

/**
 * Created by sergiomse@gmail.com on 21/07/2015.
 */
public class ProgramComponent extends ProgramComponentBase {

    private int id;
    private String time;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
