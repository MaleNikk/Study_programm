package searchengine.dto.entity;

import java.util.HashMap;
import java.util.HashSet;

public record ModelWord(String word, HashMap<Integer, HashSet<String>> sites) {}
