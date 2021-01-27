package ru.itmo.softwaredesign.todo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNameGenerator {
    public String name() {
        return name(10);
    }

    public String name(int length) {
        List<Character> characters = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
            characters.add(Character.toUpperCase(c));
        }
        for (char c = '0'; c <= '9'; c++) {
            characters.add(c);
        }
        int charactersAmount = characters.size();
        StringBuilder sb = new StringBuilder();
        int index;
        for (int i = 0; i < length; i++) {
            index = new Random().nextInt(charactersAmount);
            sb.append(characters.get(index));
        }
        return sb.toString();
    }
}
