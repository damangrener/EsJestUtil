package com.wtf.esjest.esjestutil;

import com.wtf.esjest.esjestutil.page.Page;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtengfei
 * @since 2020/6/16 11:12
 */
public class EsJestUtil<T> {
    //创建jest客户端
    public static JestClient jestClient() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder("http://192.168.2.200:9200/") //参数可以是集群，请先定义一个list集合，将节点url分别添加到list
                        .multiThreaded(true)//设置为多线程
                        .defaultMaxTotalConnectionPerRoute(2)
                        .maxTotalConnection(10)//最大连接数
                        .build()
        );
        return factory.getObject();
    }

    /**
     * conditions.add(1,andConditions);
     * conditions.add(2,orConditions);
     * conditions.add(3,andNotConditions);
     * conditions.add(4,equalToNotConditions);
     * conditions.add(5,likeConditions);
     */

    @Autowired
    private JestClient jestClient;

    Page<T> query(List<Map<String, Object>> condition, Class<T> clazz) {
        StringBuilder firstAgg=new StringBuilder("");
        List<String> aggs=new ArrayList<>();
        // 初始化分页
        Page<T> page = new Page<>((Integer) condition.get(0).get("from"), (Integer) condition.get(0).get("size"));
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (condition.size() == 12) {
            //对and条件进行组装
            for (int i = 1; i <= 3; i++) {
                condition.get(i).forEach((k, v) -> {
                    //等值查询
                    condition.get(4).forEach((x, y) -> {
                        //判断以防止查询条件重复
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(x, y));
                        }
                    });
                    //模糊查询
                    condition.get(5).forEach((x, y) -> {
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.wildcardQuery(x, "*" + y + "*"));
                        }
                    });
                    //大于
                    condition.get(6).forEach((x, y) -> {
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.rangeQuery(x).gt(y));
                        }
                    });
                    //小于
                    condition.get(7).forEach((x, y) -> {
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.rangeQuery(x).lt(y));
                        }
                    });
                    //大于等于
                    condition.get(8).forEach((x, y) -> {
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.rangeQuery(x).gte(y));
                        }
                    });
                    //小于等于
                    condition.get(9).forEach((x, y) -> {
                        if (k.equals(x) && v.equals(y)) {
                            boolQueryBuilder.must(QueryBuilders.rangeQuery(x).lte(y));
                        }
                    });
                });
            }
            //组装排序条件
            condition.get(10).forEach((k, v) -> {
                if (v.equals("asc")) {
                    searchSourceBuilder.sort(k, SortOrder.ASC);
                } else if (v.equals("desc")) {
                    searchSourceBuilder.sort(k, SortOrder.DESC);
                }
            });
            //组装分组条件
//            TermsAggregationBuilder aggregationBuilder = null;
            int i=0;
            wrapperGroupBy(condition.get(11),i,firstAgg,aggs,searchSourceBuilder);
