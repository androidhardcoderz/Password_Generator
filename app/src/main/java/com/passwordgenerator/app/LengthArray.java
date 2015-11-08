package com.passwordgenerator.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 10/22/2015.
 */
public class LengthArray {

    List<Integer> lengths = new ArrayList<>();

    public LengthArray(){
        setLengths();
    }

    public List<Integer> getLengths() {
        return lengths;
    }

    public void setLengths() {
        for(int i = 1;i < 257;i++){
            lengths.add(i);
        }
    }
}
