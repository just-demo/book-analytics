package edu.self.model;

public class WordInfo implements Comparable<WordInfo> {
    private int total;
    private int common;
    private int proper;
    private int undefined;
    private int strange;

    public void incrementTotal() {
        ++total;
    }

    public void incrementUndefined() {
        ++undefined;
    }

    public void incrementCommon() {
        ++common;
    }

    public void incrementProper() {
        ++proper;
    }

    public void incrementStrange() {
        ++strange;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUndefined() {
        return undefined;
    }

    public void setUndefined(int undefined) {
        this.undefined = undefined;
    }

    public int getCommon() {
        return common;
    }

    public void setCommon(int common) {
        this.common = common;
    }

    public int getProper() {
        return proper;
    }

    public void setProper(int proper) {
        this.proper = proper;
    }

    public int getStrange() {
        return strange;
    }

    public void setStrange(int strange) {
        this.strange = strange;
    }

    @Override
    public int compareTo(WordInfo o) {
        return Integer.compare(total, o.total);
    }
}
