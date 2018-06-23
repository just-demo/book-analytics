package edu.self.model.user;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class UserWord implements Comparable {
    private static enum Type {
        COMMON, PROPER
    }

    private String text;
    private String translation;
    private boolean ignorable;
    private int count;
    private Type type;

    public UserWord() {
    }

    public UserWord(String text, String translation) {
        this.text = text;
        this.translation = translation;
        count = 1;
    }

    @XmlElement()
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlElement()
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    @XmlAttribute()
    public boolean isIgnorable() {
        return ignorable;
    }

    public void setIgnorable(boolean ignorable) {
        this.ignorable = ignorable;
    }

    public void incrementCount() {
        ++count;
    }

    public int getCount() {
        return count;
    }

    public boolean isProper() {
        return Type.PROPER.equals(type);
    }

    public boolean isCommon() {
        return Type.COMMON.equals(type);
    }

    public void setProper() {
        type = Type.PROPER;
    }

    public void setCommon() {
        type = Type.COMMON;
    }

    public boolean isUndefined() {
        return type == null;
    }

    @Override
    public int compareTo(Object o) {
        int cmp = ((Integer) count).compareTo(((UserWord) o).count);
        return cmp != 0 ? -cmp : text.compareTo(((UserWord) o).text);
    }
}