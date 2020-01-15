package com.example.peter.thekitchenmenu.commonmocks;

public class StringMaker {

    private String string = "";

    public StringMaker makeStringOfExactLength(int length) {

        String a="a";
        StringBuilder builder = new StringBuilder();

        if (!string.isEmpty()) {
            builder.append(string);
            if (string.length() >= length) {
                builder.setLength(length);
                string = builder.toString();
                return this;
            }
        }

        for (int i=string.length(); i<length; i++) {
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
        if (string.length() > 1) {
            string = string.substring(0, string.length() -1);
            return this;
        } else {
            throw new IllegalStateException("String too short");
        }
    }

    public StringMaker includeStringAtStart(String stringToInclude) {
        StringBuilder builder = new StringBuilder();
        builder.append(stringToInclude);

        if (!string.isEmpty()) {
            if (stringToInclude.length() >= string.length()) {
                builder.setLength(string.length());
            } else {
                string = string.substring(0, string.length() - stringToInclude.length());
                builder.append(string);
            }
        }
        string = builder.toString();
        return this;
    }

    public String build() {
        return string;
    }
}
