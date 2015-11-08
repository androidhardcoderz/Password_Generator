package com.passwordgenerator.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Scott on 11/4/2015.
 */
public class Password {

    int length;
    List<States> states = new ArrayList<>();
    OptionsList optionList;

    private final String TAG = "Password";

    public Password(){

    }

    public Password(int length,List<States> states){
        this.length = length;
        this.states = states;

        optionList = new OptionsList();
        setPassword(buildPassword());
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLength() {
        return length;
    }

    String password;

    private String buildPassword(){

        String password = "";

        //populate password with spaces
        for(int l = 0; l < this.length;l++){
            Log.i(TAG,password);
        }

        int numOfStates = 0;
        for(States state: states){
            //find number of states selected
            if(state.isState()){
                numOfStates += 1;
            }
        }

        int dividend,extra;

        //if states contains ambiguous or similiar singular state!
        if(states.get(states.size() - 1).getName().equals(UserPreferences.AMBIGUOUS) &&
                states.get(states.size() - 2).getName().equals(UserPreferences.SIMILAR)){
            dividend = this.length / (numOfStates -2);
            extra = this.length % (numOfStates -2);
        }
        else if(states.get(states.size() - 1).getName().equals(UserPreferences.AMBIGUOUS) ||
                states.get(states.size() - 1).getName().equals(UserPreferences.SIMILAR)){
            dividend = this.length / (numOfStates - 1);
            extra = this.length % (numOfStates - 1);
        }else{
            dividend = this.length / numOfStates;
            extra = this.length % numOfStates;
        }


        for(int i = 0; i < numOfStates;i++){
            String s = "";
            switch(states.get(i).getName()){
                case UserPreferences.SYMBOLS:
                    for(int d = 0; d < dividend;d++){
                        s += optionList.getSymbols().get(new Random().nextInt(optionList.getSymbols().size()));
                    }
                    password += s;
                    break;
                case UserPreferences.NUMBERS:
                    s = "";
                    for(int d = 0; d < dividend;d++){
                        s += optionList.getNumbers().get(new Random().nextInt(optionList.getNumbers().size()));
                    }
                    password += s;
                    break;
                case UserPreferences.LOWERCASE:
                    s = "";
                    for(int d = 0; d < dividend;d++){
                        char c = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                        s += String.valueOf(c).toLowerCase();
                    }
                    password += s;
                    break;

                case UserPreferences.UPPERCASE:
                    s = "";
                    for(int d = 0; d < dividend;d++){
                        char c = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                        s += String.valueOf(c).toUpperCase();
                    }
                    password += s;
                    break;
                default:
                    break;
            }


        }

        if(extra != 0){
           for(int e = 0; e < extra;e++){
               //loop through the number of extra characters
               int randomNumber;
               //if states contains ambiguous or similiar singular state!
               if(states.get(states.size() - 1).getName().equals(UserPreferences.AMBIGUOUS) &&
                       states.get(states.size() - 2).getName().equals(UserPreferences.SIMILAR)){
                   randomNumber = new Random().nextInt(states.size() - 2);
               }
               else if(states.get(states.size() - 1).getName().equals(UserPreferences.AMBIGUOUS) ||
                       states.get(states.size() - 1).getName().equals(UserPreferences.SIMILAR)){
                   randomNumber = new Random().nextInt(states.size() - 1);
               }else{
                   randomNumber = new Random().nextInt(states.size());
               }


               //populate password string with random option letter/number/ or symbol
               States st = states.get(randomNumber);
               switch(st.getName()){
                   case UserPreferences.SYMBOLS:
                       password += optionList.getSymbols().get(new Random().nextInt(optionList.getSymbols().size()));
                       break;
                   case UserPreferences.NUMBERS:
                       password += optionList.getNumbers().get(new Random().nextInt(optionList.getNumbers().size()));
                       break;
                   case UserPreferences.LOWERCASE:
                       char c = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                       password += String.valueOf(c).toLowerCase();
                       break;
                   case UserPreferences.UPPERCASE:
                       char b = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                       password += String.valueOf(b).toUpperCase();
                       break;
                   default:
                       break;
               }
           }
        }



        password = checkExclusions(password);

        return shuffleString(password);

    }

    public  String shuffleString(String word) {
        List<Character> characters = new ArrayList<>();
        for(char c : word.toCharArray()) {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder sb = new StringBuilder();
        for(char c : characters) {
            sb.append(c);
        }

        return sb.toString();
    }

    private String checkExclusions(String s) {

        try {
            if (states.get(states.size() - 1).getName().equals(UserPreferences.AMBIGUOUS)) {
                //find amb characters and remove them
                char chars[];
                chars = s.toCharArray();
                for(int o = 0; o < chars.length;o++){
                    Log.i(TAG,chars[o] + " ");
                }

                for(char c: chars){
                    if(optionList.ambiguous.contains(String.valueOf(c))){
                        Log.i(TAG,c + " in password");
                        int size = 0;
                        if(states.get(states.size() - 2).getName().equals(UserPreferences.SIMILAR)){
                            size = states.size() - 2;
                        }else{
                            size = states.size() -1;
                        }
                        int randomNumber = new Random().nextInt(size);
                        States st = states.get(randomNumber);
                        switch(st.getName()){
                            case UserPreferences.SYMBOLS:
                                String ab = optionList.getSymbols().get(new Random().nextInt(8));
                                s = s.replace(String.valueOf(c),ab);
                                Log.i(TAG,c + " replaced w/ " + ab);
                                break;
                            case UserPreferences.NUMBERS:
                                s = s.replace(String.valueOf(c),optionList.getNumbers().get(new Random().nextInt(optionList.getNumbers().size())));
                                Log.i(TAG, c + " replaced w/ " + optionList.getNumbers().get(new Random().nextInt(optionList.getNumbers().size())));
                                break;
                            case UserPreferences.LOWERCASE:
                                char ch = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                                s = s.replace(String.valueOf(c),String.valueOf(ch).toLowerCase());
                                Log.i(TAG, c + " replaced w/ " + ch);
                                break;
                            case UserPreferences.UPPERCASE:
                                char cha = optionList.getAlpha()[new Random().nextInt(optionList.getAlpha().length)];
                              s = s.replace(String.valueOf(c), String.valueOf(cha).toUpperCase());
                                Log.i(TAG, c + " replaced w/ " + cha);
                                break;
                            default:
                                Log.i(TAG,"WWTF");
                                break;
                        }
                    }
                }
            }
        }catch(IndexOutOfBoundsException aiobe){
            Log.i(TAG,aiobe.getMessage().toString());
        }catch(Exception ex){
            Log.i(TAG,ex.getMessage().toString());
        }


        return s;
    }
}
