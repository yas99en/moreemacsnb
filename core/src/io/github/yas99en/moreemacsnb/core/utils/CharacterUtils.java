/*
 * Copyright (c) 2015, Yasuhiro Endoh
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the authors nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
