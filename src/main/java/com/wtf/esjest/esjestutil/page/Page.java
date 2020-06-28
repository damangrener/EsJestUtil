package com.wtf.esjest.esjestutil.page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Page<T> extends Pagination{
    private static final long serialVersionUID = 1L;
    /**
     * 结果集合
     */
    private List<T> records;

    /**
     * scroll分页ID
     */
    private String scrollId;

    private Map<String, Map<String,Double>> aggregationMap;

    public Page() {
        this.records = Collections.emptyList();
    }

    public Page(int size) {
        this(1, size);
    }

    public Page(int current, int size) {
        super(current, size);
        this.records = Collections.emptyList();
    }

    public List<T> getRecords() {
        return this.records;
    }

    public Map<String, Map<String, Double>> getAggregationMap() {
        return aggregationMap;
    }

    public void setAggregationMap(Map<String, Map<String, Double>> aggregationMap) {
        this.aggregationMap = aggregationMap;
    }

    public Page<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public String toString() {
        StringBuilder pg = new StringBuilder();
        pg.append(" Page:{ [").append(super.toString()).append("], ");
        if (this.records != null) {
            pg.append("records-size:").append(this.records.size());
        } else {
            pg.append("records is null");
        }

        return pg.append(" }").toString();
    }

}
