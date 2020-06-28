# EsJestUtil
基于JestClient，模仿MybatisExample写法（ElasticSearch，Java）

        ```Example example = new Example("index_name", "_doc");
        example.setFrom(0);
        example.setSize(10);
        example.createQueryCondition().andDeviceNameEqualTo("aaaa");

        example.createQueryCondition().groupByDeviceName();
        EsJestUtil esJestUtil = new EsJestUtil();
        Page<DeviceVO> page = esJestUtil.query(example.createQueryCondition().conditions, DeviceVO.class);
            page.getAggregationMap().forEach((k,v)->{
                System.out.println(k+" : "+v);
            });```
