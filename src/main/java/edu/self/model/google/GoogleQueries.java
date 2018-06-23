package edu.self.model.google;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "queries")
public class GoogleQueries {
    @XmlElement(name = "query")
    protected List<Query> queries;

    public List<Query> getQueries() {
        if (queries == null) {
            queries = new ArrayList<Query>();
        }
        return queries;
    }

    public static class Query {
        private String text;
        private String responce;

        public Query() {
        }

        public Query(String text, String responce) {
            this.text = text;
            this.responce = responce;
        }

        @XmlElement()
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @XmlElement()
        public String getResponce() {
            return responce;
        }

        public void setResponce(String responce) {
            this.responce = responce;
        }
    }
}