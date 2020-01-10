package com.example.peter.thekitchenmenu.commonmocks;

public class StringMaker {

    private String string = "";

    public StringMaker makeStringOfExactLength(int length) {
        StringBuilder builder = new StringBuilder();
        String a="a";
        for (int i=0; i<length; i++) {
            builder.append(a);
        }
        string = builder.toString();
        return this;
    }

    public StringMaker thenAddOneCharacter() {
        string += "a";
        return this;
    }

    public StringMaker thenRemoveOneCharacter() {
        string = string.substring(0, string.length() -1);
        return this;
    }

    public String build() {
        return string;
    }
}
