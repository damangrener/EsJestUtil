package com.wtf.esjest.esjestutil.page;

public class PageHelper {

    public static int offsetCurrent(int current, int size) {
        return current > 0 ? (current - 1) * size : 0;
    }

    public static int offsetCurrent(Pagination pagination) {
        return null == pagination ? 0 : offsetCurrent(pagination.getCurrent(), pagination.getSize());
    }
}
