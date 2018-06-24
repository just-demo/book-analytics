package edu.self.model;

public class UserWord implements Comparable {
    private enum Type {
        COMMON, PROPER
    }

    private String text;
    private String translation;
    private boolean ignorable;
    private int count;
    private Type type;

    public UserWord(String text, String translation) {
        this.text = text;
        this.translation = translation;
        count = 1;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

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
        int cmp = Integer.compare(count, ((UserWord) o).count);
        return cmp != 0 ? -cmp : text.compareTo(((UserWord) o).text);
    }
}