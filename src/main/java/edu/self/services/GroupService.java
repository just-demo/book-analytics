package edu.self.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    @Autowired
    @Qualifier("groups")
    private Set<Set<String>> groups;

    @Autowired
    @Qualifier("wordGroups")
    private Map<String, Set<String>> wordGroups;

    public Collection<Collection<String>> group(Collection<String> collection) {
        Map<Object, Collection<String>> groupBuffer = new HashMap<>();
        for (String word : collection) {
            Object identifier = wordGroups.get(word);
            if (identifier == null) {
                identifier = word;
            }
            Collection<String> group = groupBuffer.computeIfAbsent(identifier, k -> new ArrayList<>());
            group.add(word);
        }
        return groupBuffer.values();
    }

    public Set<Set<String>> getGroups() {
        return groups;
    }
}