//            if(condition.get(11).size()>0){
//                for (Map.Entry<String, Object> m : condition.get(11).entrySet()) {
//                    if(i==0){
//                        if(m.getKey().contains(".keyword")){
//                            aggregationBuilder=AggregationBuilders
//                                    .terms(m.getKey().replace(".keyword","") + "Agg")
//                                    .field(m.getKey()).order(BucketOrder.count(true)).size(Integer.MAX_VALUE);
//                        }else{
//                            aggregationBuilder=AggregationBuilders
//                                    .terms(m.getKey().replace(".keyword","") + "Agg")
//                                    .field(m.getKey()).order(BucketOrder.count(true)).size(Integer.MAX_VALUE)
//                                    .subAggregation(AggregationBuilders.min("min_"+m.getKey()).field(m.getKey()))
//                                    .subAggregation(AggregationBuilders.max("max_"+m.getKey()).field(m.getKey()))
//                                    .subAggregation(AggregationBuilders.avg("avg_"+m.getKey()).field(m.getKey()))
//                                    .subAggregation(AggregationBuilders.sum("sum_"+m.getKey()).field(m.getKey()));
//                        }
//                        firstAgg=m.getKey();
//                        aggs.add(m.getKey() + "Agg");
//                        i++;
//                    }
//                }
//                if(condition.get(11).size()>1){
//                    for (Map.Entry<String, Object> m : condition.get(11).entrySet()) {
//                        if(m.getKey()!=firstAgg){
//                            if(m.getKey().contains(".keyword")){
//                                aggregationBuilder.subAggregation(AggregationBuilders
//                                        .terms(m.getKey().replace(".keyword","") + "Agg")
//                                        .field(m.getKey()).order(BucketOrder.count(true))).size(Integer.MAX_VALUE);
//                            }else{
//                                aggregationBuilder.subAggregation(AggregationBuilders
//                                        .terms(m.getKey().replace(".keyword","") + "Agg")
//                                        .field(m.getKey()).order(BucketOrder.count(true))).size(Integer.MAX_VALUE)
//                                        .subAggregation(AggregationBuilders.min("min_"+m.getKey()).field(m.getKey()))
//                                        .subAggregation(AggregationBuilders.max("max_"+m.getKey()).field(m.getKey()))
//                                        .subAggregation(AggregationBuilders.avg("avg_"+m.getKey()).field(m.getKey()))
//                                        .subAggregation(AggregationBuilders.sum("sum_"+m.getKey()).field(m.getKey()));
//                            }
//
//                            aggs.add(m.getKey() + "Agg");
//                        }
//                    }
//                }
//                searchSourceBuilder.aggregation(aggregationBuilder);
//            }
        }


        searchSourceBuilder.query(boolQueryBuilder).from((Integer) condition.get(0).get("from")).size((Integer) condition.get(0).get("size"));
        Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(condition.get(0).get("indexName").toString()).addType(condition.get(0).get("type").toString())
                .build();
        System.err.println("[查询语句]：{}" + searchSourceBuilder.toString());
        try {
            SearchResult result = EsJestUtil.jestClient().execute(search);
            System.out.println(result);
            Map<String,Map<String,Double>> aggregationMap=new HashMap<>();
            if(aggs.size()>0){
                List<TermsAggregation.Entry> keyOneAgg =
                        result.getAggregations().getTermsAggregation(getRealName(aggs.get(0))+"Agg").getBuckets();

                for (TermsAggregation.Entry entry : keyOneAgg) {
                    Map<String,Double> functionMap=new HashMap<>();
                    functionMap.put("count",entry.getCount().doubleValue());
                    if(!firstAgg.toString().contains(".keyword")){
                        Double avg=entry.getAvgAggregation("avg_"+getRealName(aggs.get(0))).getAvg();
                        Double max=entry.getMaxAggregation("max_"+getRealName(aggs.get(0))).getMax();
                        Double min=entry.getMinAggregation("min_"+getRealName(aggs.get(0))).getMin();
                        Double sum=entry.getSumAggregation("sum_"+getRealName(aggs.get(0))).getSum();
                        functionMap.put("avg",avg);
                        functionMap.put("max",max);
                        functionMap.put("min",min);
                        functionMap.put("sum",sum);
                        aggregationMap.put(entry.getKeyAsString(),functionMap);
                    }
                    if(aggs.size()>1) {
                        List<TermsAggregation.Entry> keyTwoAgg = entry.getTermsAggregation(getRealName(aggs.get(1))+"Agg").getBuckets();
                        //循环每一个桶，拿到里面的聚合，再拿桶
                        for (TermsAggregation.Entry keyTwoEntry : keyTwoAgg) {
                            Double avg1=entry.getAvgAggregation("avg_"+getRealName(aggs.get(1))).getAvg();
                            Double max1=entry.getMaxAggregation("max_"+getRealName(aggs.get(1))).getMax();
                            Double min1=entry.getMinAggregation("min_"+getRealName(aggs.get(1))).getMin();
                            Double sum1=entry.getSumAggregation("sum_"+getRealName(aggs.get(1))).getSum();

                            functionMap.put("count",keyTwoEntry.getCount().doubleValue());
                            functionMap.put("avg",avg1);
                            functionMap.put("max",max1);
                            functionMap.put("min",min1);
                            functionMap.put("sum",sum1);
                            aggregationMap.remove(entry.getKeyAsString());
                            aggregationMap.put(entry.getKeyAsString()+";"+keyTwoEntry.getKeyAsString(),functionMap);
                        }
                    }
                }
            }

            if (result.isSucceeded()) {
                List<T> list = result.getSourceAsObjectList(clazz);
//                for (T t:list
//                     ) {
//                    Field f = page.getClass().getDeclaredField("aggregationMap");
//                    f.setAccessible(true);
//                    f.set(t, aggregationMap);
//                }
                page.setAggregationMap(aggregationMap);
                page.setRecords(list);
                page.setTotal(result.getTotal());
            }
            return page;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRealName(String str){
        String str1=str.replace(".keyword","");
        return str1.substring(0,str1.length()-3);
    }

    void wrapperGroupBy(Map<String, Object> groupMap,int i,StringBuilder firstAgg,List<String> aggs,SearchSourceBuilder searchSourceBuilder){
        TermsAggregationBuilder aggregationBuilder = null;
        if(groupMap.size()>0){
            for (Map.Entry<String, Object> m : groupMap.entrySet()) {
                if(i==0){
                    if(m.getKey().contains(".keyword")){
                        aggregationBuilder=AggregationBuilders
                                .terms(m.getKey().replace(".keyword","") + "Agg")
                                .field(m.getKey()).order(BucketOrder.count(true)).size(Integer.MAX_VALUE);
                    }else{
                        aggregationBuilder=AggregationBuilders
                                .terms(m.getKey().replace(".keyword","") + "Agg")
                                .field(m.getKey()).order(BucketOrder.count(true)).size(Integer.MAX_VALUE)
                                .subAggregation(AggregationBuilders.min("min_"+m.getKey()).field(m.getKey()))
                                .subAggregation(AggregationBuilders.max("max_"+m.getKey()).field(m.getKey()))
                                .subAggregation(AggregationBuilders.avg("avg_"+m.getKey()).field(m.getKey()))
                                .subAggregation(AggregationBuilders.sum("sum_"+m.getKey()).field(m.getKey()));
                    }
                    firstAgg=firstAgg.append(m.getKey());
                    aggs.add(m.getKey() + "Agg");
                    i++;
                }
            }
            if(groupMap.size()>1){
                for (Map.Entry<String, Object> m : groupMap.entrySet()) {
                    if(!m.getKey().equals(firstAgg.toString())){
                        if(m.getKey().contains(".keyword")){
                            aggregationBuilder.subAggregation(AggregationBuilders
                                    .terms(m.getKey().replace(".keyword","") + "Agg")
                                    .field(m.getKey()).order(BucketOrder.count(true))).size(Integer.MAX_VALUE);
                        }else{
                            aggregationBuilder.subAggregation(AggregationBuilders
                                    .terms(m.getKey().replace(".keyword","") + "Agg")
                                    .field(m.getKey()).order(BucketOrder.count(true))).size(Integer.MAX_VALUE)
                                    .subAggregation(AggregationBuilders.min("min_"+m.getKey()).field(m.getKey()))
                                    .subAggregation(AggregationBuilders.max("max_"+m.getKey()).field(m.getKey()))
                                    .subAggregation(AggregationBuilders.avg("avg_"+m.getKey()).field(m.getKey()))
                                    .subAggregation(AggregationBuilders.sum("sum_"+m.getKey()).field(m.getKey()));
                        }

                        aggs.add(m.getKey() + "Agg");
                    }
                }
            }
            searchSourceBuilder.aggregation(aggregationBuilder);
        }
    }

    public static void main(String[] args) {
        Example example = new Example("index_name", "_doc");
        example.setFrom(0);
        example.setSize(10);
        example.createQueryCondition().andDeviceNameEqualTo("aaaa");
//        example.createQueryCondition().orDeviceNameEqualTo("");
//        example.createQueryCondition().andDeviceNameLike("");
//        example.createQueryCondition().andNameEqualTo("abc");
//        example.createQueryCondition().groupByDeviceName();
        example.createQueryCondition().groupByDeviceName();
        EsJestUtil esJestUtil = new EsJestUtil();
        Page<DeviceVO> page = esJestUtil.query(example.createQueryCondition().conditions, DeviceVO.class);
            page.getAggregationMap().forEach((k,v)->{
                System.out.println(k+" : "+v);
            });
//        System.out.println(page.getRecords());
//        System.out.println(page.getPages());
//        System.out.println(page.getTotal());
    }
}
