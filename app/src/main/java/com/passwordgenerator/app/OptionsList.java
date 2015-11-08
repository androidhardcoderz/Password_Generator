package com.passwordgenerator.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 10/26/2015.
 */
public class OptionsList {

    private int uppercaseLetters;
    private int lowercaseLetters;
    private final String TAG = "OptionsList";

    List<String> numbers = new ArrayList<>();

    public List<String> getNumbersEX() {
        return numbersEX;
    }

    public void setNumbersEX(List<String> numbersEX) {
        this.numbersEX = numbersEX;
    }

    List<String> numbersEX = new ArrayList<>();
    List<String> ambiguous = new ArrayList<>();
    List<String> similiar = new ArrayList<>();

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<String> getSimiliar() {
        return similiar;
    }

    public void setSimiliar(List<String> similiar) {
        this.similiar = similiar;
    }

    List<String> symbols = new ArrayList<>();
    char[] alpha = new char[26];


    public OptionsList(){

        symbols.add("!");
        symbols.add("@");
        symbols.add("#");
        symbols.add("$");
        symbols.add("%");
        symbols.add("^");
        symbols.add("&");
        symbols.add("*");
        symbols.add("{");
        symbols.add("}");
        symbols.add("(");
        symbols.add(")");
        symbols.add("[");
        symbols.add("]");
        symbols.add("/");
        symbols.add("\\");
        symbols.add("'");
        symbols.add("\"");
        symbols.add("`");
        symbols.add("~");
        symbols.add(",");
        symbols.add(";");
        symbols.add(":");
        symbols.add(".");
        symbols.add("<");
        symbols.add(">");

        similiar.add("i");
        similiar.add("I");
        similiar.add("l");
        similiar.add("o");
        similiar.add("O");
        similiar.add("0");
        similiar.add("L");


        ambiguous.add("{");
        ambiguous.add("}");
        ambiguous.add("(");
        ambiguous.add(")");
        ambiguous.add("[");
        ambiguous.add("]");
        ambiguous.add("/");
        ambiguous.add("\\");
        ambiguous.add("'");
        ambiguous.add("\"");
        ambiguous.add("`");
        ambiguous.add("~");
        ambiguous.add(",");
        ambiguous.add(";");
        ambiguous.add(":");
        ambiguous.add(".");
        ambiguous.add("<");
        ambiguous.add(">");

        for(int i = 0; i < 10;i++){
            numbers.add(i + "");
        }



        int k = 0;
        for(int i = 0; i < 26; i++){
            alpha[i] = (char)(97 + (k++));
        }

        for(String s : ambiguous){
            System.out.print(s);
        }

    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public char[] getAlpha() {
        return alpha;
    }

    public void setAlpha(char[] alpha) {
        this.alpha = alpha;
    }

}
