package ru.itmo.softwaredesign.todo.extensions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Extensions {
    public static String toSlug(String str) {
        return Arrays.stream(str.toLowerCase()
                .replace("\n", " ")
                .replaceAll("[^a-z\\d\\s]", " ")
                .split(" ")).sequential().collect(Collectors.joining("-"))
                .replaceAll("[+-]", "-");
    }
}
