package edu.self.services.dicts12;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import edu.self.model.dicts12.DictsGroups;
import edu.self.model.dicts12.DictsWord;
import edu.self.utils.XMLUtils;

public class GroupService {
    private Map<String, DictsGroups.Group> groups;

    public GroupService(String fileName) throws FileNotFoundException, JAXBException {
        DictsGroups dictsGroups = XMLUtils.load(fileName, DictsGroups.class);
        groups = new HashMap<String, DictsGroups.Group>();
        for (DictsGroups.Group group : dictsGroups.getGroups()) {
            for (DictsWord word : group.getWords()) {
                groups.put(word.getText(), group);
            }
        }
    }

    public Object getGroup(String word) {
        return groups.get(word);
    }

    public Collection<Collection<String>> group(Collection<String> collection) {
        Map<Object, Collection<String>> groupBuffer = new HashMap<Object, Collection<String>>();
        for (String word : collection) {
            Object identifier = groups.get(word);
            if (identifier == null) {
                identifier = word;
            }
            Collection<String> group = groupBuffer.get(identifier);
            if (group == null) {
                group = new ArrayList<String>();
                groupBuffer.put(identifier, group);
            }
            group.add(word);
        }
        return groupBuffer.values();
    }
}
