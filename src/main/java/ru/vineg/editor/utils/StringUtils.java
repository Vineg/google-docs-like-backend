package ru.vineg.editor.utils;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {
    public static List<Character> toCharacterList(String text) {
        return text.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
}
