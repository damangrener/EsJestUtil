package com.wtf.esjest.esjestutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtengfei
 * @since 2020/6/16 9:53
 */
public class Example {

    private String indexName;
    private String type;

    private Integer from;
    private Integer size;

    static Map<String, Object> base = new HashMap<>();
    static Map<String, Object> andConditions = new HashMap<>();
    static Map<String, Object> orConditions = new HashMap<>();
    static Map<String, Object> andNotConditions = new HashMap<>();

    static Map<String, Object> equalToConditions = new HashMap<>();
    static Map<String, Object> likeConditions = new HashMap<>();
    static Map<String, Object> gtConditions = new HashMap<>();
    static Map<String, Object> ltConditions = new HashMap<>();
    static Map<String, Object> gteConditions = new HashMap<>();
    static Map<String, Object> lteConditions = new HashMap<>();

    static Map<String, Object> orderConditions = new HashMap<>();
    static Map<String, Object> groupConditions = new HashMap<>();

//    List<Map<String, Object>> queryCondition = createQueryCondition().conditions;

    //将查询条件分类组装
    QueryCondition createQueryCondition() {
        if (indexName == null || type == null || from == null || size == null) {
            throw new RuntimeException("基本属性值缺失");
        }
        QueryCondition queryCondition = new QueryCondition();
//        List<Map<String, Object>> conditions = new ArrayList<>();
        base.put("indexName", indexName);
        base.put("type", type);
        base.put("from", from);
        base.put("size", size);
        queryCondition.conditions.add(0, base);
        queryCondition.conditions.add(1, andConditions);
        queryCondition.conditions.add(2, orConditions);
        queryCondition.conditions.add(3, andNotConditions);
        queryCondition.conditions.add(4, equalToConditions);
        queryCondition.conditions.add(5, likeConditions);
        queryCondition.conditions.add(6, gtConditions);
        queryCondition.conditions.add(7, ltConditions);
        queryCondition.conditions.add(8, gteConditions);
        queryCondition.conditions.add(9, lteConditions);
        queryCondition.conditions.add(10, orderConditions);
        queryCondition.conditions.add(11, groupConditions);

        return queryCondition;
    }

    public Example(String indexName, String type) {
        this.indexName = indexName;
        this.type = type;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }


    public static class QueryCondition {

        List<Map<String, Object>> conditions = new ArrayList<>();
        //"must"
        final Integer AND = 1;
        //"should"
        final Integer OR = 2;
        //"mustNot"
        final Integer AND_NOT = 3;
        //"equalTo"
        final Integer EQUAL_TO = 1;
        //"like"
        final Integer LIKE = 2;
        //"gt"
        final Integer GT = 3;
        //"lt"
        final Integer LT = 4;
        //"gte"
        final Integer GTE = 5;
        //"lte"
        final Integer LTE = 6;

        //
        final String ASC = "asc";
        //
        final String DESC = "desc";

        //
        final String GROUP_BY = "group_by";

        /**
         * @param a：AND/OR/AND_NOT：且/或/非
         * @param b：参数名
         * @param c：EQUAL_TO/LIKE:等值/模糊
         * @param value：参数值
         */
        void addQueryConditions(Integer a, StringBuilder b, Integer c, Object value, Boolean isStringInEs) {
            if (isStringInEs) {
                b = b.append(".keyword");
            }
            if (a == AND) {
                andConditions.put(b.toString(), value);
            } else if (a == OR) {
                orConditions.put(b.toString(), value);
            } else if (a == AND_NOT) {
                andNotConditions.put(b.toString(), value);
            }

            if (c == EQUAL_TO) {
                equalToConditions.put(b.toString(), value);
            } else if (c == LIKE) {
                likeConditions.put(b.toString(), value);
            } else if (c == GT) {
                gtConditions.put(b.toString(), value);
            } else if (c == LT) {
                ltConditions.put(b.toString(), value);
            } else if (c == GTE) {
                gteConditions.put(b.toString(), value);
            } else if (c == LTE) {
                lteConditions.put(b.toString(), value);
            }

            if (value.equals(ASC)) {
                orderConditions.put(b.toString(), ASC);
            } else if (value.equals(DESC)) {
                orderConditions.put(b.toString(), DESC);
            }else if(value.equals(GROUP_BY)){
                groupConditions.put(b.toString(), GROUP_BY);
            }
        }

        /**
         * andIdEqualTo
         * and/or/andNot：且/或/非
         * id:参数名
         * EqualTo/Like:等值/模糊
         *
         * @param value 值
         */
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        void andDeviceNameEqualTo(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = EQUAL_TO;
            addQueryConditions(a, b, c, value, true);
        }

        void andDeviceNameLike(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = LIKE;
            addQueryConditions(a, b, c, value, true);
        }

        void orDeviceNameEqualTo(Object value) {
            Integer a = OR;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = EQUAL_TO;
            addQueryConditions(a, b, c, value, true);
        }

        void orDeviceNameLike(Object value) {
            Integer a = OR;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = LIKE;
            addQueryConditions(a, b, c, value, true);
        }

        void andNotDeviceNameEqualTo(Object value) {
            Integer a = AND_NOT;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = EQUAL_TO;
            addQueryConditions(a, b, c, value, true);
        }

        void andNotDeviceNameLike(Object value) {
            Integer a = AND_NOT;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = LIKE;
            addQueryConditions(a, b, c, value, true);
        }

        void orderByDeviceNameAsc() {
            StringBuilder b = new StringBuilder("deviceName");
            String value = ASC;
            addQueryConditions(null, b, null, value, true);
        }

        void orderByDeviceNameDesc() {
            StringBuilder b = new StringBuilder("deviceName");
            String value = DESC;
            addQueryConditions(null, b, null, value, true);
        }

        void andDeviceNameGreaterThan(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = GT;
            addQueryConditions(a, b, c, value, true);
        }

        void andDeviceNameLessThan(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = LT;
            addQueryConditions(a, b, c, value, true);
        }

        void andDeviceNameGreaterThanAndEqualTo(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = GTE;
            addQueryConditions(a, b, c, value, true);
        }

        void andDeviceNameLessThanAndEqualTo(Object value) {
            Integer a = AND;
            StringBuilder b = new StringBuilder("deviceName");
            Integer c = LTE;
            addQueryConditions(a, b, c, value, true);
        }

        void groupByDeviceName() {
            StringBuilder b = new StringBuilder("deviceName");
            String value = GROUP_BY;
            addQueryConditions(null, b, null, value, true);
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }

}
