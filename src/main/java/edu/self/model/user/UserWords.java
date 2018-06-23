package edu.self.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "words")
public class UserWords {
    @XmlElement(name = "word")
    protected List<UserWord> words;

    public List<UserWord> getWords() {
        if (words == null) {
            words = new ArrayList<UserWord>();
        }
        return words;
    }
}