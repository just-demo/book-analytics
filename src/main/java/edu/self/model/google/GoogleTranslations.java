package edu.self.model.google;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "translations")
public class GoogleTranslations {
    @XmlElement(name = "t")
    protected List<Translation> translations;

    public List<Translation> getTranslations() {
        if (translations == null) {
            translations = new ArrayList<Translation>();
        }
        return translations;
    }

    public static class Translation {
        private String text;
        private List<String> translation;

        public Translation() {
        }

        public Translation(String text, List<String> translation) {
            this.text = text;
            this.translation = translation;
        }

        @XmlAttribute(name = "k")
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @XmlElement(name = "v")
        //@XmlValue()
        public List<String> getTranslation() {
            if (translation == null) {
                translation = new ArrayList<String>();
            }
            return translation;
        }

        public void setTranslation(List<String> translation) {
            this.translation = translation;
        }
    }
}