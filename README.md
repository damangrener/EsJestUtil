# EsJestUtil
基于JestClient，模仿MybatisExample写法（ElasticSearch，Java）
注意：其中的分页是参考来的，但是我找不到参考的地址了，大部分思想也是仿照他的写法改的，感谢一下。之前用注解的方式封装过，但是使用复杂，于是模仿mybatis。

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
