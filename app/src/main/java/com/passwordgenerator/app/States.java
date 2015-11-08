package com.passwordgenerator.app;

/**
 * Created by Scott on 11/4/2015.
 */
public class States {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    private String name;
    private boolean state;
}
