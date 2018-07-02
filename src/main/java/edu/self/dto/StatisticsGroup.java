package edu.self.dto;

import java.util.ArrayList;
import java.util.List;

public class StatisticsGroup {
    private long occurrence;
    private List<Statistics> items = new ArrayList<>();

    public void add(Statistics statistics) {
        items.add(statistics);
        occurrence += statistics.getOccurrence();
    }

    public long getOccurrence() {
        return occurrence;
    }

    public List<Statistics> getItems() {
        return items;
    }
}
