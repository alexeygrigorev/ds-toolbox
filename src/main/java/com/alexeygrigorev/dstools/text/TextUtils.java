package com.alexeygrigorev.dstools.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextUtils {

    private static final Pattern NOT_LATIN_LETTER_PATTERN = Pattern.compile("\\W+");

    private static Locale RUSSIAN_LOCALE = new Locale("ru", "ru");
    private static final Pattern CYRRILIC_LETTERS_PATTERN = Pattern.compile("[0-9a-zа-яё]+");

    public static List<String> tokenize(String line) {
        String[] split = NOT_LATIN_LETTER_PATTERN.split(line.toLowerCase());
        return Arrays.stream(split)
                .map(String::trim)
                .filter(s -> s.length() > 2)
                .collect(Collectors.toList());
    }

    public static List<String> tokenizeRussian(String line) {
        line = line.toLowerCase(RUSSIAN_LOCALE);
        List<String> result = new ArrayList<>();

        Matcher matcher = CYRRILIC_LETTERS_PATTERN.matcher(line);

        while (matcher.find()) {
            String token = matcher.group();
            if (token.length() >= 2) {
                result.add(token);
            }
        }

        return result;
    }

    public static List<String> removeStopwords(List<String> line, Set<String> stopwords) {
        return line.stream().filter(token -> !stopwords.contains(token)).collect(Collectors.toList());
    }

    public static List<String> ngrams(List<String> tokens, int minN, int maxN) {
        List<String> result = new ArrayList<>();

        for (int n = minN; n <= maxN; n++) {
            List<String> ngrams = ngrams(tokens, n);
            result.addAll(ngrams);
        }

        return result;
    }

    public static List<String> ngrams(List<String> tokens, int n) {
        if (n == 1) {
            return tokens;
        }

        int size = tokens.size();
        if (n > size) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();

        for (int i = 0; i < size - n + 1; i++) {
            List<String> sublist = tokens.subList(i, i + n);
            result.add(String.join("_", sublist));
        }

        return result;
    }

    public static boolean isPunctuation(String token) {
        char first = token.charAt(0);
        return !Character.isAlphabetic(first) && !Character.isDigit(first);
    }

}
