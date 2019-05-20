package com.example.peter.thekitchenmenu.utils;

import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

public class CharacterInputFilters {

    public static InputFilter getOnlyCharactersFilter() {
        return getCustomInputFilter(true, false, false);
    }

    public static InputFilter getCharactersAndDigitsFilter() {
        return getCustomInputFilter(true, true, false);
    }

    public static InputFilter getCustomInputFilter(final boolean allowCharacters,
                                                   final boolean allowDigits,
                                                   final boolean allowSpaceChar) {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source,
                                       int start,
                                       int end,
                                       Spanned dest,
                                       int dstart,
                                       int dend) {

                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);

                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);

                    if (isCharAllowed(c)) {
                        sb.append(c);
                    } else {
                        keepOriginal = false;
                    }
                }

                if (keepOriginal) {
                    return null;

                } else {

                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom(
                                (Spanned) source,
                                start,
                                sb.length(),
                                null, sp,
                                0);
                        return sp;

                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {

                if (Character.isLetter(c) && allowCharacters) {
                    return true;
                }

                if (Character.isDigit(c) && allowDigits) {
                    return true;
                }

                return Character.isSpaceChar(c) && allowSpaceChar;
            }
        };
    }
}
