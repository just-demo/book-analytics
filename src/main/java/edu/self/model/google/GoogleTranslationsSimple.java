package edu.self.model.google;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "translations")
public class GoogleTranslationsSimple {
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
        private String translation;

        public Translation() {
        }

        public Translation(String text, String translation) {
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

        @XmlValue()
        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }
}