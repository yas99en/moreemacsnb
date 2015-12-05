package io.github.yas99en.moreemacsnb.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;

public final class CharacterUtils {
    private CharacterUtils() {}

    private static final List<String> EAST_ASIAN_LANGS =
        Arrays.asList("ja", "vi", "kr", "zh");

    public static int getWidth(int codePoint) {
        return getWidth(codePoint, Locale.getDefault());
    }

    public static int getWidth(int codePoint, Locale locale) {
        if(locale == null) {
            throw new NullPointerException("locale is null");
        }
        int value = UCharacter.getIntPropertyValue(codePoint, 
                UProperty.EAST_ASIAN_WIDTH);
        switch(value) {
        case UCharacter.EastAsianWidth.NARROW:
        case UCharacter.EastAsianWidth.NEUTRAL:
        case UCharacter.EastAsianWidth.HALFWIDTH:
            return 1;
        case UCharacter.EastAsianWidth.FULLWIDTH:
        case UCharacter.EastAsianWidth.WIDE:
            return 2;
        case UCharacter.EastAsianWidth.AMBIGUOUS:
            if(EAST_ASIAN_LANGS.contains(locale.getLanguage())) {
                return 2;
            } else {
                return 1;
            }
        default:
            return 1;
        }
    }
}
