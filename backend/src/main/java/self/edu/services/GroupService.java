package self.edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupService {
    @Autowired
    private Map<String, Set<String>> groups;

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
