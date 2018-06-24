package edu.self.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

import static edu.self.utils.FileUtils.readResourceFile;

public class GroupService {
    private Map<String, Set<String>> groups = new HashMap<>();

    public GroupService() throws IOException {
        Set<Set<String>> groupsSet = new ObjectMapper().readValue(readResourceFile("groups.json"), new TypeReference<Set<Set<String>>>() {
        });
        groupsSet.forEach(group -> group.forEach(word -> groups.put(word, group)));
    }

    public Collection<Collection<String>> group(Collection<String> collection) {
        Map<Object, Collection<String>> groupBuffer = new HashMap<>();
        for (String word : collection) {
            Object identifier = groups.get(word);
            if (identifier == null) {
                identifier = word;
            }
            Collection<String> group = groupBuffer.computeIfAbsent(identifier, k -> new ArrayList<>());
            group.add(word);
        }
        return groupBuffer.values();
    }
}
