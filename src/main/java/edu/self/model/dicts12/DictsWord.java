package edu.self.model.dicts12;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class DictsWord {
    @XmlValue()
    private String text;
    @XmlTransient
    private List<DictsWord> children;
    @XmlTransient
    private List<DictsWord> parents;

    public DictsWord() {
    }

    public DictsWord(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DictsWord> getChildren() {
        if (children == null) {
            children = new ArrayList<DictsWord>();
        }
        return children;
    }

    public void setChildren(List<DictsWord> children) {
        this.children = children;
    }

    public List<DictsWord> getParents() {
        if (parents == null) {
            parents = new ArrayList<DictsWord>();
        }
        return parents;
    }

    public void setParents(List<DictsWord> parents) {
        this.parents = parents;
    }

    /*
      * public String toString(){ return text; }
      */
}
