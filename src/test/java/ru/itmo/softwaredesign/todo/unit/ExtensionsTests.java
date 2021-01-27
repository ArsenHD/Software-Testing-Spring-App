package ru.itmo.softwaredesign.todo.unit;

import org.junit.jupiter.api.Test;
import ru.itmo.softwaredesign.todo.extensions.Extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExtensionsTests {
    @Test
    public void testToSlugSingleWord() {
        String slug = Extensions.toSlug("somestring");
        assertEquals("somestring", slug);
    }

    @Test
    public void testToSlugTwoWords() {
        String slug = Extensions.toSlug("some string");
        assertEquals("some-string", slug);
    }

    @Test
    public void testToSlugThreeWords() {
        String slug = Extensions.toSlug("some long string");
        assertEquals("some-long-string", slug);
    }

    @Test
    public void testToSlugDecapitalize() {
        String slug = Extensions.toSlug("Camel Case String");
        assertEquals("camel-case-string", slug);
    }

    @Test
    public void testToSlugRepalceDelimiters() {
        String slug = Extensions.toSlug("some,separated,string");
        assertEquals("some-separated-string", slug);
    }
}
