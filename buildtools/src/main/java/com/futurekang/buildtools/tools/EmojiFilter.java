package com.futurekang.buildtools.tools;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * EmojiFilter
 * example
 * EditText.setFilter(new InputFilter[]{ new EmojiFilter})
 */
public class EmojiFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            int type = Character.getType(source.charAt(i));
            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }
        return null;
    }
}
