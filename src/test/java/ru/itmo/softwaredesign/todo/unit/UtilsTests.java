package ru.itmo.softwaredesign.todo.unit;

import org.junit.jupiter.api.Test;
import ru.itmo.softwaredesign.todo.extensions.Utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTests {
    @Test
    public void testToSlugSingleWord() {
        String slug = Utils.toSlug("somestring");
        assertEquals("somestring", slug);
    }

    @Test
    public void testToSlugTwoWords() {
        String slug = Utils.toSlug("some string");
        assertEquals("some-string", slug);
    }

    @Test
    public void testToSlugThreeWords() {
        String slug = Utils.toSlug("some long string");
        assertEquals("some-long-string", slug);
    }

    @Test
    public void testToSlugDecapitalize() {
        String slug = Utils.toSlug("Camel Case String");
        assertEquals("camel-case-string", slug);
    }

    @Test
    public void testToSlugRepalceDelimiters() {
        String slug = Utils.toSlug("some,separated,string");
        assertEquals("some-separated-string", slug);
    }
}
