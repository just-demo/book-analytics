package edu.self.model.oxford;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "words")
public class OxfordWords {
    @XmlElement(name = "word")
    protected List<Word> words;

    public List<Word> getWords() {
        if (words == null) {
            words = new ArrayList<Word>();
        }
        return words;
    }

    public static class Word {
        private String base;
        private Set<String> inflections;
        private Set<String> derivatives;

        public Word() {
        }

        public Word(String base, Set<String> inflections, Set<String> derivatives) {
            this.base = base;
            this.inflections = inflections;
            this.derivatives = derivatives;
        }

        @XmlElement(name = "base")
        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        @XmlElement(name = "inflection")
        //@XmlElementWrapper(name="inflections")
        public Set<String> getInflections() {
            return inflections;
        }

        public void setInflections(Set<String> inflections) {
            this.inflections = inflections;
        }

        @XmlElement(name = "derivative")
        //@XmlElementWrapper(name="derivatives")
        public Set<String> getDerivatives() {
            return derivatives;
        }

        public void setDerivatives(Set<String> derivatives) {
            this.derivatives = derivatives;
        }
    }
}
