package edu.self.model.dicts12;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlSeeAlso({ArrayList.class})
@XmlRootElement(name = "groups")
public class DictsGroups {
//	public static void main(String[] args) throws FileNotFoundException, JAXBException {
//		DictsGroups groups = new DictsGroups();
//		List<DictsWord> group;
//		group = new ArrayList<DictsWord>();
//		group.add(new DictsWord("ccc"));
//		group.add(new DictsWord("ddd"));
//		groups.getGroups().add(new Group(group));
//		group = new ArrayList<DictsWord>();
//		group.add(new DictsWord("aaa"));
//		groups.getGroups().add(new Group(group));
//		String fileName = CONFIG_ROOT + "\\12dicts_result.xml";
//		XMLUtils.save(fileName, groups);
//		DictsGroups groups2 = XMLUtils.load(fileName, DictsGroups.class);
//		String fileName2 = CONFIG_ROOT + "\\12dicts_result2.xml";
//		XMLUtils.save(fileName2, groups2);
//	}

    @XmlElement(name = "group")
    List<Group> groups;

    public List<Group> getGroups() {
        if (groups == null) {
            groups = new ArrayList<Group>();
        }
        return groups;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Group {
        @XmlElement(name = "word")
        public List<DictsWord> words;

        public Group() {
        }

        public Group(List<DictsWord> words) {
            this.words = words;
        }

        public List<DictsWord> getWords() {
            if (words == null) {
                words = new ArrayList<DictsWord>();
            }
            return words;
        }

        public void setWords(List<DictsWord> words) {
            this.words = words;
        }
    }
}
