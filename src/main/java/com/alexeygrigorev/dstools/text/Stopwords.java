package com.alexeygrigorev.dstools.text;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public class Stopwords {
    public static final Set<String> EN_STOPWORDS = ImmutableSet.of("a", "an", "and", "are", "as", "at", "be", "but",
            "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their",
            "then", "there", "these", "they", "this", "to", "was", "will", "with", "what", "which", "s", "m", "t");

    public static final Set<String> RU_STOPWORDS = ImmutableSet.of("в", "на", "и", "с", "по", "для", "не", "от", "за",
            "к", "до", "из", "или", "у", "один", "вы", "при", "так", "ваш", "как", "а", "наш", "быть", "под", "б", "р",
            "мы", "эт", "же", "также", "что", "это", "раз", "свой", "он", "если", "но", "я", "о", "ещё", "тот", "этот",
            "то", "они", "ни", "чем", "где", "бы", "оно", "там", "ж", "она", "ты");
}
