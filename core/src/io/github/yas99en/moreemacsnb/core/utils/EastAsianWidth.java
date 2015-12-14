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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Yasuhiro Endoh
 */
public class EastAsianWidth {
    public static final byte NEUTRAL = 0;
    public static final byte NARROW = 4;
    public static final byte HALFWIDTH = 2;
    public static final byte FULLWIDTH = 3;
    public static final byte WIDE = 5;
    public static final byte AMBIGUOUS = 1;

    private static final String[] WIDE_CHAR_RANGES = {
        "3400..4DBF;W",
        "4E00..9FFF;W",
        "F900..FAFF;W",
        "20000..2A6DF;W",
        "2A700..2B73F;W",
        "2B740..2B81F;W",
        "2B820..2CEAF;W",
        "2F800..2FA1F;W",
        "20000..2FFFD;W",
        "30000..3FFFD;W",
    };

    private static final byte[] db = new byte[0x110000];

    static {
        try {
            loadData();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void loadData() throws IOException {
        parseStream(Arrays.stream(WIDE_CHAR_RANGES), db);
        InputStream in = EastAsianWidth.class.getResourceAsStream("EastAsianWidth.txt");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            parseStream(reader.lines(), db);
        }
    }

    private static void parseStream(Stream<String> stream, byte[] db) {
        stream.map(record -> {
            int commentStart = record.indexOf("#");
            return (commentStart == -1) ? record : record.substring(0, commentStart);
        })
        .map(String::trim)
        .filter(content->!content.isEmpty())
        .forEach(content -> {
            parseContent(content, db);
        });
    }

    private static void parseContent(String content, byte[] db) {
        String[] splited = content.split(";");
        if(splited.length != 2) {
            return;
        }
        String rangeStr = splited[0];
        byte property = property2Byte(splited[1]);
        int delimPos = rangeStr.indexOf("..");
        if(delimPos == -1) {
            int cp = Integer.parseInt(rangeStr, 16);
            db[cp] = property;
            return;
        }

        int start = Integer.parseInt(rangeStr.substring(0, delimPos), 16);
        int end   = Integer.parseInt(rangeStr.substring(delimPos+2),  16);
        for(int cp = start; cp <= end; cp++) {
            db[cp] = property;
        }
    }

    private static byte property2Byte(String property) {
        switch(property) {
            case "Na":
                return NARROW;
            case "N":
                return NEUTRAL;
            case "H":
                return HALFWIDTH;
            case "F":
                return FULLWIDTH;
            case "W":
                return WIDE;
            case "A":
                return AMBIGUOUS;
            default:
                throw new RuntimeException("Unknown property: " + property);
        }
    }

    private EastAsianWidth() {}

    public static int getProperty(int cp) {
        return cp < db.length ? db[cp] : NEUTRAL;
    }
}
